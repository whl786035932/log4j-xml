package cn.videoworks.cms.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.entity.Classification;
import cn.videoworks.cms.entity.ClassificationOld;
import cn.videoworks.cms.entity.Content;
import cn.videoworks.cms.entity.Media;
import cn.videoworks.cms.entity.Poster;
import cn.videoworks.cms.entity.Task;
import cn.videoworks.cms.enumeration.ClassificationDele;
import cn.videoworks.cms.enumeration.ClassificationEdit;
import cn.videoworks.cms.service.ClassificationOldService;
import cn.videoworks.cms.service.ClassificationService;
import cn.videoworks.cms.service.ContentService;
import cn.videoworks.cms.service.MediaService;
import cn.videoworks.cms.service.PosterService;
import cn.videoworks.cms.service.TaskService;
import cn.videoworks.cms.util.ApiResponse;
import cn.videoworks.cms.util.DateUtil;

@Controller
@RequestMapping("/cms/adi/v1/merge")
public class MergeDataController {
	
	@Resource
	private ContentService contentServiceImpl;
	
	@Resource
	private PosterService posterServiceImpl;
	
	@Resource
	private TaskService taskServiceImpl;
	
	@Resource
	private MediaService mediaServiceImpl;
	
	@Resource
	private ClassificationOldService classificationOldServiceIml;
	
	@Resource
	private ClassificationService classificationServiceIml;
	
	private static final Logger log = LoggerFactory.getLogger(MergeDataController.class);
	
