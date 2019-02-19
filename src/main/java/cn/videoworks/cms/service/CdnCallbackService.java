package cn.videoworks.cms.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.gearman.GearmanJobEvent;
import org.gearman.GearmanJobEventCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.videoworks.cms.constant.ResponseDictionary;
import cn.videoworks.cms.dto.CDNResponse;
import cn.videoworks.cms.dto.CDNReturnDto;
import cn.videoworks.cms.dto.CDNWorkerResponseDto;
import cn.videoworks.cms.dto.LogDto;
import cn.videoworks.cms.dto.OperationConvertDto;
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
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.web.AdiController;
import cn.videoworks.commons.util.json.JsonConverter;

public class CdnCallbackService implements GearmanJobEventCallback<String> {
	private static final Logger log = LoggerFactory.getLogger(AdiController.class);
	private ContentService contentService;

	private PosterService posterService;

	private ContentPosterMappingService contentPosterMappingService;

	private MediaService mediaService;

	private TaskService taskService;
	private Properties databaseConfig;

	public CdnCallbackService(ContentService contentService, PosterService posterService,
			ContentPosterMappingService contentPosterMappingService, MediaService mediaService, TaskService taskService,
			Properties databaseConfig) {
		super();
		this.contentService = contentService;
		this.posterService = posterService;
		this.contentPosterMappingService = contentPosterMappingService;
		this.mediaService = mediaService;
		this.taskService = taskService;
		this.databaseConfig = databaseConfig;
	}

	public CdnCallbackService() {
		super();
	}

