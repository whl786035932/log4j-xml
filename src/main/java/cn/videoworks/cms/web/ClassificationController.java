package cn.videoworks.cms.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.videoworks.cms.client.StorageClient;
import cn.videoworks.cms.constant.ResponseDictionary;
import cn.videoworks.cms.dto.ClassificationContentMappingDto;
import cn.videoworks.cms.dto.ClassificationDto;
import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.dto.LogDto;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.dto.RecommonedDto;
import cn.videoworks.cms.dto.StorageRequestDto;
import cn.videoworks.cms.dto.StorageResponseDto;
import cn.videoworks.cms.entity.Classification;
import cn.videoworks.cms.entity.ClassificationContentMapping;
import cn.videoworks.cms.entity.Content;
import cn.videoworks.cms.entity.ContentPosterMapping;
import cn.videoworks.cms.entity.Poster;
import cn.videoworks.cms.entity.Source;
import cn.videoworks.cms.enumeration.BusinessType;
import cn.videoworks.cms.enumeration.ContentStatus;
import cn.videoworks.cms.enumeration.ContentType;
import cn.videoworks.cms.enumeration.LogLevel;
import cn.videoworks.cms.enumeration.LogSource;
import cn.videoworks.cms.enumeration.LogType;
import cn.videoworks.cms.enumeration.UserType;
import cn.videoworks.cms.service.ClassificationContentMappingService;
import cn.videoworks.cms.service.ClassificationService;
import cn.videoworks.cms.service.ContentService;
import cn.videoworks.cms.service.PosterService;
import cn.videoworks.cms.service.SourceService;
import cn.videoworks.cms.util.ApiResponse;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.util.Md5Util;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.cms.util.UserUtil;
import cn.videoworks.cms.vo.ContentClassificationVo;
import cn.videoworks.cms.vo.UserVo;
import cn.videoworks.commons.util.json.JsonConverter;
import cn.videoworks.commons.webdev.constant.ResponseStatusCode;

/**
 * 分类 ClassificationController
 * 
 * @author Pei
 * 
 */
@Controller
@RequestMapping("classification")
public class ClassificationController {
	private static final Logger log = LoggerFactory
			.getLogger(ClassificationController.class);
	@Resource
	private ClassificationService service;
	@Resource
	private ClassificationContentMappingService cfmservice;
	@Resource
	private ContentService contentServiceImpl;
	@Resource
	private Properties databaseConfig;
	@Resource
	private SourceService sourceServiceImpl;
	@Resource
	private PosterService posterServiceImpl;
	
	/**
	 * 分类管理页面显示.
	 */
	@RequestMapping(value = "classificationManage", method = RequestMethod.GET)
	public String showClassification(Model model) {
		ClassificationDto tree = getClassificationTree();
		if (tree != null) {
			model.addAttribute("treeJson", JsonConverter.format(tree));
		} else {
			model.addAttribute("treeJson", null);
		}
		return "site.cms.classification.classification_manage";
	}

