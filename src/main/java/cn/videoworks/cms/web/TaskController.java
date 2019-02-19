package cn.videoworks.cms.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.client.CDNGearmanClient;
import cn.videoworks.cms.constant.GearmanFunctionConstant;
import cn.videoworks.cms.constant.ResponseDictionary;
import cn.videoworks.cms.dto.CDNJobDto;
import cn.videoworks.cms.dto.CDNReturnDto;
import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.dto.LogDto;
import cn.videoworks.cms.dto.OperationConvertDto;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.dto.TaskDto;
import cn.videoworks.cms.entity.Content;
import cn.videoworks.cms.entity.Media;
import cn.videoworks.cms.entity.Poster;
import cn.videoworks.cms.entity.Task;
import cn.videoworks.cms.enumeration.BusinessType;
import cn.videoworks.cms.enumeration.CdnReturnType;
import cn.videoworks.cms.enumeration.CdnStatus;
import cn.videoworks.cms.enumeration.LogLevel;
import cn.videoworks.cms.enumeration.LogSource;
import cn.videoworks.cms.enumeration.LogType;
import cn.videoworks.cms.enumeration.TaskStatus;
import cn.videoworks.cms.service.ContentPosterMappingService;
import cn.videoworks.cms.service.ContentService;
import cn.videoworks.cms.service.MediaService;
import cn.videoworks.cms.service.PosterService;
import cn.videoworks.cms.service.TaskService;
import cn.videoworks.cms.util.ApiResponse;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.commons.util.json.JsonConverter;
import cn.videoworks.commons.webdev.constant.ResponseStatusCode;

/**
 * 任务(注入管理) TaskController
 * 
 * @author Pei
 * 
 */
@Controller
@RequestMapping("tasks")
public class TaskController {
	private static final Logger log = LoggerFactory.getLogger(TaskController.class);
	@Resource
	private TaskService service;

	@Resource
	private MediaService mediaService;

	@Resource
	private PosterService posterService;

	@Resource
	private Properties databaseConfig;

	@Resource
	private TaskService taskService;

	@Resource
	private ContentService contentService;
	
	@Resource
	private    ContentPosterMappingService contentPosterMappingService;

	/**
	 * 列表页面跳转.
	 */
	@RequestMapping(value = "list")
	public String view() {
		return "site.cms.task.index";
	}

