package cn.videoworks.cms.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.videoworks.cms.client.RemoveStorageClient;
import cn.videoworks.cms.constant.ResponseDictionary;
import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.dto.StorageRemoveDto;
import cn.videoworks.cms.dto.StorageRequestDto;
import cn.videoworks.cms.entity.Content;
import cn.videoworks.cms.entity.ContentPosterMapping;
import cn.videoworks.cms.entity.Media;
import cn.videoworks.cms.enumeration.ContentStatus;
import cn.videoworks.cms.service.ContentPosterMappingService;
import cn.videoworks.cms.service.ContentService;
import cn.videoworks.cms.service.MediaService;
import cn.videoworks.cms.util.ApiResponse;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.commons.util.json.JsonConverter;


/**
 * @author   meishen
 * @Date	 2018	2018年8月23日		下午6:59:36
 * @Description 方法描述: 自动清理过期内容
 */
@Component
public class AutoClearContentSchedule {
	
	@Resource
	private ContentService contentServiceImpl;
	
	@Resource
	private MediaService mediaServiceImpl;
	
	@Resource
	private ContentPosterMappingService contentPosterMappingServiceImpl;
	
	@Resource
	private Properties databaseConfig;
	
	private static final Logger log = LoggerFactory.getLogger(AutoClearContentSchedule.class);

	/**
	 * clearContent:(自动清理过期内容，0 0 15 * * ? 每天下午3点执行)
	 * @author   meishen
	 * @Date	 2018	2018年8月23日		下午7:01:14 
	 * @return void    
	 * @throws
	 */
	@Scheduled(cron="0 0 15,16 * * ?")
//	@Scheduled(cron="0/5 * * * * ?")
	public void clearContent() {
		boolean isBooths = databaseConfig.containsKey("is.booths")?Boolean.valueOf(databaseConfig.getProperty("is.booths")):true;
		if(!isBooths) {
			boolean clearSwitch = databaseConfig.containsKey("clear.storage.switch")?Boolean.valueOf(databaseConfig.getProperty("clear.storage.switch")):false;
	 		if(clearSwitch) {
				Page page = new Page();
				page.setSize(2000);
				int day = Integer.valueOf(databaseConfig.getProperty("clear.storage"));
				String publishTime = DateUtil.getPastDate(day);
				List<Content> list = contentServiceImpl.list(publishTime, page);
				List<StorageRemoveDto> dtos = new ArrayList<StorageRemoveDto>();
				
				for(Content content : list) {
					StorageRemoveDto dto = new StorageRemoveDto();
					dto.setContentId(content.getId());
					List<StorageRequestDto> reqsDto = new ArrayList<StorageRequestDto>();
					
				   List<Media> medias = mediaServiceImpl.getByContentId(content.getId());
				   for(Media media : medias) {
					   reqsDto.add(ConvertDto.convertStorageRequestDto(media));
				   }
				   
				   List<ContentPosterMapping> contentPosterMappings = contentPosterMappingServiceImpl.getByContentId1(content.getId());
				   for (ContentPosterMapping contentPosterMapping : contentPosterMappings) {
					   reqsDto.add(ConvertDto.convertStorageRequestDto(contentPosterMapping.getPoster()));
				   }
				   dto.setStorageRequestDto(reqsDto);
				   dtos.add(dto);
				}
				
				try {
					if(null != dtos && dtos.size() > 0) {
						log.info("开始清除：【"+publishTime+"】之前的数据，共：【"+dtos.size()+"】条");
						//移出存储
						ApiResponse result = RemoveStorageClient.getRemoveStorageClient(databaseConfig.getProperty("gearman.ip"),Integer.valueOf(databaseConfig.getProperty("gearman.port"))).execute(dtos);
//						log.info("work返回结果："+JsonConverter.format(result));
						if(result.getStatusCode() == ResponseDictionary.SUCCESS) {
							ApiResponse rest = JsonConverter.parse(result.getData().toString(), ApiResponse.class);
							if(rest.getStatusCode() == ResponseDictionary.SUCCESS) {
								List<StorageRemoveDto> storageResponses = JsonConverter.asList(JsonConverter.format(rest.getData()), StorageRemoveDto.class);
								for(StorageRemoveDto dto : storageResponses) {
									Long contentId = dto.getContentId();
									if(null != dto.getFlag() && dto.getFlag()) {
										Content content = contentServiceImpl.get(contentId);
										content.setStatus(ContentStatus.DELETESTORAGE.getValue());
										contentServiceImpl.update(content);
										log.info("内容【"+content.getTitle()+"】存储清理成功！");
									}
								}
							}else {
								log.error("清除对象存储数据失败!");
							}
						}else {
							log.error("调用清除对象存储client失败!");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.error("清除对象存储数据失败!");
				}
			}
		}
	}
		
	
}