	/**
	 * 获取栏目树.
	 */
	public ClassificationDto getClassificationTree() {
		// 获取分类根节点
				List<ClassificationDto> all = service.getClassification();
				if (all != null && all.size() > 1) {
					for (int i = 1; i < all.size(); i++) {
						for (int j = i - 1; j >= 0; j--) {
							if (all.get(j).getId().equals(all.get(i).getpId())) {
								all.get(j).getChildren().add(all.get(i));
							}
							if (all.get(i).getId().equals(all.get(j).getpId())) {
								all.get(i).getChildren().add(all.get(j));
							}
						}
					}
				}
				// 设置根节点父id
				if (all != null && all.size() > 0) {
					ClassificationDto tree = null;
					for (ClassificationDto d : all) {
						if(d.getpId().equals("0")){
							tree = d;
							break;
						}
					}
					tree.setNocheck(true);
					return tree;
				}
				return null;
	}
	/**
	 * 获取栏目树.
	 */
	@RequestMapping(value = "getTree", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse getTree(HttpServletRequest request) {
		UserVo user = (UserVo)request.getSession().getAttribute("user"); 
		RestResponse response = null;
		Map<String, Object> tree = new HashMap<String, Object>();
		ClassificationDto cc = new ClassificationDto();
		if(null != user && null != user.getType() && user.getType() == UserType.SUPER.getValue()) {
			cc = getClassificationTree();
		}else {
			cc = user.getClassifications();
		}
		
		if (cc != null) {
			tree.put("tree", JsonConverter.format(cc));
			response = new RestResponse();
			response.setData(tree);
			response.setStatusCode(200);
			return response;
		} else {
			tree.put("tree", null);
			response = new RestResponse();
			response.setData(tree);
			response.setStatusCode(200);
		}
		return response;
	}

	/**
	 * 分类id显示分类详情.
	 */
	@RequestMapping(value = "getClassificationDetail", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse getClassificationDetail(String id) {
		RestResponse response = null;
		Map<String, Object> result = service.getDetail(id);
		if (result == null) {
			response = new RestResponse();
			response.setStatusCode(500);
			return response;
			// log.debug("分类返回结果:" + JsonConverter.format(result));
		}
		if (result.containsKey("icon")) {
			if (result.get("icon") == null || result.get("icon").equals("")) {
				result.put("code", 1);
			} else {
				result.put("code", 2);
			}
		}
		response = new RestResponse();
		response.setStatusCode(200);
		response.setData(result);
		return response;
	}

	/**
	 * 保存节点详情.
	 */
	@RequestMapping(value = "saveClassificationManage", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse saveClassificationManage(
			@RequestBody ClassificationDto dto,HttpServletRequest request) {
		Map<String,Object> detail = new HashMap<String,Object>();
		LogDto logDto = ConvertDto.buildOperationLog("CMS-更新分类结点", BusinessType.UPDATE.getValue(), "分类管理", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), detail);
		log.info(JsonConverter.format(logDto));
		RestResponse response = null;
		// 判断参数
		if (dto == null) {
			log.debug("参数传输错误:参数为空");
			response = new RestResponse();
			response.setStatusCode(400);
			response.setMessage("参数传输错误!");
			return response;
		}
		int i = service.saveClassification(dto);
		response = new RestResponse();
		if (i > 0) {
			log.debug("分类节点信息保存成功!");
			response.setStatusCode(200);
			response.setMessage("分类节点信息保存成功!");
			return response;
		}
		log.debug("分类节点信息保存失败!");
		response.setStatusCode(500);
		response.setMessage("分类节点信息保存失败!");
		return response;

	}

	/**
	 * 保存节点详情.
	 */
	@RequestMapping(value = "addClassificationManage", method = RequestMethod.POST)
	@ResponseBody
	public synchronized RestResponse addClassificationManage(
			@RequestBody ClassificationDto dto,HttpServletRequest request) {
		Map<String,Object> detail = new HashMap<String,Object>();
		LogDto logDto = ConvertDto.buildOperationLog("CMS-更新结分类点详情", BusinessType.UPDATE.getValue(), "分类管理", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), detail);
		log.info(JsonConverter.format(logDto));
		RestResponse response = null;
		// 判断参数
		if (dto == null) {
			log.debug("参数传输错误:参数为空");
			response = new RestResponse();
			response.setStatusCode(400);
			response.setMessage("参数传输错误!");
			return response;
		}
		//if (dto.getpId() == null) {
			log.debug("创建跟节点");
			List<Classification>list = service.getByPid(dto.getpId());
			if(list!=null){
				dto.setSequence(list.size());
			}
			int i = service.add(dto);
			if (i > 0) {
				log.debug("节点添加成功!");
				response = new RestResponse();
				response.setStatusCode(200);
				response.setMessage("节点添加成功!");
				return response;
			} else {
				log.debug("节点添加失败!");
				response = new RestResponse();
				response.setStatusCode(500);
				response.setMessage("节点添加失败!");
				return response;
			}
	}

	/**
	 * 删除节点.
	 * 
	 */
	@RequestMapping(value = "removeClassificationManage", method = RequestMethod.POST)
	@ResponseBody
	public synchronized RestResponse removeClassificationManage(
			@RequestBody ClassificationDto dto,HttpServletRequest request) {
		Map<String,Object> detail = new HashMap<String,Object>();
		LogDto logDto = ConvertDto.buildOperationLog("CMS-删除分类结点", BusinessType.DELETE.getValue(), "分类管理", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), detail);
		log.info(JsonConverter.format(logDto));
		RestResponse response = null;
		// 判断参数
		if (dto == null) {
			log.debug("参数传输错误:参数为空");
			response = new RestResponse();
			response.setStatusCode(400);
			response.setMessage("参数传输错误!");
			return response;
		}
		Classification c = service.get(dto.getId());
		// 查询当前节点和子节点
		List<Classification> list = service.select(dto);
		// 删除关联数据
		if (list != null) {
			for (Classification ccf : list) {
				cfmservice.delete(ccf.getId());
			}
		}
		// 删除当前节点
		int i = service.remove(dto);
		// 获取同级别序列号后边的值
		if (c.getParent() != null) {
			List<Classification> listc = service.selectElevel(c.getParent(), c.getSequence());
			if (listc != null && listc.size() > 0) {
				for (Classification cf : listc) {
					if (cf.getSequence() != null) {
						cf.setSequence(cf.getSequence() - 1);
					}
					cf.setUpdated_at(new Date());
					service.update(cf);
				}
			}
		}
		if (i <= 0) {
			log.debug("节点删除失败");
			response = new RestResponse();
			response.setStatusCode(500);
			response.setMessage("节点删除失败!");
			return response;
		}
		log.debug("节点删除成功");
		response = new RestResponse();
		response.setStatusCode(200);
		response.setMessage("节点删除成功!");
		return response;
	}