	@RequestMapping("/ajax")
	@ResponseBody
	public Map<String, Object> ajax(String data) {
		Map<String, Object> rr = new HashMap<String, Object>();
		Map<String, Object> q = ConvertDto.convertDataTableSearchForTask(data);
		Page page = ConvertDto.convertPage(data);
		List<TaskDto> dtos = new ArrayList<TaskDto>();

		List<Task> tasks = service.list(q, "inserted_at", page);
		for (Task task : tasks) {
			dtos.add(ConvertDto.convertTask(task));
		}
		page = service.paging(q, page);

		buildRR(rr, dtos, page);
		return rr;
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
	 * @throws @since
	 *             Videoworks Ver 1.1
	 */
	private void buildRR(Map<String, Object> rr, List<TaskDto> taskDtos, Page page) {
		rr.put("data", taskDtos);
		rr.put("iTotalRecords", taskDtos.size());
		rr.put("iTotalDisplayRecords", page.getRecordCount());
	}

	/**
	 * 重试CDN
	 * 
	 * @return
	 */
	@RequestMapping(value = "/reCdnStore/{taskId}")
	@ResponseBody
	public RestResponse recdnStore(@PathVariable(value = "taskId") Long taskId) {
		Task task = service.get(taskId);
		if (task != null) {
			task.setStatus(TaskStatus.ONGOING.getValue());
			service.update(task);
			Content content = task.getContent();
			if (content != null) {

				content.setCdn_sync_status(CdnStatus.ONGOING.getValue());
				contentService.update(content);
			}
			String data = task.getData();
			CDNJobDto parse = JsonConverter.parse(data, CDNJobDto.class);
			parse.setTaskId(taskId);
			data=JsonConverter.format(parse);
			ApiResponse submitCDNJob = submitCDNJob(data, task.getId());
			if (submitCDNJob.getStatusCode() != ResponseDictionary.SUCCESS) {
				return buildResponse(ResponseStatusCode.INTERNAL_SERVER_ERROR, submitCDNJob.getMessage());
			} else {
				return buildResponse(ResponseStatusCode.OK, "重试成功");
			}
		}
		return buildResponse(ResponseStatusCode.NOT_FOUND, "任务不存在");
	}

	public RestResponse buildResponse(Integer statusCode, String message) {
		RestResponse restResponse = new RestResponse();
		restResponse.setStatusCode(statusCode);
		restResponse.setMessage(message);
		return restResponse;
	}

	/**
	 * 修改海报和媒体的cdn状态
	 * 
	 * @param cdnDatas
	 */
	public void updateMediaAndPosterCDN(List<CDNReturnDto> cdnDatas) {
		Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
		for (CDNReturnDto cdnReturnDto : cdnDatas) {
			Integer type = cdnReturnDto.getType();
			Long id = cdnReturnDto.getId();
			String cdn_key = cdnReturnDto.getCdn_key();
			if (type == CdnReturnType.MEDIA.getValue()) {
				Media media = mediaService.get(id);
				media.setCdn_sync_status(CdnStatus.SYNCHRONIZED.getValue());
				media.setCdnKey(cdn_key);
				media.setUpdatedAt(nowTimeStamp);
				mediaService.update(media);
			} else if (type == CdnReturnType.POSTER.getValue()) {
				Poster poster = posterService.get(id);
				poster.setUrl(cdn_key);
				poster.setUpdatedAt(nowTimeStamp);
				poster.setCdnSyncStatus(CdnStatus.SYNCHRONIZED.getValue());
				posterService.update(poster);
			}
		}
	}

	/**
	 * 向gearman提交任务
	 * 
	 * @param cdnData
	 * @return
	 */
	public ApiResponse submitCDNJob(String cdnData, Long taskId) {
		ApiResponse result = new ApiResponse();
		String gearamHost = databaseConfig.getProperty("gearman.ip");
		String gearmanPortStr = databaseConfig.getProperty("gearman.port");
		CDNGearmanClient cdnGearmanClient =  CDNGearmanClient.getCDNGearmanClient(gearamHost, Integer.valueOf(gearmanPortStr),
				GearmanFunctionConstant.WRITE_CDN,contentService,posterService,contentPosterMappingService,mediaService,taskService,databaseConfig);
		try {
			// 记录操作日志
			String user = databaseConfig.containsKey("user.name") == true ? databaseConfig.getProperty("user.name")
					: "未知";
			HashMap<String, Object> detail = new HashMap<String, Object>();
			detail.put("param", JsonConverter.format(cdnData));
			LogDto writecdnLogDto = OperationConvertDto.buildSystemLog("BOOTHS重试CDN功能-重试CDN功能",
					BusinessType.WRITECDN.getValue(), "BOOTHS重试CDN功能-重试CDN功能", taskId + "", user,
					LogType.OPERATIONLOG.getValue(), LogSource.BOOTHS.getValue(), LogLevel.INFO.getValue(), detail);
			log.info(JsonConverter.format(writecdnLogDto));

			result = cdnGearmanClient.submitJob(cdnData);
		} catch (Exception e) {
			result.setStatusCode(ResponseDictionary.SERVEREXCEPTION);
			result.setMessage("注入cdn失败:" + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 修改任务的状态 TO---------------DO
	 * 
	 * @return
	 */
	public void updateTaskStatus(Long taskId, Integer status) {
		Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
		Task task = taskService.get(taskId);
		task.setStatus(status);
		task.setUpdated_at(nowTimeStamp);
		taskService.update(task);
	}

	/**
	 * 修改内容的cdn状态
	 * 
	 * @param contentId
	 * @param cdnStatus
	 */
	public void updateContentCdnStatus(Long contentId, Integer cdnStatus) {
		Content content = contentService.get(contentId);
		content.setCdn_sync_status(cdnStatus);
		Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
		content.setUpdatedAt(nowTimeStamp);
		contentService.update(content);
	}

	/**
	 * delete:(批量任务删除)
	 * 
	 * @author meishen
	 * @Date 2018 2018年5月31日 下午6:43:02
	 * @param id
	 * @return
	 * @return Map<String,Object>
	 * @throws @since
	 *             Videoworks Ver 1.1
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam List<Long> ids) {
		// 记录添加站点-操作日志
		String user = databaseConfig.containsKey("user.name") == true ? databaseConfig.getProperty("user.name") : "未知";
		Map<String, Object> detail = new HashMap<String, Object>();
		detail.put("param", JsonConverter.format(ids));
		LogDto logDto = OperationConvertDto.buildOperationLog("BOOTHS-批量删除CDN任务", BusinessType.DELETE.getValue(),
				"入站管理", user, LogType.OPERATIONLOG.getValue(), LogSource.BOOTHS.getValue(), LogLevel.INFO.getValue(),
				detail);
		log.info(JsonConverter.format(logDto));

		if (null != ids && ids.size() > 0) {
			for (Long id : ids) {
				taskService.delete(id);
			}
		}
		return buildResponseMap(ResponseStatusCode.OK, "成功", null);
	}

	/**
	 * delete:(批量重试CDN)
	 * 
	 * @author meishen
	 * @Date 2018 2018年5月31日 下午6:43:02
	 * @param id
	 * @return
	 * @return Map<String,Object>
	 * @throws @since
	 *             Videoworks Ver 1.1
	 */
	@RequestMapping("/batchRetryCdn")
	@ResponseBody
	public Map<String, Object> retryCdn(@RequestParam List<Long> ids) {
		// 记录添加站点-操作日志
		String user = databaseConfig.containsKey("user.name") == true ? databaseConfig.getProperty("user.name") : "未知";
		Map<String, Object> detail = new HashMap<String, Object>();
		detail.put("param", JsonConverter.format(ids));
		LogDto logDto = OperationConvertDto.buildOperationLog("BOOTHS-批量重试CDN任务", BusinessType.DELETE.getValue(),
				"入站管理", user, LogType.OPERATIONLOG.getValue(), LogSource.BOOTHS.getValue(), LogLevel.INFO.getValue(),
				detail);
		log.info(JsonConverter.format(logDto));
		ApiResponse errorResponse = null;
		if (null != ids && ids.size() > 0) {
			for (Long taskId : ids) {
				Task task = service.get(taskId);
				task.setStatus(TaskStatus.ONGOING.getValue());
				taskService.update(task);
				Content content = task.getContent();
				if (content != null) {
					content.setCdn_sync_status(CdnStatus.ONGOING.getValue());
					contentService.update(content);
				}
				String data = task.getData();
				CDNJobDto parse = JsonConverter.parse(data, CDNJobDto.class);
				parse.setTaskId(taskId);
				data=JsonConverter.format(parse);
				ApiResponse submitCDNJob = submitCDNJob(data, task.getId());
				if (submitCDNJob.getStatusCode() != ResponseDictionary.SUCCESS) {
					errorResponse = submitCDNJob;
					break;
				}
			}
		}
		if (errorResponse != null) {
			return buildResponseMap(errorResponse.getStatusCode(), errorResponse.getMessage(), null);
		}
		return buildResponseMap(ResponseStatusCode.OK, "成功", null);
	}

	public Map<String, Object> buildResponseMap(Integer statusCode, String message, Object data) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("statusCode", statusCode);
		response.put("message", message);
		response.put("data", data);
		return response;
	}

}