	@Override
	public void onEvent(String arg0, GearmanJobEvent event) {
		String user = databaseConfig.containsKey("user.name") == true ? databaseConfig.getProperty("user.name") : "未知";

		switch (event.getEventType()) {
		case GEARMAN_SUBMIT_SUCCESS:
			log.debug(event.getEventType() + " 提交成功");
			break;
		case GEARMAN_JOB_SUCCESS:
			try {
				String result = new String(event.getData(), "utf-8");
				log.debug("收到的cdn worker的结果---------------------------="+result);
				CDNResponse workerResponse = JsonConverter.parse(result, CDNResponse.class);
				if (workerResponse != null) {
					int statusCode = workerResponse.getStatusCode();
					if (statusCode == ResponseDictionary.SUCCESS) { // 模拟百度cdn,直接返回了结果
						// 开始修改数据库------------media ---poster-----------模拟状态
						//
						CDNWorkerResponseDto parse = JsonConverter.parse(JsonConverter.format(workerResponse.getData()),
								CDNWorkerResponseDto.class);
						List<CDNReturnDto> cdns = parse.getCdns();
						String contentId = parse.getContentId();
						HashMap<String, Object> detail_BAIDU = new HashMap<>();
						detail_BAIDU.put("param", JsonConverter.format(workerResponse));
						LogDto monitorBaiduCdn = OperationConvertDto.buildSystemLog(
								"提交CDN注入请求 成功，且CDN注入成功", BusinessType.WRITECDN.getValue(),
								"提交CDN注入请求 成功，且CDN注入成功，返回结果：", contentId + "", user,
								LogType.SYSTEMLOG.getValue(), LogSource.BOOTHS.getValue(), LogLevel.INFO.getValue(),
								detail_BAIDU);
						log.info(JsonConverter.format(monitorBaiduCdn));
						Long taskId = parse.getTaskId();
						updateMediaAndPosterCDN(cdns);
						updateTaskStatus(taskId, CdnStatus.SYNCHRONIZED.getValue(),null);
						updateContentCdnStatus(Long.valueOf(contentId), CdnStatus.SYNCHRONIZED.getValue());


					} else if (statusCode == ResponseDictionary.WAITCDNCALLBACK) {

						// 获取工单号-----------------------------商量工单好的返回结果
						CDNWorkerResponseDto parse = JsonConverter.parse(JsonConverter.format(workerResponse.getData()),
								CDNWorkerResponseDto.class);
						String msgid = parse.getMsgid();
						String contentIdReturnId = parse.getContentId();
						Long taskId = parse.getTaskId();
						// 获取海报然后把海报上传到apache路径下

						List<CDNReturnDto> cdns = parse.getCdns();

						udpateTaskWithMsgId(taskId, msgid);
						updatePosterCdn(cdns);
						HashMap<String, Object> detailQianMap = new HashMap<>();
						detailQianMap.put("param", JsonConverter.format(workerResponse));
						LogDto monitorBaiduCdn = OperationConvertDto.buildSystemLog(
								"提交CDN注入请求 成功", BusinessType.WRITECDN.getValue(),
								"提交CDN注入请求 成功 的返回结果：", contentIdReturnId + "", user,
								LogType.SYSTEMLOG.getValue(), LogSource.BOOTHS.getValue(), LogLevel.INFO.getValue(),
								detailQianMap);
						log.info(JsonConverter.format(monitorBaiduCdn));
					} else {

						// cdn worker那块儿有问题，cdn注入失败
						CDNWorkerResponseDto parse = JsonConverter.parse(JsonConverter.format(workerResponse.getData()),
								CDNWorkerResponseDto.class);
						String cdn_failMessage = workerResponse.getMessage();
						String contentId = parse.getContentId();
						Long taskId = parse.getTaskId();
						updateTaskStatus(taskId, CdnStatus.SYNCHRONIZEDFAILED.getValue(),"失败原因:"+cdn_failMessage);
						updateContentCdnStatus(Long.valueOf(contentId), CdnStatus.SYNCHRONIZEDFAILED.getValue());

						HashMap<String, Object> detailQianMap = new HashMap<>();
						detailQianMap.put("param", JsonConverter.format(workerResponse));
						LogDto monitorBaiduCdn = OperationConvertDto.buildSystemLog("提交CDN注入请求失败",
								BusinessType.WRITECDN.getValue(), "提交CDN注入请求 失败，返回结果：" + cdn_failMessage,
								contentId + "", user, LogType.SYSTEMLOG.getValue(), LogSource.BOOTHS.getValue(),
								LogLevel.INFO.getValue(), detailQianMap);
						log.info(JsonConverter.format(monitorBaiduCdn));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case GEARMAN_SUBMIT_FAIL:
			String message = "注入CDN失败！原因【" + event.getEventType() + ":" + new String(event.getData()) + "】";
			log.error(message);
			// warning // 开始修改数据库------------media ---poster-----------模拟状态
			// updateTaskStatus(taskId, CdnStatus.SYNCHRONIZEDFAILED.getValue());
			// updateContentCdnStatus(contentId, CdnStatus.SYNCHRONIZEDFAILED.getValue());

			break;
		case GEARMAN_JOB_FAIL:
			String message1 = "注入CDN失败-！原因【" + event.getEventType() + ":" + new String(event.getData()) + "】";
			log.error(message1);
			// warning // 开始修改数据库------------media ---poster-----------模拟状态
			// updateTaskStatus(taskId, CdnStatus.SYNCHRONIZEDFAILED.getValue());
			// updateContentCdnStatus(contentId, CdnStatus.SYNCHRONIZEDFAILED.getValue());

			break;
		case GEARMAN_EOF:
			break;
		default:
			break;
		}
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
				if(media!=null) {
					
					media.setCdn_sync_status(CdnStatus.SYNCHRONIZED.getValue());
					media.setCdnKey(cdn_key);
					media.setUpdatedAt(nowTimeStamp);
					mediaService.update(media);
				}
			} else if (type == CdnReturnType.POSTER.getValue()) {
				Poster poster = posterService.get(id);
				if(poster!=null) {
					poster.setUrl(cdn_key);
					poster.setUpdatedAt(nowTimeStamp);
					poster.setCdnSyncStatus(CdnStatus.SYNCHRONIZED.getValue());
					posterService.update(poster);
				}
			}
		}
	}
	
	public void updatePosterCdn(List<CDNReturnDto> cdnDatas) {
		Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
		for (CDNReturnDto cdnReturnDto : cdnDatas) {
			Integer type = cdnReturnDto.getType();
			Long id = cdnReturnDto.getId();
			String cdn_key = cdnReturnDto.getCdn_key();
			if (type == CdnReturnType.POSTER.getValue()) {
				Poster poster = posterService.get(id);
				if(poster!=null) {
					poster.setUrl(cdn_key);
					poster.setUpdatedAt(nowTimeStamp);
					poster.setCdnSyncStatus(CdnStatus.SYNCHRONIZED.getValue());
					posterService.update(poster);
				}
			}
		}
	}

	/**
	 * 修改内容的cdn状态
	 * 
	 * @param contentId
	 * @param cdnStatus
	 */
	public void updateContentCdnStatus(Long contentId, Integer cdnStatus) {
		Content content = contentService.get(contentId);
		if(content!=null) {
			Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
			content.setCdn_sync_status(cdnStatus);
			content.setUpdatedAt(nowTimeStamp);
			contentService.update(content);
		}
	}

	/**
	 * 修改任务的状态 TO---------------DO
	 * 
	 * @return
	 */
	public void updateTaskStatus(Long taskId, Integer cdnStatus,String reason) {
		Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
		Task task = taskService.get(taskId);
		if(task!=null) {
			if(reason!=null) {
				task.setMessage(reason);
			}
			task.setStatus(cdnStatus);
			task.setUpdated_at(nowTimeStamp);
			taskService.update(task);
		}

	}

	/***
	 * 根据提交cdn任务的处理结果获取工单号，并修改task的工单号
	 * 
	 * @param taskId
	 * @param msgId
	 */
	public void udpateTaskWithMsgId(Long taskId, String msgId) {
		Task task = taskService.get(taskId);
		if (task != null) {
			task.setMsgid(msgId);
			taskService.update(task);
		}
	}

}
