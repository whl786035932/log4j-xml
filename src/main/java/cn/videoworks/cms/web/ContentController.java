/**
 * ContentController.java
 * cn.videoworks.cms.web
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月25日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
 */

package cn.videoworks.cms.web;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.client.CDNGearmanClient;
import cn.videoworks.cms.constant.GearmanFunctionConstant;
import cn.videoworks.cms.dto.AdiImageDto;
import cn.videoworks.cms.dto.CDNJobDto;
import cn.videoworks.cms.dto.ContentDto;
import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.dto.LogDto;
import cn.videoworks.cms.dto.OperationConvertDto;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.dto.PosterDto;
import cn.videoworks.cms.entity.Classification;
import cn.videoworks.cms.entity.ClassificationContentMapping;
import cn.videoworks.cms.entity.Content;
import cn.videoworks.cms.entity.ContentPosterMapping;
import cn.videoworks.cms.entity.Media;
import cn.videoworks.cms.entity.Poster;
import cn.videoworks.cms.entity.Source;
import cn.videoworks.cms.entity.Task;
import cn.videoworks.cms.enumeration.BusinessType;
import cn.videoworks.cms.enumeration.CdnStatus;
import cn.videoworks.cms.enumeration.CdnType;
import cn.videoworks.cms.enumeration.ContentStatus;
import cn.videoworks.cms.enumeration.LogLevel;
import cn.videoworks.cms.enumeration.LogSource;
import cn.videoworks.cms.enumeration.LogType;
import cn.videoworks.cms.enumeration.Recommend;
import cn.videoworks.cms.enumeration.TaskStatus;
import cn.videoworks.cms.service.ClassificationContentMappingService;
import cn.videoworks.cms.service.ClassificationService;
import cn.videoworks.cms.service.ContentPosterMappingService;
import cn.videoworks.cms.service.ContentService;
import cn.videoworks.cms.service.MediaService;
import cn.videoworks.cms.service.PosterService;
import cn.videoworks.cms.service.SourceService;
import cn.videoworks.cms.service.TaskService;
import cn.videoworks.cms.util.ApiResponse;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.cms.util.UserUtil;
import cn.videoworks.cms.vo.ContentClassificationVo;
import cn.videoworks.cms.vo.UserVo;
import cn.videoworks.commons.util.json.JsonConverter;
import cn.videoworks.commons.webdev.constant.ResponseStatusCode;

/**
 * ClassName:ContentController Function: TODO ADD FUNCTION Reason: TODO ADD
 * REASON
 * 
 * @author meishen
 * @version
 * @since Ver 1.1
 * @Date 2018年5月25日 上午10:47:03
 * 
 * @see
 */
@Controller
@RequestMapping("contents")
public class ContentController {

	@Resource
	private ContentService contentServiceImpl;

	@Resource
	private SourceService sourceServiceImpl;

	@Resource
	private PosterService posterServiceImpl;

	@Resource
	private ClassificationService classificationServiceImpl;

	@Resource
	private ClassificationContentMappingService classificationContentMappingServiceImpl;

	@Resource
	private ContentPosterMappingService contentPosterMappingServiceImpl;
	
	@Resource
	private TaskService taskService;
	
	@Resource
	private MediaService mediaServiceImpl;
	
	private static final Logger log = LoggerFactory.getLogger(ContentController.class);
	
	@Resource
	private Properties databaseConfig;