	/**
	 * 拖拽分类树后保存数据.
	 */
	@RequestMapping(value = "saveDrop", method = RequestMethod.POST)
	@ResponseBody
	public synchronized RestResponse saveDrop(@RequestBody Map<String, Object> param,HttpServletRequest request) {
		RestResponse response = new RestResponse();
		Map<String,Object> detail = new HashMap<String,Object>();
		LogDto logDto = ConvertDto.buildOperationLog("CMS-分类排序", BusinessType.ORDERCLASSIFICATION.getValue(), "分类管理", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), detail);
		log.info(JsonConverter.format(logDto));
		if (param == null) {
			log.debug("参数为空!");
			response.setStatusCode(400);
			response.setMessage("传输参数为空!");
			return response;
		}
		log.debug("参数为" + JsonConverter.format(param));
		String moveType = param.containsKey("moveType") ? (String) param.get("moveType") : null;
		String id = param.containsKey("id") ? (String) param.get("id") : null;
		String tid = param.containsKey("tid") ? (String) param.get("tid") : null;
		if (id == null || tid == null || moveType == null) {
			response.setStatusCode(500);
			response.setMessage("参数传输失败");
			return response;
		}
		// 获取目标点
		Classification tc = service.get(tid);
		// 获取移动点
		Classification yc = service.get(id);
		// moveType --prev:上移 --next:后移 --inner:放入里边
		// list合并
		List<Classification> all = new ArrayList<>();
		// 源层级
		Integer yl = yc.getLevel();
		// 目标层级
		Integer tl = tc.getLevel();
		// 源序列号
		Integer yq = yc.getSequence();
		// 目标序列号
		Integer tq = tc.getSequence();
		// 源节点父id
		String yp = yc.getParent();
		// 目标点父id
		String tp = tc.getParent();
		if (moveType.equals("prev")) {
			int lec = 0;
			if (yl != null && tl != null) {
				lec = tl - yl;
			}
			// 更改父节点
			yc.setParent(tc.getParent());
			// 更改层级
			yc.setLevel(tc.getLevel());
			// 更改更新时间
			yc.setUpdated_at(new Date());
			tc.setUpdated_at(new Date());
			// 目标点排序
			if (tq != null) {
				// 序号
				yc.setSequence(tq);
				tc.setSequence(tq + 1);
			}
			// 目标点同层级序列号大
			List<Classification> tlist = service.selectElevel(tp, tq);
			if (tlist != null && tlist.size() > 0) {
				for (Classification t : tlist) {
					if (t.getId().equals(yc.getId())) {
						tlist.remove(t);
						break;
					}
				}
			}
			// 移动点同层级序列号大列表
			List<Classification> ylist = service.selectElevel(yp, yq);
			checList(tlist, ylist);
			// 目标点同层排序加一
			if (tlist != null && tlist.size() >0) {
				for (Classification t : tlist) {
					t.setUpdated_at(new Date());
					if (t.getSequence() != null) {
						t.setSequence(t.getSequence() + 1);
					}
				}
			}
			if (ylist != null && ylist.size() > 0) {
				for (Classification y : ylist) {
					y.setUpdated_at(new Date());
					if (y.getSequence() != null) {
						y.setSequence(y.getSequence() - 1);
					}
				}
			}
			// 获取移动点所有子节点
			List<Classification> topC = getTopChildren(yc.getId());
			List<Classification> allC = new ArrayList<>();
			if(topC!=null&&topC.size()>0){
				allC = getAllChildren(topC, allC);
			}
			//更改level
			if (allC != null && allC.size() > 0) {
				for (Classification cc : allC) {
					if (cc.getLevel() != null) {
						cc.setLevel(cc.getLevel() + lec);
					}
				}
			}
			all.add(yc);
			all.add(tc);
			if (tlist != null && tlist.size() > 0) {
				all.addAll(tlist);
			}
			if (ylist != null && ylist.size() > 0) {
				all.addAll(ylist);
			}
			if (allC != null && allC.size() > 0) {
				all.addAll(allC);
			}
		}
		// 下移
		if (moveType.equals("next")) {
//			// 源层级
//			Integer yl = yc.getLevel();
//			// 目标层级
//			Integer tl = tc.getLevel();
			int lec = 0;
			if (yl != null && tl != null) {
				lec = tl - yl;
			}
			// 更改父节点
			yc.setParent(tc.getParent());
			// 更改层级
			yc.setLevel(tl);
			// 更改更新时间
			yc.setUpdated_at(new Date());
			tc.setUpdated_at(new Date());
			// 目标点排序
			if (tp.equals(yp)) {
				if (tq != null) {
					yc.setSequence(tq);
					tc.setSequence(tq - 1);
				}
			} else {
				if (tq != null) {
					yc.setSequence(tq + 1);
				}
			}
			// 目标点同层级序列号大
			List<Classification> tlist = service.selectElevel(tp, tq);
			// 移动点同层级序列号大列表
			List<Classification> ylist = service.selectElevel(yp, yq);
			if (ylist != null && ylist.size() > 0) {
				for (Classification t : ylist) {
					if (t.getId().equals(tc.getId())) {
						ylist.remove(t);
						break;
					}
				}
			}
			checList(tlist, ylist);
			// 目标点同层排序加一
			if (tlist != null) {
				for (Classification t : tlist) {
					t.setUpdated_at(new Date());
					if (t.getSequence() != null) {
						t.setSequence(t.getSequence() + 1);
					}
				}
			}
			if (ylist != null) {
				for (Classification y : ylist) {
					y.setUpdated_at(new Date());
					if (y.getSequence() != null) {
						y.setSequence(y.getSequence() - 1);
					}
				}
			}
			// 获取移动点所有子节点
			List<Classification> topC = getTopChildren(yc.getId());
			List<Classification> allC = new ArrayList<>();
			if (topC != null && topC.size() > 0) {
				allC = getAllChildren(topC, allC);
			}
			// 更改level
			if (allC != null && allC.size() > 0) {
				for (Classification cc : allC) {
					if (cc.getLevel() != null) {
						cc.setLevel(cc.getLevel() + lec);
					}
				}
			}
			all.add(yc);
			all.add(tc);
			if (tlist != null && tlist.size() > 0) {
				all.addAll(tlist);
			}
			if (ylist != null && ylist.size() > 0) {
				all.addAll(ylist);
			}
			if (allC != null && allC.size() > 0) {
				all.addAll(allC);
			}
		}
		// 放置内部
		if (moveType.equals("inner")) {
			int lec = 0;
			if (yl != null && tl != null) {
				lec = tl + 1 - yl;
			}
			// 更改父节点
			yc.setParent(tc.getId());
			// 更改层级
			yc.setLevel(tl + 1);
			// 更改更新时间
			yc.setUpdated_at(new Date());
			tc.setUpdated_at(new Date());
			// 目标点排序
			yc.setSequence(1);
			// 目标点最高子集
			List<Classification> ttc = getTopChildren(tc.getId());
			if (ttc != null && ttc.size() > 0) {
				for (Classification c : ttc) {
					if (c.getSequence() != null) {
						c.setSequence(c.getSequence() + 1);
					}
				}
			}
			// 移动点同层级序列号大列表
			List<Classification> ylist = service.selectElevel(yp, yq);
			if (ylist != null && ylist.size() > 0) {
				for (Classification y : ylist) {
					y.setUpdated_at(new Date());
					if (y.getSequence() != null) {
						y.setSequence(y.getSequence() - 1);
					}
				}
			}
			// 获取移动点所有子节点
			List<Classification> topC = getTopChildren(yc.getId());
			List<Classification> allC = new ArrayList<>();
			if (topC != null && topC.size() > 0) {
				allC = getAllChildren(topC, allC);
			}
			// 更改level
			if (allC != null && allC.size() > 0) {
				for (Classification cc : allC) {
					if (cc.getLevel() != null) {
						cc.setLevel(cc.getLevel() + lec);
					}
				}
			}
			
			all.add(yc);
			if (ylist != null && ylist.size() > 0) {
				all.addAll(ylist);
			}
			if (allC != null && allC.size() > 0) {
				all.addAll(allC);
			}
		}
		// 更新结点
		try {
			for (Classification u : all) {
				service.update(u);
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			response.setMessage("数据保存失败");
			return response;
		}
		response.setStatusCode(200);
		response.setMessage("数据保存成功");
		return response;

	}