	/**
	 * mergeContent:(合并内容，把站点内容cnd同步状态不等于同步成功的数据合并到cms内容表中)
	 * @author   meishen
	 * @Date	 2018	2018年10月15日		下午1:51:31 
	 * @return String    
	 * @throws
	 */
	@RequestMapping(value = "/content",method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse mergeContent(@RequestBody List<Map<String,String>> contents) {
		ApiResponse res = new ApiResponse();
		log.info("合并内容表数据---开始");
		if(null != contents && contents.size() > 0) {
			for (int i = 0; i < contents.size(); i++) {
				Map<String,String> contentMap = contents.get(i);
				Long id = Long.valueOf(contentMap.get("id"));
				int cdnSyncStatus = Integer.valueOf(contentMap.get("cdn_sync_status"));
				Content content = contentServiceImpl.get(id);
				if(null != content) {
					content.setCdn_sync_status(cdnSyncStatus);
					contentServiceImpl.update(content);
					log.warn("内容【"+content.getTitle()+"】【"+id+"】状态【"+cdnSyncStatus+"】修改成功！");
				}else {
					log.warn("内容id【"+id+"】不存在！");
				}
			}
			res.setStatusCode(200);
			res.setMessage("内容数据合并成功");
		}else {
			res.setStatusCode(400);
			res.setMessage("参数不能为空");
		}
		log.info("合并内容表数据---结束");
		return res;
	}
	
	/**
	 * mergeMedia:(站点媒体表同步到cms媒体表中)
	 * @author   meishen
	 * @Date	 2018	2018年10月15日		下午2:25:12 
	 * @return ApiResponse    
	 * @throws
	 */
	@RequestMapping(value = "/media",method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse mergeMedia(@RequestBody List<Map<String,String>> medias) {
		ApiResponse res = new ApiResponse();
		log.info("合并媒体表数据---开始");
		if(null != medias && medias.size() > 0) {
			for (int i = 0; i < medias.size(); i++) {
				Map<String,String> mediaMap = medias.get(i);
				Long id = Long.valueOf(mediaMap.get("id"));
				int cdnSyncStatus = Integer.valueOf(mediaMap.get("cdn_sync_status"));
				String cdnKey = String.valueOf(mediaMap.get("cdn_key")); 
				Media media = mediaServiceImpl.get(id);
				if(null != media) {
					media.setCdn_sync_status(cdnSyncStatus);
					media.setCdnKey(cdnKey);
					mediaServiceImpl.update(media);
				}else {
					log.warn("媒体id【"+id+"】不存在！");
				}
			}
			res.setStatusCode(200);
			res.setMessage("媒体数据合并成功");
		}else {
			res.setStatusCode(400);
			res.setMessage("参数不能为空");
		}
		log.info("合并媒体表数据---结束");
		return res;
	}
	
	/**
	 * mergePoster:(同步海报数据)
	 * @author   meishen
	 * @Date	 2018	2018年10月15日		下午2:37:10 
	 * @return ApiResponse    
	 * @throws
	 */
	@RequestMapping(value = "/poster",method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse mergePoster(@RequestBody List<Map<String,String>> posters) {
		ApiResponse res = new ApiResponse();
		log.info("合并海报表数据---开始");
		if(null != posters && posters.size() > 0) {
			for (int i = 0; i < posters.size(); i++) {
				Map<String,String> posterMap = posters.get(i);
				Long id = Long.valueOf(posterMap.get("id"));
				int cdnSyncStatus = Integer.valueOf(posterMap.get("cdn_sync_status"));
				String url = String.valueOf(posterMap.get("url")); 
				Poster poster = posterServiceImpl.get(id);
				if(null != poster) {
					String currentUrl = poster.getUrl();
					poster.setCdnSyncStatus(cdnSyncStatus);
					poster.setSourceUrl(currentUrl);
					poster.setUrl(url);
					posterServiceImpl.update(poster);
				}else{
					log.warn("海报id【"+id+"】不存在！");
				}
			}
			res.setStatusCode(200);
			res.setMessage("海报数据合并成功");
		}else {
			res.setStatusCode(400);
			res.setMessage("参数不能为空");
		}
		log.info("合并海报表数据---结束");
		return res;
	}
	
	/**
	 * mergeTask:(同步站点任务为未成功的)
	 * @author   meishen
	 * @Date	 2018	2018年10月15日		下午2:44:12 
	 * @return ApiResponse    
	 * @throws
	 */
	@RequestMapping(value = "/task",method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse mergeTask(@RequestBody List<Map<String,String>> tasks) {
		ApiResponse res = new ApiResponse();
		log.info("合并任务表数据---开始");
		if(null != tasks && tasks.size() > 0) {
			for (int i = 0; i < tasks.size(); i++) {
				Map<String,String> taskMap = tasks.get(i);
				Long content_id = Long.valueOf(taskMap.get("content_id"));
				int status = Integer.valueOf(taskMap.get("status")); 
				String data = String.valueOf(taskMap.get("data")); 
				String msgid = String.valueOf(taskMap.get("msgid")); 
				String description = String.valueOf(taskMap.get("description")); 
				String inserted_at = String.valueOf(taskMap.get("inserted_at")); 
				String updated_at = String.valueOf(taskMap.get("updated_at")); 
				int type = Integer.valueOf(taskMap.get("type")); 
				String message = String.valueOf(taskMap.get("message")); 
				
				Task task = new Task();
				task.setContent(new Content(content_id));
				task.setData(data);
				task.setDescription(description);
				task.setInserted_at(DateUtil.getTimeStamp(inserted_at));
				task.setMessage(message);
				task.setMsgid(msgid);
				task.setStatus(status);
				task.setType(type);
				task.setUpdated_at(DateUtil.getTimeStamp(updated_at));
				taskServiceImpl.save(task);
			}
			res.setStatusCode(200);
			res.setMessage("任务数据合并成功");
		}else {
			res.setStatusCode(400);
			res.setMessage("参数不能为空");
		}
		log.info("合并任务表数据---结束");
		return res;
	}
	/**
	 * 分类左右值数据改为父子关系.
	 * 
	 */
	@RequestMapping(value = "/classification",method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse mergeClassification() {
		ApiResponse response = new ApiResponse();
		log.info("合并分类表数据---开始");
		try {
			//获取旧表值
			List<ClassificationOld> olds = classificationOldServiceIml.getAll();
			if (olds == null) {
				log.warn("旧数据失败!");
				response.setStatusCode(400);
				response.setMessage("分类数据合并失败");
				return response;
			}
			if (olds != null && olds.size() > 1) {
				change(olds);
			}
			// 查询跟结点
			if (olds != null && olds.size() > 0) {
				for (ClassificationOld c : olds) {
					if (c.getParent() == null) {
						c.setLevel(0);
						c.setSequence(1);
						changeLevel(c.getChildren(), 0);
						break;
					}
				}
			}
			List<Classification> news = replaceOld(olds);
			if(news!=null&&news.size()>0){
				classificationServiceIml.insert(news);
			}
			response.setStatusCode(200);
			response.setMessage("分类数据合并成功");
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setStatusCode(400);
			response.setMessage("分类数据合并失败");
			return response;
		}
		log.info("合并分类表数据---结束");
		// 插入新数据
		return response;
	}
	/**
	 * 左右值改为上下级关系.
	 * 
	 */
	private List<ClassificationOld> change(List<ClassificationOld> all){
		for (int i = 1; i < all.size(); i++) {
			long lft = all.get(i).getLft();
			for (int j = i - 1; j >= 0; j--) {
				long lftn = all.get(j).getLft();
				long rgtn = all.get(j).getRgt();
				if (lft > lftn && lft < rgtn) {
					if(all.get(j).getChildren()==null){
						all.get(j).setChildren(new ArrayList<ClassificationOld>());
					}
					all.get(j).getChildren().add(all.get(i));
					all.get(i).setParent(all.get(j).getId());// 设置父id
					break;
				}
			}
		}
		return all;
	}

	/**
	 * 设置level squence
	 */
	private void changeLevel(List<ClassificationOld> list, Integer level) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setSequence(i + 1);
				list.get(i).setLevel(level + 1);
				if (list.get(i).getChildren() != null && list.get(i).getChildren().size() > 0) {
					for(int j = 0; j < list.get(i).getChildren().size(); j++){
						changeLevel(list.get(i).getChildren(), level + 1);
					}
				}
		}}
	}
	/**
	 * 新旧数据转换.
	 * 
	 */
	private List<Classification> replaceOld(List<ClassificationOld> olds) {
		List<Classification> news = new ArrayList<>();
		if(olds!=null&&olds.size()>0){
			for (ClassificationOld c : olds) {
				Classification n = new Classification();
				n.setId(c.getId());// id
				n.setName(c.getName());// name
				n.setIcon(c.getIcon());// icon
				n.setDescription(c.getDescription());// descriotion
				n.setStatus(c.getStatus());// status
				n.setInserted_at(c.getInserted_at()); // inserted
				n.setUpdated_at(c.getUpdated_at());// update
				n.setType(c.getType());// type
				if (c.getParent() != null && !c.getParent().equals("")) {
					n.setParent(c.getParent());// parent
				} else {
					n.setParent("0");
				}
				n.setEditable(ClassificationEdit.STARTUSE);// 编辑状态
				n.setDeletable(ClassificationDele.STARTUSE);// 删除状态
				n.setLevel(c.getLevel());// level
				n.setSequence(c.getSequence());
				news.add(n);
			}
		}
		return news;
	}
}
