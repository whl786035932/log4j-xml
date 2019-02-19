/**
 * AdiController.java
 * cn.videoworks.edge.web
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2017年6月26日 		meishen
 *
 * Copyright (c) 2017, TNT All Rights Reserved.
*/

package cn.videoworks.cms.web;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.client.CDNGearmanClient;
import cn.videoworks.cms.client.StorageClient;
import cn.videoworks.cms.constant.GearmanFunctionConstant;
import cn.videoworks.cms.constant.ResponseDictionary;
import cn.videoworks.cms.dto.AdiContentDto;
import cn.videoworks.cms.dto.AdiMovieDto;
import cn.videoworks.cms.dto.CDNJobDto;
import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.dto.LogDto;
import cn.videoworks.cms.dto.NewsdbContentDto;
import cn.videoworks.cms.dto.OperationConvertDto;
import cn.videoworks.cms.dto.StorageRequestDto;
import cn.videoworks.cms.dto.StorageResponseDto;
import cn.videoworks.cms.entity.Classification;
import cn.videoworks.cms.entity.Content;
import cn.videoworks.cms.entity.ContentPosterMapping;
import cn.videoworks.cms.entity.InstorageStatistics;
import cn.videoworks.cms.entity.Media;
import cn.videoworks.cms.entity.Poster;
import cn.videoworks.cms.entity.Source;
import cn.videoworks.cms.entity.Task;
import cn.videoworks.cms.entity.TaskStatistics;
import cn.videoworks.cms.enumeration.BusinessType;
import cn.videoworks.cms.enumeration.CdnStatus;
import cn.videoworks.cms.enumeration.CdnType;
import cn.videoworks.cms.enumeration.ContentStatus;
import cn.videoworks.cms.enumeration.ContentType;
import cn.videoworks.cms.enumeration.LogLevel;
import cn.videoworks.cms.enumeration.LogSource;
import cn.videoworks.cms.enumeration.LogType;
import cn.videoworks.cms.enumeration.TaskStatus;
import cn.videoworks.cms.service.ClassificationContentMappingService;
import cn.videoworks.cms.service.ClassificationService;
import cn.videoworks.cms.service.ContentPosterMappingService;
import cn.videoworks.cms.service.ContentService;
import cn.videoworks.cms.service.InstorageStatisticsService;
import cn.videoworks.cms.service.MediaService;
import cn.videoworks.cms.service.PosterService;
import cn.videoworks.cms.service.RestTemplateService;
import cn.videoworks.cms.service.SourceService;
import cn.videoworks.cms.service.TaskService;
import cn.videoworks.cms.service.TaskStatisticsService;
import cn.videoworks.cms.thread.VcaAnalyze;
import cn.videoworks.cms.util.ApiResponse;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.util.UserUtil;
import cn.videoworks.commons.util.json.JsonConverter;

/**
 * ClassName:AdiController
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.0.0
 * @Date	 2017年6月26日		下午2:32:19
 *
 */
@Controller
@RequestMapping("/cms/adi/v1")
public class AdiController {
	
	@Resource
	private ContentService contentServiceImpl;
	
	@Resource
	private PosterService posterServiceImpl;
	
	@Resource
	private MediaService mediaServiceImpl;
	
	@Resource
	private ContentPosterMappingService contentPosterMappingServiceImpl;
	
	@Resource
	private SourceService sourceServiceImpl;
	
	@Resource
	private ClassificationService classificationServiceImpl;
	
	@Resource
	private ClassificationContentMappingService classificationContentMappingServiceImpl;
	
	@Resource
	private PosterService posterServcieImpl;
	
	@Resource
	private RestTemplateService restTemplateServiceImpl;
	
	@Resource
	private TaskService taskServiceImpl;
	
	@Resource
	private TaskStatisticsService taskStatisticsServiceImpl;
	
	@Resource
	private InstorageStatisticsService instorageStatisticsServiceImpl;
	