	/**
	 * index:(列表页面)
	 * 
	 * @author meishen
	 * @Date 2018 2018年5月30日 下午6:16:52
	 * @param model
	 * @return
	 * @return String
	 * @throws
	 * @since Videoworks　Ver 1.1
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(ModelMap model,HttpServletRequest request) {
		List<Source> list = sourceServiceImpl.list("insertedAt");
		model.put("sources", list);
		HttpSession session = request.getSession();
		UserVo userVo = (UserVo) session.getAttribute("user");
		userVo.getOperations();
		return "site.cms.content.index";
	}

	/**
	 * ajax:(ajax获取数据)
	 * 
	 * @author meishen
	 * @Date 2018 2018年5月30日 下午6:17:07
	 * @param data
	 * @return
	 * @return Map<String,Object>
	 * @throws
	 * @since Videoworks　Ver 1.1
	 */
	@RequestMapping("/ajax")
	@ResponseBody
	public Map<String, Object> ajax(String data) {
		Map<String, Object> rr = new HashMap<String, Object>();
		Map<String, Object> q = ConvertDto.convertDataTableSearchForContent(data);
		Page page = ConvertDto.convertPage(data);
		List<ContentDto> dtos = new ArrayList<ContentDto>();
		String order = q.containsKey("order") == true ? q.get("order").toString() : "";
		String sort = q.containsKey("sort") == true ? q.get("sort").toString(): "";
		List<Content> contents = contentServiceImpl.list(q, order, sort, page);
		for (Content content : contents) {
			List<ContentClassificationVo> classifs = new ArrayList<ContentClassificationVo>();
			ContentDto dto = ConvertDto.convertContent(content);
			if(content.getClassificationContentMappingMappings() != null) {
				for(ClassificationContentMapping ccm : content.getClassificationContentMappingMappings()) {
					String cId = ccm.getClassification() != null?ccm.getClassification().getId():"";
					// 获取所有父分类
					List<Classification> cfs = classificationServiceImpl.getClassificationF(cId);
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
			dto.setClassifs(classifs);
			dtos.add(dto);
		}
		page = contentServiceImpl.paging(q, page);

		buildRR(rr, dtos, page);
		return rr;
	}

	/**
	 * edit:(获取内容)
	 * 
	 * @author meishen
	 * @Date 2018 2018年5月30日 下午6:21:40
	 * @param id
	 * @param map
	 * @return
	 * @return String
	 * @throws
	 * @since Videoworks　Ver 1.1
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String get(@PathVariable Long id, ModelMap map) {
		Content content = contentServiceImpl.get(id);

		List<ClassificationContentMapping> ccms = classificationContentMappingServiceImpl
				.list(id);
		List<Map<String, Object>> classifs = new ArrayList<Map<String, Object>>();
		for (ClassificationContentMapping ccm : ccms) {
			// 获取所有父分类
			List<Classification> cfs = classificationServiceImpl.getClassificationF(ccm.getClassification().getId());
			String hierarchyName = "";
			if (cfs != null) {
				for (int i = cfs.size()-1; i >=0; i--) {
					if (i == cfs.size() - 1)
						hierarchyName =hierarchyName + cfs.get(i).getName();
					else
						hierarchyName =hierarchyName +"/"+ cfs.get(i).getName();
				}
				// 组装内容详情数据
				classifs.add(ConvertDto.convertClassification(
						ccm.getClassification(), hierarchyName));
			}
		}
		ContentDto contentDto = ConvertDto.convertContentN(content);
		contentDto.setClassifications(classifs);
		List<PosterDto> images = contentDto.getImagesN();
		List<Long> orignalPosters=new ArrayList<Long>();
		List<AdiImageDto> emptyImag = null;
		if (images != null) {
			if (images.size() < 5) {
				for (int i = images.size(); i < 5; i++) {
					if (emptyImag == null) {
						emptyImag = new ArrayList<>();
					}
					emptyImag.add(new AdiImageDto());
				}
			}
		} else {
			emptyImag = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				emptyImag.add(new AdiImageDto());
			}
		}
		for (PosterDto posterDto : images) {
			orignalPosters.add(posterDto.getId());
		}
		map.put("content", contentDto);
		map.put("emptyImg", emptyImag);
		map.put("orignalPosters", orignalPosters);
		return "site.cms.content.edit";
	}

	/**
	 * update:(修改)
	 * 
	 * @author meishen
	 * @Date 2018 2018年5月30日 下午6:24:26
	 * @param dto
	 * @return
	 * @return String
	 * @throws
	 * @since Videoworks　Ver 1.1
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update( ContentDto dto,HttpServletRequest request) {
		//记录添加站点-操作日志
		Map<String,Object> detail = new HashMap<String,Object>();
		detail.put("param", JsonConverter.format(dto));
		LogDto logDto = ConvertDto.buildOperationLog("CMS-修改内容", BusinessType.UPDATE.getValue(), "内容管理", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), detail);
		log.info(JsonConverter.format(logDto));
		dto = replace(dto);
		Content content = contentServiceImpl.get(dto.getId());
		
		ConvertDto.convertContent(content, dto);
		contentPosterMappingServiceImpl.deleteByCid(dto.getId());// 删除已有关联
		if (dto.getImgId() != null) {
			for (Long pid : dto.getImgId()) {
				if (pid != null) {
					final ContentPosterMapping contentPosterMapping = new ContentPosterMapping();
					Poster po = posterServiceImpl.get(pid);
					contentPosterMapping.setContent(content);
					contentPosterMapping.setPoster(po);
					contentPosterMapping.setPosition(1);
					contentPosterMappingServiceImpl.save(contentPosterMapping);
					List<Long> origanlPosters = dto.getOriganlPosters();
					boolean containsImages = containsImages(origanlPosters,po);
					if(!containsImages) {
						final Long contentId = dto.getId();
						Runnable runnable = new Runnable() {
							@Override
							public void run() {
								Set<ContentPosterMapping> unCdnPosterMappings = new HashSet<>();
								unCdnPosterMappings.add(contentPosterMapping);
								final Map<String, Object> addCdnTaskMap = addCdnTaskByContent(contentId, null, unCdnPosterMappings,
										0,false);
								ApiResponse submitCDNJobResponse = submitCDNJob(addCdnTaskMap);
							}
						};
						ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(1);
						newFixedThreadPool.execute(runnable);
					}
					
					
				}
			}
		}
		contentServiceImpl.update(content);
		
		
		
		return ConvertDto.buildRestResponse(ResponseStatusCode.OK, "修改成功", null);
	}
	
	
	public boolean containsImages(List<Long> orignalPosters, Poster po) {
		if(orignalPosters!=null) {
			for (Long posterId : orignalPosters) {
				Poster poster = posterServiceImpl.get(posterId);
				if(posterId.longValue()==po.getId().longValue()) {
					if(poster.getUrl()!=null) {
						return true;
					}
					
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 保存cdn任务
	 * 
	 * @param content_id
	 *            内容的Id
	 * @param cdn_param_id
	 *            媒体的id, 海报的id
	 */

	public Map<String, Object> addCdnTaskByContent(Long contentId, List<Media> uncdnMedias,
			Set<ContentPosterMapping> uncdnPosters, Integer isUploadCdn,boolean firstzhru) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		Content content = contentServiceImpl.get(contentId);
		Task task = new Task();
		if (content != null) {
			content.setMedias(uncdnMedias);
			content.setContentPosterMappings(uncdnPosters);
			task.setContent(content);
			task.setType(CdnType.CDNSTORAGE.getValue());
			task.setStatus(TaskStatus.ONGOING.getValue());
			task.setDescription("内容contentId=" + contentId + "进行CDN入库");
			Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
			task.setInserted_at(nowTimeStamp);
			task.setUpdated_at(nowTimeStamp);

			// cdn 任务设置任务参数-----------------------todo------------------------

			CDNJobDto cdnJobDto = ConvertDto.convertContentEntityToCDNJobDto(content);
			cdnJobDto.setIsUploadCdn(isUploadCdn);

			String cdnData = JsonConverter.format(cdnJobDto);
			System.out.println("发送的cdnDatea=" + cdnData);
			task.setData(cdnData);
			if(firstzhru) {
				task.setMessage("首次注入");
			}else {
				task.setMessage("修改海报注入");
			}
			
			// 调用cdn的入库接口 --------------------------todo------------------------
			taskService.save(task);

			if (isUploadCdn.intValue() == 1) {
				content.setCdn_sync_status(CdnStatus.ONGOING.getValue());
			}
			contentServiceImpl.update(content);
			cdnJobDto.setTaskId(task.getId());
			cdnData = JsonConverter.format(cdnJobDto);

			hashMap.put("data", cdnData);
			hashMap.put("taskId", task.getId());
			hashMap.put("contentId", contentId);
		}
		return hashMap;
	}
	
	
	/**
	 * 向gearman提交任务
	 * 
	 * @param cdnData
	 * @return
	 */
	public ApiResponse submitCDNJob(Map<String, Object> cdnDataMap) {
		ApiResponse result = new ApiResponse();
		String user = databaseConfig.containsKey("user.name") == true ? databaseConfig.getProperty("user.name") : "未知";
		Long taskId = (Long) cdnDataMap.get("taskId");
		String cdnData = (String) cdnDataMap.get("data");
		Long contentId = (Long) cdnDataMap.get("contentId");
		String gearamHost = databaseConfig.getProperty("gearman.ip");
		String gearmanPortStr = databaseConfig.getProperty("gearman.port");
		CDNGearmanClient cdnGearmanClient =  CDNGearmanClient.getCDNGearmanClient(gearamHost, Integer.valueOf(gearmanPortStr),
				GearmanFunctionConstant.WRITE_CDN, contentServiceImpl, posterServiceImpl, contentPosterMappingServiceImpl, mediaServiceImpl,
				taskService, databaseConfig);

		// 异步任务begin

		// log.info("提交的CDN的任务的data=" + cdnData);
		HashMap<String, Object> detail = new HashMap<String, Object>();
		detail.put("param", JsonConverter.format(cdnData));
		LogDto writeCmsLogDto = OperationConvertDto.buildSystemLog("BOOTHS同步CDN功能-同步到CDN的数据",
				BusinessType.WRITECDN.getValue(), "BOOTHS同步CDN功能-同步到CDN的数据", contentId + "", user,
				LogType.SYSTEMLOG.getValue(), LogSource.BOOTHS.getValue(), LogLevel.DEBUG.getValue(), detail);
		log.info(JsonConverter.format(writeCmsLogDto));

		// cdn的返回结果
		result = cdnGearmanClient.submitJob(cdnData);
		// 异步任务end

		return result;
	}
	

	/**
	 * 替换字段双引号.
	 */
	private ContentDto replace(ContentDto dto) {
		dto.setTitle(dto.getTitle().replace("\"", "&quot;"));
		dto.setTitleAbbr(dto.getTitleAbbr().replace("\"", "&quot;"));
		dto.setDescription(dto.getDescription().replace("\"", "&quot;"));
		return dto;
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
		LogDto logDto = ConvertDto.buildOperationLog("CMS-修改内容", bisinessBype, "内容管理", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), new HashMap<String,Object>());
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
		LogDto logDto = ConvertDto.buildOperationLog("CMS-删除内容", BusinessType.DELETE.getValue(), "内容管理", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), detail);
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
	 * addClassification:(添加多分类对应对内容)
	 * 
	 * @author meishen
	 * @Date 2018 2018年5月31日 下午7:24:29
	 * @param cfids
	 * @param cids
	 * @return
	 * @return Map<String,Object>
	 * @throws
	 * @since Videoworks　Ver 1.1
	 */
	@RequestMapping("/addClassification")
	@ResponseBody
	public Map<String, Object> addClassification(
			@RequestParam List<String> cfIds, @RequestParam List<Long> cIds,HttpServletRequest request) {
		try {
			//记录添加站点-操作日志
			LogDto logDto = ConvertDto.buildOperationLog("CMS-修改内容", BusinessType.ADDCLASSIFICATION.getValue(), "内容管理", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), new HashMap<String,Object>());
			log.info(JsonConverter.format(logDto));
			
			buildClassificationContentMapping(cfIds, cIds,
					classificationContentMappingServiceImpl, contentServiceImpl);
		} catch (Exception e) {
			e.printStackTrace();
			return ConvertDto.buildRestResponse(
					ResponseStatusCode.INTERNAL_SERVER_ERROR,
					"失败【" + e.getMessage() + "】", null);
		}
		return ConvertDto.buildRestResponse(ResponseStatusCode.OK, "成功", null);
	}

	/**
	 * buildClassificationContentMapping:(挂载分类和内容的关系，CMS注入接口有用到此方法)
	 * 
	 * @author meishen
	 * @Date 2018 2018年7月19日 上午10:20:04
	 * @param cfIds
	 * @param cIds
	 * @throws Exception
	 * @return void
	 * @throws
	 * @since Videoworks　Ver 1.1
	 */
	public void buildClassificationContentMapping(
			List<String> cfIds,
			List<Long> cIds,
			ClassificationContentMappingService classificationContentMappingServiceImpl,
			ContentService contentServiceImpl) throws Exception {
		if (null != cfIds && cfIds.size() > 0) {
			for (String cfId : cfIds) {
				for (Long cId : cIds) {
					List<ClassificationContentMapping> ccms = classificationContentMappingServiceImpl
							.list(cfId, cId);
					if (null != ccms && ccms.size() <= 0) {
						ClassificationContentMapping ccm = new ClassificationContentMapping();
						ccm.setClassification(new Classification(cfId));
						ccm.setContent(new Content(cId));
						ccm.setRecommend(Recommend.UNRECOMMEND.getValue());
						ccm.setSequence(Integer.valueOf(cId.toString()));// 设置排序功能，兼容站点按照分类查询内容分页使用，接口用到了sequence
						classificationContentMappingServiceImpl.save(ccm);
					}
				}
			}
		}
	}

	/**
	 * buildRR:(构建datatables数据)
	 * 
	 * @author meishen
	 * @Date 2018 2018年5月30日 下午6:17:24
	 * @param rr
	 * @param contentsDto
	 * @param page
	 * @return void
	 * @throws
	 * @since Videoworks　Ver 1.1
	 */
	private void buildRR(Map<String, Object> rr, List<ContentDto> contentsDto,
			Page page) {
		rr.put("data", contentsDto);
		rr.put("iTotalRecords", contentsDto.size());
		rr.put("iTotalDisplayRecords", page.getRecordCount());
	}

	/**
	 * 获取来源信息.
	 */
	@RequestMapping("/getSource")
	@ResponseBody
	public RestResponse getSource() {
		RestResponse response = new RestResponse();
		List<Source> list = sourceServiceImpl.list("insertedAt");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		response.setStatusCode(200);
		response.setData(result);
		return response;
	}

	/**
	 * 获取海报.
	 * 
	 * @param index
	 *            页码
	 * @param source
	 *            来源信息
	 */
	@RequestMapping(value = "getPoster", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> ajax(Integer index, String source) {
		Page page = new Page();
		if (null != index)
			page.setOffSet(index * 20);
		Map<String, String> q = new HashMap<String, String>();
		q.put("source", source);
		List<Poster> posters = posterServiceImpl.list(q, "insertedAt", page);
		Page pp = posterServiceImpl.paging(q, page);
		pp.setIndex(index + 1);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("page", pp);
		result.put("posters", posters);
		return ConvertDto
				.buildRestResponse(ResponseStatusCode.OK, "成功", result);
	}

	/**
	 * 导出数据到Excel.
	 */
	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	public void exportExcel(HttpServletResponse response, String data) {
		Map<String, Object> q = ConvertDto.convertDataTableSearchForContent(data);
		List<ContentDto> dtos = new ArrayList<ContentDto>();
		String order = q.containsKey("order") == true ? q.get("order").toString() : "";
		String sort = q.containsKey("sort") == true ? q.get("sort").toString(): "";
		List<Content> contents = contentServiceImpl.list(q, "insertedAt", "desc", null);
		for (Content content : contents) {
			List<ContentClassificationVo> classifs = new ArrayList<ContentClassificationVo>();
			ContentDto dto = ConvertDto.convertContent(content);
			if (content.getClassificationContentMappingMappings() != null) {
				for (ClassificationContentMapping ccm : content
						.getClassificationContentMappingMappings()) {
					String cId = ccm.getClassification() != null ? ccm.getClassification().getId() : "";
					// 获取所有父分类
					List<Classification> cfs = classificationServiceImpl.getClassificationF(cId);
					String hierarchyName = "";
					if (cfs != null) {
						for (int i = cfs.size() - 1; i >= 0; i--) {
							if (i == cfs.size() - 1)
								hierarchyName = hierarchyName + cfs.get(i).getName();
							else
								hierarchyName = hierarchyName + "/" + cfs.get(i).getName();
						}
						ContentClassificationVo classificationVo = new ContentClassificationVo();
						classificationVo.setClassificationId(cId);
						classificationVo.setClassificationName(hierarchyName);
						// 组装内容详情数据
						classifs.add(classificationVo);
					}
				}
			}
			dto.setClassifs(classifs);
			dtos.add(dto);
		}
		try {
			createExcel(dtos,response);
			return;
		} catch (Exception e) {
			log.error("导出Excel异常", e);
		}
		
	}
	
	public void createExcel(List<ContentDto> dtos,HttpServletResponse response) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("内容统计列表");
		sheet.setColumnWidth(0, 14336);
		sheet.setColumnWidth(1, 5120);
		sheet.setColumnWidth(2, 5120);
		sheet.setColumnWidth(3, 5120);
		sheet.setColumnWidth(4, 8000);
		sheet.setColumnWidth(5, 5120);
		sheet.setColumnWidth(6, 2560);
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell cell = row.createCell(0);
		String [] titles = {"标题","发布时间","入站时间","来源栏目","分类","来源频道","时长"};
		for (int i = 0; i < titles.length; i++) {
			cell.setCellValue(titles[i]);
			cell.setCellStyle(style);
			cell = row.createCell(i+1);
		}
		if (dtos != null && dtos.size() != 0) {
			for (int i = 0; i < dtos.size(); i++) {
				row = sheet.createRow(i + 1);
				ContentDto dto = dtos.get(i);
				row.createCell(0).setCellValue(dto.getTitle());
				row.createCell(1).setCellValue(dto.getPublishTime());
				row.createCell(2).setCellValue(dto.getInsertedAt());
				row.createCell(3).setCellValue(dto.getSourceColumn());
				String classi = "";
				if (dto.getClassifs() != null && dto.getClassifs().size() > 0) {
					for (int j = 0; j < dto.getClassifs().size(); j++) {
						ContentClassificationVo c = dto.getClassifs().get(j);
						classi += c.getClassificationName();
						if (j < dto.getClassifs().size() - 1) {
							classi += "\r\n";
						}
					}
				}
				//row.createCell(4).setCellValue(new HSSFRichTextString(classi));// 多个
				HSSFCell createCell = row.createCell(4);
				HSSFCellStyle cellStyle = createCell.getCellStyle();
				cellStyle.setWrapText(true);
				createCell.setCellStyle(cellStyle);
				createCell.setCellValue(new HSSFRichTextString(classi));
 				row.createCell(5).setCellValue(dto.getSourceChannel());
				int seconds = Integer.valueOf(dto.getDuration()/1000);
				int temp=0;
				StringBuffer sb=new StringBuffer();
				temp = seconds/3600;
				sb.append((temp<10)?"0"+temp+":":""+temp+":");

				temp=seconds%3600/60;
				sb.append((temp<10)?"0"+temp+":":""+temp+":");

				temp=seconds%3600%60;
				sb.append((temp<10)?"0"+temp:""+temp);
				row.createCell(6).setCellValue(sb.toString());
			}
		}
		try {
			this.setResponseHeader(response);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error("数据写入Excel表格异常", e);
		}
	}
	
	  //发送响应流方法
    public void setResponseHeader(HttpServletResponse response) {
        try {
        	String fileName = new String(("content-export("+ DateUtil.format.format(new Date()) + ")").getBytes(), "UTF-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