	/**
	 * 递归设置层级.
	 */
	private List<Classification> getAllChildren(List<Classification> topChildren, List<Classification> resultTrees) {
		if (topChildren != null && topChildren.size() > 0) {
			for (Classification tree : topChildren) {
				resultTrees.add(tree);
				List<Classification> children = getTopChildren(tree.getId());
				getAllChildren(children, resultTrees);
			}
		}
		return resultTrees;
	}

	/**
	 * 查询某父节点下的直接子节点(即不包含孙子节点等)
	 */
	public List<Classification> getTopChildren(String parentId) {
		return service.getTopChildren(parentId);
	}
	/**
	 * check List same
	 */
	private void checList(List<Classification> tlist,List<Classification> ylist){
		List<Classification> sList = null;
		if (tlist != null && tlist.size() > 0 && ylist!=null&&ylist.size()>0) {
			for(Classification c : tlist){
				if(ylist.contains(c)){
					if(sList == null){
						sList = new ArrayList<>();
					}
					sList.add(c);
				}
			}
			if (sList != null) {
				ylist.removeAll(sList);
				tlist.removeAll(sList);
			}
		}
	}
	/**
	 * 通过子节点id获取所有父节点.
	 */
	@RequestMapping(value = "getClassificationF/{id}", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse getClassificationF(@PathVariable(value = "id") String id) {
		RestResponse response = null;
		if (id == null) {
			log.debug("参数为空,参数传输失败!");
			response = new RestResponse();
			response.setStatusCode(400);
			response.setMessage("参数传输失败!");
			return response;
		}
		List<Classification> list = service.getClassificationF(id);
		if (list != null) {
			log.debug("数据获取成功");
			response = new RestResponse();
			response.setStatusCode(200);
			response.setMessage("数据获取成功!");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("data", list);
			response.setData(result);
			return response;
		}
		log.debug("数据获取失败");
		response = new RestResponse();
		response.setStatusCode(500);
		response.setMessage("数据获取失败!");
		return response;
	}

	
	/**
	 * 分类编排.
	 */
	@RequestMapping(value = "arrangement", method = RequestMethod.GET)
	public String classificationArrangement(Model model,HttpServletRequest request) {
		UserVo user = (UserVo)request.getSession().getAttribute("user"); 
		ClassificationDto tree = new ClassificationDto();
		if(null != user && null != user.getType() && user.getType() == UserType.SUPER.getValue()) {
			tree = getClassificationTree();
		}else {
			tree = user.getClassifications();
		}
		model.addAttribute("treeJson", JsonConverter.format(tree));
		return "site.cms.classification.classification_arrangement";
	}

	/**
	 * 分类编排获取分页数据.
	 */
	@RequestMapping(value = "ajax", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ajax(String data) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> param = ConvertDto.convertDataTableSearchForContent(data);
		Page page = ConvertDto.convertPage(data);
		// 通过节点id获取所有子节点(包括自己)
		String classid = param.containsKey("classid") == true ? param.get("classid").toString() : null;
		String status = param.containsKey("status") == true ? param.get("status").toString() : null;
		String title = param.containsKey("title") == true ? param.get("title").toString() : null;
		List<ClassificationContentMapping> list = cfmservice.list(classid,
				title, Integer.parseInt(status), "publishTime", page);
		List<ClassificationContentMappingDto> dtos = new ArrayList<ClassificationContentMappingDto>();
		for (ClassificationContentMapping cfcm : list) {
			List<ContentClassificationVo> classifs = new ArrayList<ContentClassificationVo>();
			if(cfcm.getContent().getClassificationContentMappingMappings() != null) {
				for(ClassificationContentMapping ccm : cfcm.getContent().getClassificationContentMappingMappings()) {
					String cId = ccm.getClassification() != null?ccm.getClassification().getId():"";
					// 获取所有父分类
					List<Classification> cfs = service.getClassificationF(cId);
					String hierarchyName = "";
					if (cfs != null) {
						for (int i = cfs.size()-1; i >=0; i--) {
							if (i == cfs.size()-1)
								hierarchyName =hierarchyName+ cfs.get(i).getName();
							else
								hierarchyName =hierarchyName+"/"+ cfs.get(i).getName() ;
						}
						ContentClassificationVo classificationVo = new ContentClassificationVo();
						classificationVo.setClassificationId(cId);
						classificationVo.setClassificationName(hierarchyName);
						// 组装内容详情数据
						classifs.add(classificationVo);
					}
				}
			}
			ClassificationContentMappingDto convertClassificationContent = ConvertDto.convertClassificationContent(cfcm);
			convertClassificationContent.setClassifs(classifs);
			dtos.add(convertClassificationContent);
		}
		page = cfmservice.paging(classid, title, Integer.parseInt(status), page);
		bulidParam(result, dtos, page);
		return result;
	}

	/**
	 * 设置页面返回参数.
	 */
	private void bulidParam(Map<String, Object> result,
			List<ClassificationContentMappingDto> dtos, Page page) {
		result.put("data", dtos);
		result.put("iTotalRecords", dtos.size());
		result.put("iTotalDisplayRecords", page.getRecordCount());
	}

	/**
	 * 从分类中移出内容.
	 * 
	 * @param map
	 *            分类id,内容id数组
	 */
	@RequestMapping(value = "removeContent", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse removeContent(@RequestBody Map<String, Object> param,HttpServletRequest request) {
		Map<String,Object> detail = new HashMap<String,Object>();
		LogDto logDto = ConvertDto.buildOperationLog("CMS-内容移出分类", BusinessType.REMOVECLASSIFICATION.getValue(), "分类管理", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), detail);
		log.info(JsonConverter.format(logDto));
		RestResponse response = new RestResponse();
		if (param == null) {
			log.debug("参数为空,参数传输失败!");
			response = new RestResponse();
			response.setStatusCode(400);
			response.setMessage("参数传输失败!");
			return response;
		}
		String classid = param.containsKey("classid") == true ? param.get(
				"classid").toString() : null;
		@SuppressWarnings("unchecked")
		List<Long> contentId = param.containsKey("contentIds") == true ? (List<Long>) param
				.get("contentIds") : null;
		// log.debug("需要删除的内容id为:" + contentId);
		int i = cfmservice.delete(classid, contentId);
		if (i > 0) {
			// log.debug("数据移出成功:" + i + "条");
			response = new RestResponse();
			response.setStatusCode(200);
			response.setMessage("数据移出成功");
			return response;
		}
		log.debug("数据移出失败!");
		response = new RestResponse();
		response.setStatusCode(500);
		response.setMessage("数据移出失败");
		return response;
	}


	/**
	 * 删除内容.
	 */
	@RequestMapping(value = "deleteContent", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse deleteContent(@RequestBody Map<String, Object> param,HttpServletRequest request) {
		RestResponse response = new RestResponse();
		Map<String,Object> detail = new HashMap<String,Object>();
		LogDto logDto = ConvertDto.buildOperationLog("CMS-内容删除", BusinessType.DELETE.getValue(), "分类管理", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), detail);
		log.info(JsonConverter.format(logDto));
		if (param == null) {
			log.debug("参数为空,参数传输失败!");
			response = new RestResponse();
			response.setStatusCode(400);
			response.setMessage("参数传输失败!");
			return response;
		}
		String classid = param.containsKey("classid") == true ? param.get(
				"classid").toString() : null;
		@SuppressWarnings("unchecked")
		List<Long> contentId = param.containsKey("contentIds") == true ? (List<Long>) param
				.get("contentIds") : null;
		log.debug("需要删除的内容id为:" + contentId);
		try {
			int i = cfmservice.delete(classid, contentId);
			if (null != contentId && contentId.size() > 0) {
				for (Long id : contentId) {
					Content content = contentServiceImpl.get(id);
					content.setStatus(ContentStatus.DELETE.getValue());
					contentServiceImpl.update(content);
				}
			}
			log.debug("数据删除成功:" + i + "条");
			response = new RestResponse();
			response.setStatusCode(200);
			response.setMessage("数据移出成功");
			return response;
		} catch (Exception e) {
			log.debug("数据移出失败!");
			response = new RestResponse();
			response.setStatusCode(500);
			response.setMessage("数据移出失败");
			e.printStackTrace();
		}
		return response;
	}
	
	@RequestMapping(value="/photo",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updatePhoto(@RequestParam("poster_file") MultipartFile file,HttpServletRequest request) {
		//记录添加站点-操作日志
		LogDto logDto = ConvertDto.buildOperationLog("CMS-上传海报", BusinessType.ADD.getValue(), "海报管理", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), new HashMap<String,Object>());
		log.info(JsonConverter.format(logDto));
		
		Map<String,Object> map = null;
		try {
			if(file.getSize() >0){
				Map<String, String> param = uploadFile(file);
				if(param == null||!param.get("statusCode").toString().equals("200")){
					map = new HashMap<String,Object>();
					map.put("statusCode",ResponseDictionary.ERROR);
					if(param.containsKey("message")){
						map.put("message",param.get("message"));
					}else{
						map.put("message","图片上传临时服务器失败!");
					}
					log.info("图片上传临时服务器失败!");
					return map;
				}
				// 存库
				Poster poster = bulidPoster(param);
				// 图片加密
				poster.setCheckSum(Md5Util.getMD5(file.getBytes()));
				// 图片大小
				poster.setSize(new Long(file.getSize()).intValue());
				Source source = sourceServiceImpl.get(cn.videoworks.cms.enumeration.Source.LOCAL.getValue());
				if(null == source) {
					source = ConvertDto.convertSource(cn.videoworks.cms.enumeration.Source.LOCAL.getName(),cn.videoworks.cms.enumeration.Source.LOCAL.getValue());
					sourceServiceImpl.save(source);
				}
				poster.setSource(source.getName());//文件源
				poster.setDescription(null);// 描述
				// 数据存库
				posterServiceImpl.save(poster);
				ApiResponse storeResult = storageImg(poster);
				if(storeResult==null){
					map = new HashMap<String,Object>();
					map.put("statusCode",ResponseDictionary.ERROR);
					map.put("message","图片上传临时服务器失败!");
					log.info("图片存储失败!");
				}
				log.info("存储work响应数据："+JsonConverter.format(storeResult));
				map = checkStoreResult(storeResult,poster);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 上传到临时服务器.
	 */
	private Map<String, String> uploadFile(MultipartFile file) {
		Map<String, String> result = null;
		if (file != null && file.getSize() > 0) {
			String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			String logImageName = UUID.randomUUID().toString() + suffix;// 构建文件名称
			/** 拼成完整的文件保存路径加文件* */
			String dir = databaseConfig.getProperty("poster.dir");
			File fdir = new File(dir);
			if (!fdir.exists() && !fdir.isDirectory()) {
				// 文件夹不存在,创建文件夹
				fdir.mkdir();
			}
	        String fileName = dir + File.separator + logImageName;
	        File files = new File(fileName);
	        try {
	        	// 获取图片大小
				BufferedImage bi = ImageIO.read(file.getInputStream());
				if(bi==null){
					result = new HashMap<>();
		        	result.put("statusCode", "500");
		        	result.put("message", "非图片文件");
		        	return result;
				}
				int width = bi.getWidth();
				int hight = bi.getHeight();
				file.transferTo(files);
				result = new HashMap<>();
				result.put("statusCode", "200");
				result.put("fileName", fileName);
				result.put("fileWidth", String.valueOf(width));
				result.put("fileHight", String.valueOf(hight));
				result.put("fileRealName", file.getOriginalFilename());
	        } catch (Exception e) {
	        	result = new HashMap<>();
	        	result.put("statusCode", "500");
	            e.printStackTrace();
	        }
	        log.info("传到服务器的文件的绝对路径:"+fileName);
		}
		return result;
	}

	/**
	 * 生成poster 实体.
	 * 
	 * @param param
	 * @return
	 */
	private Poster bulidPoster(Map<String, String> param) {
		Poster poster = new Poster();
		poster.setFileName(param.get("fileRealName"));// 图片名称
		poster.setHeight(Integer.valueOf(param.get("fileHight")));// 图片高度
		poster.setInsertedAt(new Date());// 插入时间
		poster.setStatus(1);// 状态
		poster.setUpdatedAt(new Date());// 更新时间
		poster.setUrl(param.get("fileName"));// 图片路径
		poster.setSourceUrl(param.get("fileName"));// 图片路径
		poster.setWidth(Integer.valueOf(param.get("fileWidth")));// 图片宽度
		return poster;
	}
	/**
	 * 图片正式存储.
	 */
	private ApiResponse storageImg(Poster poster) {
		ApiResponse response = null;
		List<StorageRequestDto> res = new ArrayList<StorageRequestDto>();
		StorageRequestDto dto = ConvertDto.convertStorageRequestDto(poster);
		res.add(dto);
		try {
			response = StorageClient.getStorageClient(
					databaseConfig.getProperty("gearman.ip"),
					Integer.valueOf(databaseConfig.getProperty("gearman.port")))
					.execute(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	/**
	 * 存储返回结果.
	 */
	private Map<String, Object> checkStoreResult(ApiResponse result,
			Poster poster) {
		Map<String, Object> param = null;
		// 数据正常返回
		try {
			deleteFile(poster); // 删除临时服务器文件
			if (result.getStatusCode() != ResponseDictionary.SUCCESS) {
				// 清除内容、海报、媒体关系
				posterServiceImpl.delete(poster.getId());
				if (param == null) {
					param = new HashMap<>();
				}
				param.put("statusCode", ResponseDictionary.EXTERNALINTERFACECALLSEXCEPTION);
				param.put("message",result.getMessage());
				param.put("url",null);
				return param;
			} else {// 处理成功
				ApiResponse rest = JsonConverter.parse(result.getData().toString(), ApiResponse.class);
				List<StorageResponseDto> storageResponses = JsonConverter
						.asList(JsonConverter.format(rest.getData()),
								StorageResponseDto.class);
				String url = null;
				for (StorageResponseDto dto : storageResponses) {
					int type = dto.getType();
					if (type == ContentType.POSTER.getValue()) {
						Poster p = posterServiceImpl.get(dto.getId());
						url = dto.getUrl();
						posterServiceImpl.delete(p.getId());
					} else {
						log.error("存储work响应未知类型，无法进行数据处理！");
					}
				}
				if(param == null){
					param = new HashMap<>();
				}
				param.put("statusCode", ResponseDictionary.SUCCESS);
				param.put("message",result.getMessage());
				param.put("url",url);
				return param;
			}
		} catch (Exception e) {
			e.printStackTrace();
			posterServiceImpl.delete(poster.getId());// 清除内容、海报、媒体关系
			deleteFile(poster); // 删除临时服务器文件
			log.error("解析存储 work响应数据失败：" + e.getMessage());
		}
		return null;
	}
	@RequestMapping(value="/checkName",method=RequestMethod.POST)
	@ResponseBody
	public RestResponse checkName(@RequestBody ClassificationDto dto){
		RestResponse response = null;
		List<Classification>list = service.get(dto);
		if(list==null||list.size()==0){
			response = new RestResponse();
			response.setStatusCode(200);
			return response;
		}
		response = new RestResponse();
		response.setStatusCode(500);
		return response;
	}
	
	/**
	 * 删除临时服务器上文件.
	 */
	private void deleteFile(Poster poster){
		try {
			if(poster!=null){
				File file = new File(poster.getUrl());
				if(file.exists()){
					file.delete();
				}
				log.info("删除临时服务器文件成功");
			}
		} catch (Exception e) {
			log.info("删除临时服务器文件失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * operation:(批量内容上下架)
	 * 
	 * @author meishen
	 * @Date 2018 2018年5月31日 下午6:38:08
	 * @param id
	 * @param status
	 * @return
	 * @return Map<String,Object>
	 * @throws
	 * @since Videoworks　Ver 1.1
	 */
	@RequestMapping("/{status}/upOrdown")
	@ResponseBody
	public Map<String, Object> operation(@RequestParam List<Long> ids,@PathVariable int status,HttpServletRequest request) {
		String bisinessBype = BusinessType.DOWN.getValue();
		if(status == ContentStatus.SHELVED.getValue()) 
			bisinessBype = BusinessType.UP.getValue();
		//记录添加站点-操作日志
		LogDto logDto = ConvertDto.buildOperationLog("CMS-分类下修改内容", bisinessBype, "分类编排", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), new HashMap<String,Object>());
		log.info(JsonConverter.format(logDto));
		int ok = 0;int error =0;
		String subMessage="";
		if (null != ids && ids.size() > 0) {
			for (Long id : ids) {
				Content content = contentServiceImpl.get(id);
				if (content.getStatus() != status) {
					if(status == ContentStatus.SHELVED.getValue()) {
						Content content2 = contentServiceImpl.get(content.getTitle(),ContentStatus.SHELVED.getValue());
						if(content2 == null) {
							content.setStatus(status);
							content.setUpdatedAt(DateUtil.getNowTimeStamp());
							contentServiceImpl.update(content);
							ok++;
						}else {
							subMessage="标题重复且是上架状态";
							error++;
						}
					}else {
						content.setStatus(status);
						content.setUpdatedAt(DateUtil.getNowTimeStamp());
						contentServiceImpl.update(content);
						ok++;
					}
				}else {
					subMessage="已经是"+bisinessBype+"状态";
					error++;
				}
			}
		}
		String message="成功【"+ok+"】条，失败【"+error+"】条！";
		if(error > 0)
			message=message +"失败原因【"+subMessage+"】";
		return ConvertDto.buildRestResponse(ResponseStatusCode.OK, message, null);
	}
	
	/**
	 * delete:(批量内容删除)
	 * 
	 * @author meishen
	 * @Date 2018 2018年5月31日 下午6:43:02
	 * @param id
	 * @return
	 * @return Map<String,Object>
	 * @throws
	 * @since Videoworks　Ver 1.1
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam List<Long> ids,HttpServletRequest request) {
		//记录添加站点-操作日志
		Map<String,Object> detail = new HashMap<String,Object>();
		detail.put("param", JsonConverter.format(ids));
		LogDto logDto = ConvertDto.buildOperationLog("CMS-分类下删除内容", BusinessType.DELETE.getValue(), "分类编排", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), detail);
		log.info(JsonConverter.format(logDto));
				
		if (null != ids && ids.size() > 0) {
			for (Long id : ids) {
				Content content = contentServiceImpl.get(id);
				content.setStatus(ContentStatus.DELETE.getValue());
				content.setUpdatedAt(DateUtil.getNowTimeStamp());
				contentServiceImpl.update(content);
			}
		}
		return ConvertDto.buildRestResponse(ResponseStatusCode.OK, "成功", null);
	}

	/**
	 * getRecommoned (获取推荐数据).
	 * 
	 * @return Map<String,Object>
	 */
	@RequestMapping("/getRecommoned")
	@ResponseBody
	public RestResponse getRecommoned(String id) {
		RestResponse result = null;
		List<ClassificationContentMapping> list = cfmservice.getRecommoned(id);
		List<RecommonedDto> reds = buildRR(list);
		RecommonedDto dto1 = null;
		RecommonedDto dto2 = null;
		RecommonedDto dto3 = null;
		if (reds != null) {
			for (RecommonedDto dto : reds) {
				if (dto.getRecommoned() == 1) {
					dto1 = dto;
					break;
				}
			}
			for (RecommonedDto dto : reds) {
				if (dto.getRecommoned() == 2) {
					dto2 = dto;
					break;
				}
			}
			for (RecommonedDto dto : reds) {
				if (dto.getRecommoned() == 3) {
					dto3 = dto;
					break;
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dto1", dto1);
		map.put("dto2", dto2);
		map.put("dto3", dto3);
		result = new RestResponse();
		result.setStatusCode(200);
		result.setData(map);
		return result;
	}
	
	private List<RecommonedDto> buildRR(List<ClassificationContentMapping> list) {
		List<RecommonedDto> dtos = null;
		if (list != null) {
			for (ClassificationContentMapping ccm : list) {
				RecommonedDto dto = new RecommonedDto();
				dto.setId(ccm.getId());
				dto.setOldId(ccm.getId());
				dto.setRecommoned(ccm.getRecommend());
				String url = null;
				String title = null;
				String size = null;
				String wh = null;
				if(ccm.getContent()!=null){
					Content c = ccm.getContent();
					title = c.getTitle();
					title = title.replace("&quot;", "");
					if(c.getContentPosterMappings()!=null){
						Set<ContentPosterMapping> cp = c.getContentPosterMappings();
						List<ContentPosterMapping> result = new ArrayList<>(cp);
						if(result!=null&&result.size()>0){
							ContentPosterMapping cpm =result.get(0);
							if(cpm!=null){
								Poster p = cpm.getPoster();
								if(p!=null){
									url = p.getUrl();
									int s = p.getSize()/1000;
									size = String.valueOf(s)+"kb";
									wh = String.valueOf(p.getWidth())+"x"+String.valueOf(p.getHeight());
								}
							}
						}
					}
				}
				dto.setUrl(url);
				dto.setTitle(title);
				dto.setSize(size);
				dto.setWh(wh);
				if (dtos == null) {
					dtos = new ArrayList<RecommonedDto>();
				}
				dtos.add(dto);
			}
		}
		return dtos;
	}
	/**
	 * 设置新推荐数据.
	 * @param oldIds 以前id
	 * @param newIds 新id列表
	 */
	@RequestMapping(value="/updateRecommoned",method=RequestMethod.POST )
	@ResponseBody
	public RestResponse updateRecommoned(@RequestBody RecommonedDto dtos) {
		RestResponse result = null;
		if (dtos == null) {
			log.info("参数传输错误,参数为空!");
			result = new RestResponse();
			result.setStatusCode(500);
			result.setMessage("参数传输错误,参数为空!");
			return result;
		}
		try {
			List<Long> contentIds = new ArrayList<>();
			for (RecommonedDto dto : dtos.getDtos()) {
				if (dto.getId() != null) {
					contentIds.add(dto.getId());
				}
				if (dto.getOldId() != null) {
					if (!dto.getId().equals(dto.getOldId())) {
						contentIds.add(dto.getOldId());
					}
				}
			}
			for (Long id : contentIds) {
				ClassificationContentMapping cn = cfmservice.get(id);
				Content c = cn.getContent();
				c.setUpdatedAt(new Date());
				contentServiceImpl.update(c);
			}
			cfmservice.updateRecommoned(dtos.getDtos());
			log.info("推荐数据更新成功!");
			result = new RestResponse();
			result.setStatusCode(200);
			result.setMessage("推荐数据更新成功!");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		result = new RestResponse();
		result.setStatusCode(500);
		result.setMessage("推荐数据更新失败!");
		return result;
	}
	
	/**
	 * 获取替换内容详细.
	 * @param id 
	 * 
	 */
	@RequestMapping(value="/get",method=RequestMethod.POST )
	@ResponseBody
	public RestResponse getClassifiMapping(Long id) {
		RestResponse response = null;
		ClassificationContentMapping ccm = cfmservice.get(id);
		RecommonedDto dto  = buildRecommed(ccm);
		response = new RestResponse();
		Map<String,Object>data = new HashMap<String,Object>();
		data.put("dto", dto);
		response.setStatusCode(200);
		response.setData(data);
		return response;
	}

	private RecommonedDto buildRecommed(ClassificationContentMapping ccm) {
		RecommonedDto dto = null;
		if (ccm != null) {
			dto = new RecommonedDto();
			dto.setId(ccm.getId());
			dto.setOldId(ccm.getId());
			dto.setRecommoned(ccm.getRecommend());
			String url = null;
			String title = null;
			String size = null;
			String wh = null;
			if (ccm.getContent() != null) {
				Content c = ccm.getContent();
				title = c.getTitle();
				title = title.replace("&quot;", "");
				if (c.getContentPosterMappings() != null) {
					Set<ContentPosterMapping> cp = c.getContentPosterMappings();
					List<ContentPosterMapping> result = new ArrayList<>(cp);
					if (result != null && result.size() > 0) {
						ContentPosterMapping cpm = result.get(0);
						if (cpm != null) {
							Poster p = cpm.getPoster();
							if (p != null) {
								url = p.getUrl();
								int s = p.getSize() / 1000;
								size = String.valueOf(s) + "kb";
								wh = String.valueOf(p.getWidth()) + "x"
										+ String.valueOf(p.getHeight());
							}
						}
					}
				}
			}
			dto.setUrl(url);
			dto.setTitle(title);
			dto.setSize(size);
			dto.setWh(wh);
		}
		return dto;
	}
	/**
	 * 删除推荐.
	 * 
	 */
	@RequestMapping(value="/removeRecommed",method=RequestMethod.POST )
	@ResponseBody
	public RestResponse removeRecommed(Long id) {
		RestResponse response = null;
		if (id == null) {
			log.info("参数为空!");
			response = new RestResponse();
			response.setStatusCode(500);
			response.setMessage("参数传输失败!");
			return response;
		}
		
		ClassificationContentMapping c = cfmservice.get(id);
		Content cc = c.getContent();
		cc.setUpdatedAt(new Date());
		contentServiceImpl.update(cc);
		c.setRecommend(0);
		cfmservice.save(c);
		response = new RestResponse();
		response.setStatusCode(200);
		response.setMessage("删除推荐成功!");
		return response;
	}
	/**
	 * 保存拖拽推荐.
	 */
	@RequestMapping(value="/updateDrap",method=RequestMethod.POST )
	@ResponseBody
	public RestResponse updateDrap(@RequestBody RecommonedDto dtos) {
		RestResponse result = null;
		if (dtos == null ) {
			log.info("参数传输错误,参数为空!");
			result = new RestResponse();
			result.setStatusCode(500);
			result.setMessage("参数传输错误,参数为空!");
			return result;
		}
		try {
			for (RecommonedDto dto : dtos.getDtos()) {
				if (dto.getId() != null) {
					ClassificationContentMapping cn = cfmservice.get(dto.getId());
					Content c = cn.getContent();
					c.setUpdatedAt(new Date());
					contentServiceImpl.update(c);
				}
			}
			cfmservice.updateDrapRecommoned(dtos.getDtos());
			log.info("推荐数据更新成功!");
			result = new RestResponse();
			result.setStatusCode(200);
			result.setMessage("推荐数据更新成功!");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		result = new RestResponse();
		result.setStatusCode(500);
		result.setMessage("推荐数据更新失败!");
		return result;
	}
}