	@Resource
	private Properties databaseConfig;
	
	private static final Logger log = LoggerFactory.getLogger(AdiController.class);
	
	
	/**
	 * content:(同步内容、媒体、海报)
	 * @author   meishen
	 * @Date	 2017	2017年7月20日		下午3:23:09
	 * @return RestResponse    
	 * @throws 
	 * @since  CodingExample　Ver 1.0.0
	 */
	@RequestMapping(value="content",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> content(@Valid @RequestBody AdiContentDto content ,BindingResult bindingResult,HttpServletRequest request) throws Exception{
		//记录注入原始数据
		Map<String,Object> detail = new HashMap<String,Object>();
		detail.put("param", JsonConverter.format(content));
		LogDto writeCmsLogDto = ConvertDto.buildSystemLog("CMS数据注入功能-注入CMS原始数据", BusinessType.WRITECMS.getValue(), "CMS数据注入-注入CMS", content.getTitle(), UserUtil.getUserUtil(request).getUsername(), LogType.SYSTEMLOG.getValue(), LogSource.CMS.getValue(), LogLevel.DEBUG.getValue(), detail);
		log.info(JsonConverter.format(writeCmsLogDto));
		if(bindingResult.hasErrors()) {
			return ConvertDto.buildRestResponse(ResponseDictionary.UNAUTHORIZED, "参数错误", bindingResult.getFieldErrors());
		}
		List<Object> imageErrors =ConvertDto.validatorListForImages(content.getImages());
		if(imageErrors.size()>0){
			return ConvertDto.buildRestResponse(ResponseDictionary.UNAUTHORIZED, "参数错误", imageErrors);
		}
		List<Object> movieErrors =ConvertDto.validatorListForMovies(content.getMovies());
		if(movieErrors.size()>0){
			return ConvertDto.buildRestResponse(ResponseDictionary.UNAUTHORIZED, "参数错误", movieErrors);
		}
		String sourceChannel = content.getSource_channel() != null?content.getSource_channel().trim():"";
		String sourceColumn = content.getSource_column() != null?content.getSource_column().trim():"";
		Content cont = contentServiceImpl.get(content.getTitle().trim(), content.getPublish_time().toString(), content.getCp().trim(),sourceChannel,sourceColumn );
		if(null == cont) {
			cont = ConvertDto.convertContent(content);
			
			List<Media> medias =new ArrayList<Media>();
			Set<ContentPosterMapping> contentPosterMappings =new HashSet<ContentPosterMapping>();
			//添加媒体
			for(AdiMovieDto movie : content.getMovies()) {
				Media media = ConvertDto.convertMedia(movie,cont);
				medias.add(media);
			}
			cont.setMedias(medias);
			contentServiceImpl.save(cont);//添加内容
			
			//验证来源
			Source source = sourceServiceImpl.get(content.getSource().replace(" ", ""));
			if(null == source) {
				source = ConvertDto.convertSource(content.getSource(),content.getSource());
				sourceServiceImpl.save(source);
			}
			
			//存储请求参数
			List<StorageRequestDto> res = new ArrayList<StorageRequestDto>();
			
			for (int i = 0; i < content.getImages().size(); i++) {
				//添加海报
				Poster poster = ConvertDto.convertPoster(content.getImages().get(i),content.getSource());
				posterServiceImpl.save(poster);
				//创建内容海报关系
				ContentPosterMapping contentPosterMapping =ConvertDto.convertContentPosterMapping(cont, poster, i+1);
				contentPosterMappingServiceImpl.save(contentPosterMapping);
				
				contentPosterMappings.add(contentPosterMapping);
				StorageRequestDto dto = ConvertDto.convertStorageRequestDto(poster);
				res.add(dto);
			}
			
			cont.setContentPosterMappings(contentPosterMappings);
			contentServiceImpl.update(cont);//添加内容
			for(Media m : medias) {
				StorageRequestDto dto = ConvertDto.convertStorageRequestDto(m);
				res.add(dto);
			}
			try {
				//注入存储
				ApiResponse result = StorageClient.getStorageClient(databaseConfig.getProperty("gearman.ip"),Integer.valueOf(databaseConfig.getProperty("gearman.port"))).execute(res);
//				ApiResponse result = new ApiResponse();
//				result.setStatusCode(100000);
				if(result.getStatusCode() == ResponseDictionary.SUCCESS) {
					//数据正常返回
					try {
						ApiResponse  rr  = processData(result.getData().toString());
//						ApiResponse  rr  = new ApiResponse();
//						rr.setStatusCode(100000);
						if(rr.getStatusCode() != ResponseDictionary.SUCCESS) {
							//记录注入存储-失败-系统日志
							LogDto logDto = ConvertDto.buildSystemLog("CMS数据注入功能-注入存储失败【"+rr.getMessage()+"】！", BusinessType.WRITESTORAGE.getValue(), "CMS数据注入-注入存储", cont.getId().toString(), UserUtil.getUserUtil(request).getUsername(), LogType.SYSTEMLOG.getValue(), LogSource.CMS.getValue(), LogLevel.ERROR.getValue(), new HashMap<String,Object>());
							log.error(JsonConverter.format(logDto));
							
							//清除内容、海报、媒体关系
							removeContentPosterMeida(cont);
							return ConvertDto.buildRestResponse(ResponseDictionary.EXTERNALINTERFACECALLSEXCEPTION, rr.getMessage(), null);
						}else {//处理成功
							//记录注入存储-成功-系统日志
							LogDto storageLogDto = ConvertDto.buildSystemLog("CMS数据注入功能-注入存储成功！", BusinessType.WRITESTORAGE.getValue(), "CMS数据注入-注入存储", cont.getId().toString(), UserUtil.getUserUtil(request).getUsername(), LogType.SYSTEMLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), new HashMap<String,Object>());
							log.info(JsonConverter.format(storageLogDto));
							
							//验证是否自动上架、分类
							//处理分类(classification)和地区(area)
							List<String> classifications = content.getClassifications();//指定需要的分类
							List<String> areas = content.getAreas();//指定需要的地区
							
							List<String> cfIds = new ArrayList<String>();//构建分类id列表
							List<Long> cIds = new ArrayList<Long>();
							cIds.add(cont.getId());
							
							buildClassificationIds(classifications,areas, cfIds);
							
							new ContentController().buildClassificationContentMapping(cfIds, cIds,classificationContentMappingServiceImpl,contentServiceImpl);
							//分类存在，则自动上架
							if(cfIds != null && cfIds.size() > 0) {
								//内容上架 标题不重复且是未上架状态
								Content content2 = contentServiceImpl.get(cont.getTitle(), ContentStatus.SHELVED.getValue());
								if(content2 == null) {
									cont.setStatus(ContentStatus.SHELVED.getValue());
									cont.setUpdatedAt(DateUtil.getNowTimeStamp());
									contentServiceImpl.update(cont);
								}
							}
							
							//创建入站管理任务和注入cdn
							Long contentId = cont.getId();
							Set<ContentPosterMapping> unCdnPosterMappings = cont.getContentPosterMappings();
							List<Media> unCdnMedias =cont.getMedias();
							if (unCdnPosterMappings.size() > 0 || unCdnMedias.size() > 0) {
								// 添加一条任务
								final Map<String, Object> addCdnTaskMap = addCdnTaskByContent(contentId, unCdnMedias, unCdnPosterMappings,
										1,true);
								Runnable runnable = new Runnable() {
									@Override
									public void run() {
										@SuppressWarnings("unused")
										ApiResponse submitCDNJobResponse = submitCDNJob(addCdnTaskMap);
									}
								};
								ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(1);
								newFixedThreadPool.execute(runnable);
							} 
							//------------end
							
							//是否自动同步到新闻通
							if(databaseConfig.containsKey("sync.newsdb")) {
								if(Boolean.valueOf(databaseConfig.getProperty("sync.newsdb")) == true) {
									NewsdbContentDto newsdbContentDto = ConvertDto.convertNewsdbContentDto(content);
									//调用百度vcaClient
									vcaAnalyze(result.getData().toString(),newsdbContentDto);
								}
							}
							
							//记录入库统计数据
							String statisticsAt = DateUtil.getNowTimeYMD();
							InstorageStatistics channelStatistics = instorageStatisticsServiceImpl.get(sourceChannel,statisticsAt,null);
							//新建入库频道统计
							if(null == channelStatistics) {
								InstorageStatistics newChannelStatistics = ConvertDto.convertInstorageStatistics(sourceChannel, null);
								instorageStatisticsServiceImpl.save(newChannelStatistics);
								
								 InstorageStatistics  columnStatistics = instorageStatisticsServiceImpl.get(sourceColumn,statisticsAt,newChannelStatistics);
								 if(null != columnStatistics) {
									 Integer value = Integer.valueOf(columnStatistics.getValue());
									 columnStatistics.setValue(String.valueOf(value+1));
									 instorageStatisticsServiceImpl.update(columnStatistics);
								 }else {
									 InstorageStatistics newInstorageStatistics = ConvertDto.convertInstorageStatistics(sourceColumn, newChannelStatistics);
									 instorageStatisticsServiceImpl.save(newInstorageStatistics);
								 }
							}else {
								Integer valueChannel = Integer.valueOf(channelStatistics.getValue());
								channelStatistics.setValue(String.valueOf(valueChannel+1));
								 instorageStatisticsServiceImpl.update(channelStatistics);
								
								 InstorageStatistics  columnStatistics = instorageStatisticsServiceImpl.get(sourceColumn,statisticsAt,channelStatistics);
								 if(null != columnStatistics) {
									 Integer value = Integer.valueOf(columnStatistics.getValue());
									 columnStatistics.setValue(String.valueOf(value+1));
									 instorageStatisticsServiceImpl.update(columnStatistics);
								 }else {
									 InstorageStatistics newInstorageStatistics = ConvertDto.convertInstorageStatistics(sourceColumn, channelStatistics);
									 instorageStatisticsServiceImpl.save(newInstorageStatistics);
								 }
							}
							 
						}
					} catch (Exception e) {
						e.printStackTrace();
						removeContentPosterMeida(cont);//清除内容、海报、媒体关系
						//记录注入存储失败系统日志
						LogDto logDto = ConvertDto.buildSystemLog("CMS数据注入功能-注入存储失败【"+result.getMessage()+"】！", BusinessType.WRITESTORAGE.getValue(), "CMS数据注入-注入存储", cont.getId().toString(), UserUtil.getUserUtil(request).getUsername(), LogType.SYSTEMLOG.getValue(), LogSource.CMS.getValue(), LogLevel.ERROR.getValue(), new HashMap<String,Object>());
						log.error(JsonConverter.format(logDto));
					}
				}else {
					//清除内容、海报、媒体关系
					removeContentPosterMeida(cont);
					//记录注入存储失败系统日志
					LogDto logDto = ConvertDto.buildSystemLog("CMS数据注入功能-注入存储失败【"+result.getMessage()+"】！", BusinessType.WRITESTORAGE.getValue(), "CMS数据注入-注入存储", cont.getId().toString(), UserUtil.getUserUtil(request).getUsername(), LogType.SYSTEMLOG.getValue(), LogSource.CMS.getValue(), LogLevel.ERROR.getValue(), new HashMap<String,Object>());
					log.error(JsonConverter.format(logDto));
					return ConvertDto.buildRestResponse(ResponseDictionary.UNAUTHORIZED, result.getMessage(), null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				//清除内容、海报、媒体关系
				removeContentPosterMeida(cont);
				//记录注入存储失败系统日志
				LogDto logDto = ConvertDto.buildSystemLog("CMS数据注入功能-注入存储失败【"+e.getMessage()+"】！", BusinessType.WRITESTORAGE.getValue(), "CMS数据注入-注入存储", cont.getId().toString(), UserUtil.getUserUtil(request).getUsername(), LogType.SYSTEMLOG.getValue(), LogSource.CMS.getValue(), LogLevel.ERROR.getValue(), new HashMap<String,Object>());
				log.error(JsonConverter.format(logDto));
				return ConvertDto.buildRestResponse(ResponseDictionary.UNAUTHORIZED, e.getMessage(), null);
			}
		}else {//内容存在
			boolean isBooths = databaseConfig.containsKey("is.booths")?Boolean.valueOf(databaseConfig.getProperty("is.booths")):true;
			if(!isBooths) {
				String statisticsAt = DateUtil.getNowTimeYMD();
				TaskStatistics taskStatistics = taskStatisticsServiceImpl.get(statisticsAt);
				if(null  != taskStatistics ) {
					int repeatNumber = taskStatistics.getRepeatNumber() != null?taskStatistics.getRepeatNumber()+1:1;
					taskStatistics.setRepeatNumber(repeatNumber);
					List<String> repeatData = new ArrayList<String>();
					if(taskStatistics.getRepeatData() != null && !taskStatistics.getRepeatData().equals("") )
						repeatData =JsonConverter.asList(taskStatistics.getRepeatData(), String.class);
					repeatData.add("标题【"+cont.getTitle()+"】,频道【"+sourceChannel+"】，栏目【"+sourceColumn+"】，发布时间【"+cont.getPublishTime()+"】，cp【"+cont.getCp()+"】");
					taskStatistics.setRepeatData(JsonConverter.format(repeatData));
					taskStatisticsServiceImpl.update(taskStatistics);
					log.warn("内容【"+cont.getTitle()+"(assetId:"+cont.getAssetId()+")】重复，当天重复数+1");
				}else {
					TaskStatistics convertTaskStatistics = ConvertDto.convertTaskStatistics(0, 0, 0, 0,1);
					List<String> repeatData = new ArrayList<String>();
					repeatData.add("标题【"+cont.getTitle()+"】,频道【"+sourceChannel+"】，栏目【"+sourceColumn+"】，发布时间【"+cont.getPublishTime()+"】，cp【"+cont.getCp()+"】");
					convertTaskStatistics.setRepeatData(JsonConverter.format(repeatData));
					taskStatisticsServiceImpl.save(convertTaskStatistics);
					log.info("CMS注入功能，当天任务统计记录不存在，创建成功!");
				}
			}
			
			return ConvertDto.buildRestResponse(ResponseDictionary.REFUSED, "内容已经存在，请先删除再注入！", null);
		}
		
		return ConvertDto.buildRestResponse(ResponseDictionary.SUCCESS, "成功", null);
	}
	
	/**
	 * 解决注入存储失败数据
	 * @param beginTime
	 * @return
	 */
	@RequestMapping(value="execute",method=RequestMethod.GET)
	@ResponseBody
	public String execute(@RequestParam String beginTime) {
		//存储请求参数
		
		List<Content> contents = contentServiceImpl.list(beginTime);
		for(Content content : contents) {
			List<StorageRequestDto> res = new ArrayList<StorageRequestDto>();
			Set<ContentPosterMapping> cpms  = content.getContentPosterMappings();
			for(ContentPosterMapping cpm : cpms) {
				if(cpm.getPoster().getUrl().startsWith("/")) {
					log.info("内容【"+content.getTitle()+"】海报迁移地址："+cpm.getPoster().getUrl());
					StorageRequestDto dto = ConvertDto.convertStorageRequestDto(cpm.getPoster());
					res.add(dto);
				}
			}
			
			for(Media m : content.getMedias()) {
				if(m.getUrlKey().startsWith("/")) {
					log.info("内容【"+content.getTitle()+"】视频迁移地址："+m.getUrlKey());
					StorageRequestDto dto = ConvertDto.convertStorageRequestDto(m);
					res.add(dto);
				}
			}
			if(res.size() > 0) {
				//数据正常返回
				try {
					ApiResponse result = StorageClient.getStorageClient(databaseConfig.getProperty("gearman.ip"),Integer.valueOf(databaseConfig.getProperty("gearman.port"))).execute(res);
					if(result.getStatusCode() == ResponseDictionary.SUCCESS) {
						ApiResponse  rr  = processData(result.getData().toString());
						if(rr.getStatusCode() != ResponseDictionary.SUCCESS) {
							log.info("【"+content.getTitle()+"】修改存储路径失败【"+rr.getMessage()+"】");
						}else {
							log.info("【"+content.getTitle()+"】修改存储路径成功");
						}
					}else {
						log.info("注入存储失败【"+result.getMessage()+"】");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "ok";
	}
	
	/**
	 * buildClassificationIds:(查询需要指定的分类，构建自动分类功能,指定的ares也一种分类)
	 * @author   meishen
	 * @Date	 2018	2018年7月19日		上午10:36:51
	 * @param classifications 指定的分类
	 * @param ares 需要指定的分类，地区也一种分类
	 * @param cfIds   构建后的分类id集合
	 * @return void    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	private void buildClassificationIds(List<String> classifications,List<String> areas,List<String> cfIds) {
		classifications.removeAll(areas);
		classifications.addAll(areas);
		for(String name : classifications) {
			if(StringUtils.isNotBlank(name)) {
				if(name.contains(","))//多级
				{
					String lastName = name.substring(name.lastIndexOf(",")+1);
					List<String> asList = Arrays.asList(name.split(","));
					Collections.reverse(asList);
					List<Classification> cfs = classificationServiceImpl.getByName(lastName);
					int s = asList.size();
					for(int i =0; i <  cfs.size(); ++i) {
						boolean equaled = true;
						Classification current = cfs.get(i);
						for(int j = 0; j < s - 1; ++j) {
							Classification parent = classificationServiceImpl.get(current.getParent());
							if(null == parent || !parent.getName().equals(asList.get(j+1))) {
								equaled = false;
								break;
							}
							current =parent;
						}
						if(equaled) {
							cfIds.add(cfs.get(i).getId());
						}
					}
				}else {//单级
					List<Classification> cfs = classificationServiceImpl.getByName(name);
					for(Classification cf : cfs) {
						if(cf.getParent() == null || cf.getParent().equals("") || cf.getParent().equals("0")) {
							cfIds.add(cfs.get(0).getId());
						}
					}
				}
			}
		}
	}
	
	/**
	 * processData:(对work响应数据进行处理)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年6月19日		下午3:12:21
	 * @param storageResponses   
	 * @return void    
	 * @throws UnsupportedEncodingException 
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	private ApiResponse processData(String result) throws Exception {
		ApiResponse rest = JsonConverter.parse(result, ApiResponse.class);
		if(rest.getStatusCode() == ResponseDictionary.SUCCESS) {
			List<StorageResponseDto> storageResponses = JsonConverter.asList(JsonConverter.format(rest.getData()), StorageResponseDto.class);
			for(StorageResponseDto dto : storageResponses) {
				int type = dto.getType();
				if(type == ContentType.VIDEO.getValue()) {
					Media m = mediaServiceImpl.get(dto.getId());
					m.setUrlKey(dto.getUrl());
					m.setUpdatedAt(DateUtil.getNowTimeStamp());
					mediaServiceImpl.update(m);
				}else if(type == ContentType.POSTER.getValue()) {
					Poster p = posterServiceImpl.get(dto.getId());
					p.setSourceUrl(dto.getUrl());
					p.setUpdatedAt(DateUtil.getNowTimeStamp());
					posterServiceImpl.update(p);
				}else if(type == ContentType.AD.getValue()) {
					
				}else {
					log.error("存储work响应未知类型，无法进行数据处理！");
				}
			}
		}
		return rest;
	}
	
	/**
	 * removeContentPosterMeida:(移除内容、海报、媒体)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年6月19日		下午7:43:12
	 * @param cont   
	 * @return void    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	private void removeContentPosterMeida(Content cont) {
		//删除视频和内容
		cont.getMedias().isEmpty();
		//清除内容海报关系
		cont.getContentPosterMappings().isEmpty();
		contentServiceImpl.delete(cont.getId());
	}
	
	/**
	 * vcaAnalyze:(启动百度vca视频内容分析)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年8月23日		下午2:21:09
	 * @param result   
	 * @return void    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public void vcaAnalyze(String result,NewsdbContentDto newsdbContentDto) {
		if(StringUtils.isNotBlank(result)) {
			String url = "";
			String poster = "";
			ApiResponse rest = JsonConverter.parse(result, ApiResponse.class);
			if(rest.getStatusCode() == ResponseDictionary.SUCCESS) {
				List<StorageResponseDto> storageResponses = JsonConverter.asList(JsonConverter.format(rest.getData()), StorageResponseDto.class);
				for(StorageResponseDto dto : storageResponses) {
					int type = dto.getType();
					if(type == ContentType.VIDEO.getValue()) {
						url = dto.getUrl();
						continue;
					}else if(type == ContentType.POSTER.getValue()) {
						poster = dto.getUrl();
						continue;
					}
				}
				newsdbContentDto.setPoster(poster);
				if(StringUtils.isNotBlank(url)) {
					newsdbContentDto.setUrl(url);
					VcaAnalyze vcaAnalyze = new VcaAnalyze(
							databaseConfig,
							restTemplateServiceImpl,
							newsdbContentDto
							);
					new Thread(vcaAnalyze).start();
				}else {
					log.warn("对象存储地址为空");
				}
			}
		}
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

			//获取内容所在分类列表，通过注入cdn,给贵州智能推荐系统使用，
			//cdn工单 ElementType='Series' type = '新闻' ViewType = '13' kind = '军事/教育' 多个以/分割
			//通过tags参数传递到work中
			List<String> classifications = classificationContentMappingServiceImpl.getClassifications(contentId);
			if(null != classifications) {
				String classificationNames = StringUtils.join(classifications, ",");
				content.setTags(classificationNames);
			}
			
			//组织cdn参数
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
			taskServiceImpl.save(task);

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
	@SuppressWarnings("unused")
	public ApiResponse submitCDNJob(Map<String, Object> cdnDataMap) {
		ApiResponse result = new ApiResponse();
		String user = databaseConfig.containsKey("user.name") == true ? databaseConfig.getProperty("user.name") : "未知";
		Long taskId = (Long) cdnDataMap.get("taskId");
		String cdnData = (String) cdnDataMap.get("data");
		Long contentId = (Long) cdnDataMap.get("contentId");
		String gearamHost = databaseConfig.getProperty("gearman.ip");
		String gearmanPortStr = databaseConfig.getProperty("gearman.port");
		CDNGearmanClient cdnGearmanClient =  CDNGearmanClient.getCDNGearmanClient(gearamHost, Integer.valueOf(gearmanPortStr),
				GearmanFunctionConstant.WRITE_CDN, contentServiceImpl, posterServcieImpl, contentPosterMappingServiceImpl, mediaServiceImpl,
				taskServiceImpl, databaseConfig);

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
	
	
	
	
	
	
	
	
	
	

}

