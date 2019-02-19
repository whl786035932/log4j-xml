/**
 * ConvertDto.java
 * cn.videoworks.edge.dto
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2017年6月8日 		meishen
 *
 * Copyright (c) 2017, TNT All Rights Reserved.
*/

package cn.videoworks.cms.dto;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.FieldError;

import cn.videoworks.cms.entity.ApkVersion;
import cn.videoworks.cms.entity.Classification;
import cn.videoworks.cms.entity.ClassificationContentMapping;
import cn.videoworks.cms.entity.Content;
import cn.videoworks.cms.entity.ContentPosterMapping;
import cn.videoworks.cms.entity.InstorageStatistics;
import cn.videoworks.cms.entity.Media;
import cn.videoworks.cms.entity.Menu;
import cn.videoworks.cms.entity.Operation;
import cn.videoworks.cms.entity.Permission;
import cn.videoworks.cms.entity.PermissionMenuMapping;
import cn.videoworks.cms.entity.PermissionOperatoinMapping;
import cn.videoworks.cms.entity.Poster;
import cn.videoworks.cms.entity.Role;
import cn.videoworks.cms.entity.Source;
import cn.videoworks.cms.entity.Task;
import cn.videoworks.cms.entity.TaskStatistics;
import cn.videoworks.cms.entity.User;
import cn.videoworks.cms.entity.UserRoleMapping;
import cn.videoworks.cms.enumeration.Action;
import cn.videoworks.cms.enumeration.CdnStatus;
import cn.videoworks.cms.enumeration.ClassificationRecommend;
import cn.videoworks.cms.enumeration.ContentStatus;
import cn.videoworks.cms.enumeration.ContentType;
import cn.videoworks.cms.enumeration.ElementType;
import cn.videoworks.cms.enumeration.PermissionType;
import cn.videoworks.cms.enumeration.Status;
import cn.videoworks.cms.enumeration.TaskStatus;
import cn.videoworks.cms.enumeration.UserType;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.vo.TaskStatisticsVo;
import cn.videoworks.commons.util.json.JsonConverter;


/**
 * ClassName:ConvertDto
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.0.0
 * @Date	 2017年6月8日		下午3:15:45
 *
 */
public class ConvertDto {
	
	public static InstorageStatistics convertInstorageStatistics(String name,InstorageStatistics parent) {
		InstorageStatistics instorageStatistics = new InstorageStatistics();
		instorageStatistics.setInsertedAt(DateUtil.getNowTimeStamp());
		instorageStatistics.setName(name);
		instorageStatistics.setParent(parent);
		instorageStatistics.setStatisticeAt(DateUtil.getNowTimeYMD());
		instorageStatistics.setUpdatedAt(DateUtil.getNowTimeStamp());
		instorageStatistics.setValue("1");
		return instorageStatistics;
	}
	
	public static void convertTaskStatistics1(TaskStatistics taskStatistics,int totalNumber,int successNumber,int waitingNumber,int failureNumber) {
		if(null != taskStatistics) {
			taskStatistics.setTotalNumber(totalNumber);
			taskStatistics.setSuccessNumber(successNumber);
			taskStatistics.setWaitingNumber(waitingNumber);
			taskStatistics.setFailureNumber(failureNumber);
			taskStatistics.setUpdatedAt(DateUtil.getNowTimeStamp());
		}
	}
	
	public static TaskStatistics convertTaskStatistics(int totalNumber,int successNumber,int waitingNumber,int failureNumber,int repeatNumber) {
		TaskStatistics taskStatistics = new TaskStatistics();
		taskStatistics.setFailureNumber(failureNumber);
		taskStatistics.setInsertedAt( DateUtil.getNowTimeStamp());
		taskStatistics.setSuccessNumber(successNumber);
		taskStatistics.setTotalNumber(totalNumber);
		taskStatistics.setUpdatedAt(DateUtil.getNowTimeStamp());
		taskStatistics.setWaitingNumber(waitingNumber);
		taskStatistics.setRepeatNumber(repeatNumber);
		taskStatistics.setStatisticsAt(DateUtil.getNowTimeYMD());
		return taskStatistics;
	}
	
	public static TaskStatisticsVo convertTaskStatistics(TaskStatistics taskStatistics) {
		TaskStatisticsVo vo = new TaskStatisticsVo();
		if(null != taskStatistics) {
			vo.setFailureNumber(taskStatistics.getFailureNumber());
			vo.setId(taskStatistics.getId());
			vo.setInsertedAt(DateUtil.getDate(taskStatistics.getInsertedAt()));
			vo.setSuccessNumber(taskStatistics.getSuccessNumber());
			vo.setTotalNumber(taskStatistics.getTotalNumber());
			vo.setUpdatedAt(DateUtil.getDate(taskStatistics.getUpdatedAt()));
			vo.setWaitingNumber(taskStatistics.getWaitingNumber());
			vo.setRepeatNumber(taskStatistics.getRepeatNumber());
			vo.setStatisticsAt(taskStatistics.getStatisticsAt());
			if(taskStatistics.getRepeatData() != null && !taskStatistics.getRepeatData().equals(""))
				vo.setRepeatData(JsonConverter.asList(taskStatistics.getRepeatData(),String.class));
		}
		return vo;
	}
	
	public static ClassificationDto convertClassification(Classification classification) {
		ClassificationDto dto = new ClassificationDto();
		dto.setAlias(classification.getAlias());
		dto.setChildren(new ArrayList<ClassificationDto>());
		dto.setDeletable(classification.getDeletable().getValue());
		dto.setEditable(classification.getEditable().getValue());
		dto.setIcon(classification.getIcon());
		dto.setId(classification.getId());
		dto.setLevel(classification.getLevel());
		dto.setName(classification.getName());
		dto.setpId(classification.getParent());
		dto.setSequence(classification.getSequence());
		dto.setStatus(classification.getStatus().getValue());
		dto.setType(classification.getType().getValue());
		return dto;
	}
	
	public static NewsdbContentDto convertNewsdbContentDto(AdiContentDto dto) {
		NewsdbContentDto newsdbContentDto = new NewsdbContentDto();
		newsdbContentDto.setAsset_id(dto.getAsset_id());
		newsdbContentDto.setCp(dto.getCp());
		newsdbContentDto.setDescription(dto.getDescription());
		newsdbContentDto.setPublish_time(dto.getPublish_time());
		newsdbContentDto.setSource(dto.getSource());
		newsdbContentDto.setSource_channel(dto.getSource_channel());
		newsdbContentDto.setSource_column(dto.getSource_column());
		newsdbContentDto.setTitle(dto.getTitle());
		newsdbContentDto.setTitle_abbr(dto.getTitle_abbr());
		newsdbContentDto.setType(dto.getType());
		
		if(dto.getClassifications() != null && dto.getClassifications().size()>0) {
			newsdbContentDto.setClassification(dto.getClassifications().get(0));
		}
		
		return newsdbContentDto;
	}
	
	/**
	 * buildLog:(构建操作日志)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年7月19日		下午9:15:35
	 * @param message
	 * @param businessType
	 * @param businessName
	 * @param user
	 * @param type
	 * @param source
	 * @param log_level
	 * @param detail
	 * @return   
	 * @return LogDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static LogDto buildOperationLog(String message,String businessType,String businessName,String user,String type,String source,String log_level,Map<String,Object> detail) {
		Map<String,Object> operationLog = buildOperationLog(message, businessType, businessName, user, detail);
		LogDto log = convertLog(type, source, log_level, operationLog);
		return log;
	}
	
	/**
	 * buildSystemLog:(构建系统日志)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年7月19日		下午9:20:18
	 * @param message
	 * @param businessType
	 * @param businessName
	 * @param businessId
	 * @param user
	 * @param type
	 * @param source
	 * @param log_level
	 * @param detail
	 * @return   
	 * @return LogDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static LogDto buildSystemLog(String message,String businessType,String businessName,String businessId,String user,String type,String source,String log_level,Map<String,Object> detail) {
		Map<String,Object> systemLog = buildSystemLog(message, businessType, businessId, businessName, user, detail);
		LogDto log = convertLog(type, source, log_level, systemLog);
		return log;
	}
	
	/**
	 * buildOperationLog:(构建操作日志)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年7月19日		下午8:38:35
	 * @param message
	 * @param businessType
	 * @param businessName
	 * @param user
	 * @param detail
	 * @return   
	 * @return Map<String,Object>    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Map<String,Object> buildOperationLog(String message,String businessType,String businessName,String user,Map<String,Object> detail){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("message",message );
		map.put("business_type", businessType);
		map.put("business_name", businessName);
		map.put("user", user);
		map.put("detail", detail);
		return map;
	}
	
	/**
	 * buildSystemLog:(构建系统日志)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年7月19日		下午8:38:47
	 * @param message
	 * @param businessType
	 * @param businessId
	 * @param businessName
	 * @param user
	 * @param detail
	 * @return   
	 * @return Map<String,Object>    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Map<String,Object> buildSystemLog(String message,String businessType,String businessId,String businessName,String user,Map<String,Object> detail){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("message",message );
		map.put("business_type", businessType);
		map.put("business_name", businessName);
		map.put("business_id", businessId);
		map.put("user", user);
		map.put("detail", detail);
		return map;
	}
	
	/**
	 * convertLog:(构建日志信息)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年7月19日		下午8:30:50
	 * @param type
	 * @param source
	 * @param log_level
	 * @param content
	 * @return   
	 * @return LogDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static LogDto convertLog(String type,String source,String log_level,Map<String,Object> content) {
		LogDto log = new LogDto();
		log.setType(type);
		log.setSource(source);
		log.setLog_level(log_level);
		log.setInserted_at(DateUtil.getNowTime());
		log.setContent(content);
		return log;
	}
	
	/**
	 * convertStationContentPosterMapping:(构建站点内容海报关系dto)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月31日		下午5:57:05
	 * @param elementId
	 * @param elementType
	 * @param parentId
	 * @param parentType
	 * @return   
	 * @return StationContentPosterMappingDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static StationContentPosterMappingDto convertStationContentPosterMapping(String elementId,String elementType,String parentId,String parentType) {
		StationContentPosterMappingDto dto =new StationContentPosterMappingDto();
		dto.setAction(Action.UPDATE.getValue());
		dto.setElement_id(elementId);
		dto.setParent_id(parentId);
		dto.setParent_type(parentType);
		dto.setElement_type(elementType);
		return dto;
	}
	
	/**
	 * convertStationContentMediaMapping:(构建站点内容媒体关系dto)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月31日		下午5:56:48
	 * @param elementId
	 * @param elementType
	 * @param parentId
	 * @param parentType
	 * @return   
	 * @return StationContentMediaMappingDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static StationContentMediaMappingDto convertStationContentMediaMapping(String elementId,String elementType,String parentId,String parentType) {
		StationContentMediaMappingDto dto = new StationContentMediaMappingDto();
		dto.setAction(Action.UPDATE.getValue());
		dto.setElement_id(elementId);
		dto.setParent_id(parentId);
		dto.setParent_type(parentType);
		dto.setElement_type(elementType);
		return dto;
	}
	
	/**
	 * convertMappingDto:(构建站点分类内容关系dto)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月31日		下午5:44:26
	 * @param elementId
	 * @param elementType
	 * @param parentId
	 * @param parentType
	 * @param sequence
	 * @param recommend
	 * @return   
	 * @return StationClassificationContentMappingDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static StationClassificationContentMappingDto convertStationClassificationContentMapping(String elementId,String elementType,String parentId,String parentType,int sequence,int recommend) {
		StationClassificationContentMappingDto dto =new StationClassificationContentMappingDto();
		dto.setAction(Action.UPDATE.getValue());
		dto.setElement_id(elementId);
		dto.setParent_id(parentId);
		dto.setParent_type(parentType);
		dto.setSequence(sequence);
		dto.setRecommend(recommend);
		dto.setElement_type(elementType);
		return dto;
	}
	
	/**
	 * convertStationImage:(构建站点海报dto)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月31日		下午5:24:40
	 * @param p
	 * @return   
	 * @return StationImageDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static StationImageDto convertStationImage(Poster p,int position) {
		StationImageDto dto =new StationImageDto();
		if(null != p) {
			dto.setAction(Action.UPDATE.getValue());
			dto.setCheck_sum(p.getCheckSum());
			dto.setDescription(p.getDescription());
			dto.setElement_type(ElementType.POSTER.getValue());
			dto.setHeight(p.getHeight());
			dto.setId(p.getId().toString());
			dto.setPosition(position);
			dto.setSize(p.getSize());
			dto.setStatus(p.getStatus());
			dto.setUrl(p.getUrl());
			dto.setSourceUrl(p.getSourceUrl());
			dto.setWidth(p.getWidth());
			dto.setFile_name(p.getFileName());
		}
		return dto;
	}
	
	/**
	 * convertStationMedia:(构建站点媒体dto)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月31日		下午5:23:28
	 * @param m
	 * @return   
	 * @return StationMediaDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static StationMediaDto convertStationMedia(Media m) {
		StationMediaDto dto =new StationMediaDto();
		dto.setAction(Action.UPDATE.getValue());
		dto.setCheck_sum(m.getCheckSum());
		dto.setElement_type(ElementType.MEDIA.getValue());
		dto.setId(m.getId().toString());
		dto.setSize(m.getSize());
		dto.setStatus(m.getStatus());
		dto.setType(m.getType());
		dto.setUrl_key(m.getUrlKey());
		dto.setFile_name(m.getFileName());
		dto.setDuration(m.getDuration());
		dto.setBitrate(m.getBitrate());
		dto.setWidth(m.getWidth());
		dto.setHeight(m.getHeight());
		return dto;
	}
	
	/**
	 * StationProgram:(构建站点注入内容dto)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月31日		下午5:13:05
	 * @param ct
	 * @return   
	 * @return StationProgramDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static StationProgramDto convertStationProgram(Content ct) {
		StationProgramDto dto =new StationProgramDto();
		dto.setAction(Action.UPDATE.getValue());
		dto.setAsset_id(ct.getAssetId());
		dto.setDescription(ct.getDescription());
		dto.setElement_type(ElementType.PROGRAM.getValue());
		dto.setId(ct.getId().toString());
		dto.setLast_shelves_time(DateUtil.getDate(ct.getLastShelvesTime()));
		dto.setPublish_time(DateUtil.getDate(ct.getPublishTime()));
		dto.setStatus(ct.getStatus());
		dto.setTitle(ct.getTitle());
		dto.setTitle_abbr(ct.getTitleAbbr());
		dto.setCp(ct.getCp());
		dto.setType(ct.getType());
		dto.setSource(ct.getSource());
		return dto;
	}
	
	/**
	 * convertClassificationForStation:(构建站点分类dto)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月31日		下午4:29:41
	 * @param cf
	 * @return   
	 * @return ObjectDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static StationClassificationDto convertStationClassification(Classification cf) {
		StationClassificationDto dto = new StationClassificationDto();
		dto.setAction(Action.UPDATE.getValue());
		dto.setElement_type(ElementType.CLASSIFICATION.getValue());
		dto.setId(cf.getId());
		dto.setDescription(cf.getDescription());
		dto.setIcon(cf.getIcon());
		dto.setLevel(cf.getLevel());
		dto.setName(cf.getName());
		dto.setSequence(cf.getSequence());
		dto.setAlias(cf.getAlias());
		dto.setParent(cf.getParent());
		dto.setStatus(cf.getStatus().getValue());
		dto.setType(cf.getType().getValue());
		return dto;
	}
	
	
	/**
	 * convertClassification:(构建WEB内容详情分类dto)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月31日		下午6:30:59
	 * @param classification
	 * @param hierarchyName   分类层级,如  根/a/b
	 * @return    
	 * @return Map<String,Object>    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Map<String,Object> convertClassification(Classification classification,String hierarchyName) {
		Map<String,Object> classif =new HashMap<String,Object>();
		classif.put("id", classification.getId());
		classif.put("name", classification.getName());
		classif.put("icon", classification.getIcon());
		classif.put("description", classification.getDescription());
		classif.put("hierarchyName", hierarchyName);  //分类层级,如  根/a/b
		return classif;
	}
	
	/**
	 * convertContent:(构建编辑内容实体)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月30日		下午6:28:04
	 * @param content
	 * @param dto   
	 * @return void    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static void convertContent(Content content,ContentDto dto) {
		content.setTitle(dto.getTitle());
		content.setTitleAbbr(dto.getTitleAbbr());
		content.setTags(dto.getTags());
		content.setDescription(dto.getDescription());
		content.setPublishTime(DateUtil.getDate(dto.getPublishTime()));
		content.setUpdatedAt(DateUtil.getNowTimeStamp());
	}
	
	/**
	 * convertContent:(构建内容dto)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月30日		下午5:54:57
	 * @param content
	 * @return   
	 * @return ContentDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static ContentDto convertContent(Content content) {
		ContentDto dto =new ContentDto();
		dto.setAssetId(content.getAssetId());
		dto.setCp(content.getCp());
		dto.setDescription(content.getDescription());
		dto.setId(content.getId());
		dto.setInsertedAt(DateUtil.getDate(content.getInsertedAt()));
		dto.setLastShelvesTime(DateUtil.getDate(content.getLastShelvesTime()));
		dto.setPublishTime(DateUtil.getDate(content.getPublishTime()));
		dto.setSource(content.getSource());
		dto.setStatus(content.getStatus());
		dto.setCdnSyncStatus(content.getCdn_sync_status());
		dto.setTags(content.getTags());
		dto.setTitle(content.getTitle());
		dto.setTitleAbbr(content.getTitleAbbr());
		dto.setType(content.getType());
		dto.setUpdatedAt(DateUtil.getDate(content.getUpdatedAt()));
		dto.setSourceChannel(content.getSourceChannel() != null?content.getSourceChannel():"无");
		dto.setSourceColumn(content.getSourceColumn() != null?content.getSourceColumn():"无");
		
		List<AdiImageDto> images  = new ArrayList<AdiImageDto>();
		if(content.getContentPosterMappings() != null) {
			Set<ContentPosterMapping> mappings = content.getContentPosterMappings();
			for(ContentPosterMapping mapping : mappings) {
				images.add(ConvertDto.convertPoster(mapping.getPoster()));
			}
		}
		dto.setImages(images);
		
		List<AdiMovieDto> movies  = new ArrayList<AdiMovieDto>();
		if(content.getMedias() != null) {
			List<Media> medias = content.getMedias();
			for(Media m : medias) {
				movies.add(ConvertDto.convertMedia(m));
				dto.setDuration(m.getDuration());
			}
		}
		dto.setMovies(movies);
		return dto;
	}
	
	/**
	 * convertContent:(构建内容dto)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月30日		下午5:54:57
	 * @param content
	 * @return   
	 * @return ContentDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static ContentDto convertContentN(Content content) {
		ContentDto dto =new ContentDto();
		dto.setAssetId(content.getAssetId());
		dto.setCp(content.getCp());
		dto.setDescription(content.getDescription());
		dto.setId(content.getId());
		dto.setInsertedAt(DateUtil.getDate(content.getInsertedAt()));
		dto.setLastShelvesTime(DateUtil.getDate(content.getLastShelvesTime()));
		dto.setPublishTime(DateUtil.getDate(content.getPublishTime()));
		dto.setSource(content.getSource());
		dto.setStatus(content.getStatus());
		dto.setTags(content.getTags());
		dto.setTitle(content.getTitle());
		dto.setTitleAbbr(content.getTitleAbbr());
		dto.setType(content.getType());
		dto.setUpdatedAt(DateUtil.getDate(content.getUpdatedAt()));
		
		List<PosterDto> images  = new ArrayList<PosterDto>();
		if(content.getContentPosterMappings() != null) {
			Set<ContentPosterMapping> mappings = content.getContentPosterMappings();
			for(ContentPosterMapping mapping : mappings) {
				images.add(ConvertDto.convertPosterN(mapping.getPoster()));
			}
		}
		dto.setImagesN(images);
		
		List<AdiMovieDto> movies  = new ArrayList<AdiMovieDto>();
		if(content.getMedias() != null) {
			List<Media> medias = content.getMedias();
			for(Media m : medias) {
				movies.add(ConvertDto.convertMedia(m));
			}
		}
		dto.setMovies(movies);
		return dto;
	}
	
	/**
	 * convertPage:(datatables构建分页条件)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月30日		下午2:15:20
	 * @param data
	 * @return   
	 * @return Page    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Page convertPage(String data){
		Page page=new Page();
		if(null != data && 0 != data.length()){
			List<DataDto> datas=(List<DataDto>) JsonConverter.asList(data, DataDto.class);
			int size=0;
			int offSet=0;
			for(DataDto ao : datas){
				if(ao.getName().equals("iDisplayLength")){
					size=Integer.parseInt(ao.getValue());
				}
				if(ao.getName().equals("iDisplayStart")){
					offSet=Integer.parseInt(ao.getValue());
				}
			}
			page.setSize(size);
			page.setOffSet(offSet);
		}
		
		return page;
	}
	
	/**
	 * convertDataTableSearchForStation:(站点页面检索条件)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月30日		下午2:14:17
	 * @param data
	 * @return   
	 * @return Map<String,Object>    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Map<String,Object> convertDataTableSearchForStation(String data){
		Map<String,Object> q=new HashMap<String,Object>();
		if(null != data && 0 != data.length()){
			List<DataDto> datas=(List<DataDto>) JsonConverter.asList(data, DataDto.class);
			String name="";
			String iSortColNumb = getISortColNumb(datas);
			String order = "";
			String sort = "";
			for(DataDto ao : datas){
				if(StringUtils.isNotBlank(iSortColNumb)) {
					if(ao.getName().contains("mDataProp_"+iSortColNumb)) {
						order = ao.getValue();
						continue;
					}
				}
				if(ao.getName().contains("sSortDir_0")) {
					sort = ao.getValue();
					continue;
				}
				if(ao.getName().equals("name")){
					name=ao.getValue();
					continue;
				}
				
			}
			if(!name.equals("")){
				q.put("name", name);
			}
			if(!order.equals("")){
				q.put("order", order);
			}
			if(!sort.equals("")){
				q.put("sort", sort);
			}
		}
		return q;
	}
	
	/**
	 * convertDataTableSearchForSynchronous:(同步管理检索条件)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年6月6日		下午4:49:57
	 * @param data
	 * @return   
	 * @return Map<String,Object>    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Map<String,Object> convertDataTableSearchForSynchronous(String data){
		Map<String,Object> q=new HashMap<String,Object>();
		if(null != data && 0 != data.length()){
			List<DataDto> datas=(List<DataDto>) JsonConverter.asList(data, DataDto.class);
			String iSortColNumb = getISortColNumb(datas);
			String order = "";
			String sort = "";
			for(DataDto ao : datas){
				if(StringUtils.isNotBlank(iSortColNumb)) {
					if(ao.getName().contains("mDataProp_"+iSortColNumb)) {
						order = ao.getValue();
						continue;
					}
				}
				if(ao.getName().contains("sSortDir_0")) {
					sort = ao.getValue();
					continue;
				}
				
			}
			if(!order.equals("")){
				q.put("order", order);
			}
			if(!sort.equals("")){
				q.put("sort", sort);
			}
		}
		return q;
	}
	
	/**
	 * getISortColNumb:(获取datatable 排序字段列数)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年6月6日		上午10:31:08
	 * @param datas
	 * @return   
	 * @return int    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	private static String getISortColNumb(List<DataDto> datas) {
		String iSortColNumb = "";
		for(DataDto ao : datas){
			if(ao.getName().contains("iSortCol_0")) {
				iSortColNumb = ao.getValue();
				break;
			}
		}
		return iSortColNumb;
	}
	
	/**
	 * convertDataTableSearchForContent:(内容管理检索条件)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月30日		下午5:45:00
	 * @param data
	 * @return   
	 * @return Map<String,Object>    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Map<String,Object> convertDataTableSearchForContent(String data){
		Map<String,Object> q=new HashMap<String,Object>();
		if(null != data && 0 != data.length()){
			List<DataDto> datas=(List<DataDto>) JsonConverter.asList(data, DataDto.class);
			String title="";
			String status="";
			String sourceChannel="";
			String sourceColumn="";
			String publishTimeBegin="";
			String publishTimeEnd="";
			String insertedBegin = "";
			String insertedEnd = "";
			String classid = "";
			String iSortColNumb = getISortColNumb(datas);
			String order = "";
			String sort = "";
			for(DataDto ao : datas){
				if(ao.getName().equals("title")){
					title=ao.getValue();
				}
				if(ao.getName().equals("status")){
					status=ao.getValue();
				}
				if(ao.getName().equals("sourceChannel")){
					sourceChannel=ao.getValue();
				}
				if(ao.getName().equals("sourceColumn")){
					sourceColumn=ao.getValue();
				}
				if(ao.getName().equals("publishTimeBegin")){
					publishTimeBegin=ao.getValue();
				}
				if(ao.getName().equals("publishTimeEnd")){
					publishTimeEnd=ao.getValue();
				}
				if(ao.getName().equals("insertedBegin")){
					insertedBegin=ao.getValue();
				}
				if(ao.getName().equals("insertedEnd")){
					insertedEnd=ao.getValue();
				}
				if (ao.getName().equals("classid")) {
					classid = ao.getValue();
				}
				if(StringUtils.isNotBlank(iSortColNumb)) {
					if(ao.getName().contains("mDataProp_"+iSortColNumb)) {
						order = ao.getValue();
						continue;
					}
				}
				if(ao.getName().contains("sSortDir_0")) {
					sort = ao.getValue();
					continue;
				}
			}
			if(!title.equals("")){
				q.put("title", title);
			}
			if(!status.equals("")){
				q.put("status", status);
			}
			if(!sourceChannel.equals("")){
				q.put("sourceChannel", sourceChannel);
			}
			if(!sourceColumn.equals("")){
				q.put("sourceColumn", sourceColumn);
			}
			if(!publishTimeBegin.equals("")){
				q.put("publishTimeBegin", publishTimeBegin);
			}
			if(!publishTimeEnd.equals("")){
				q.put("publishTimeEnd", publishTimeEnd);
			}
			if(!insertedBegin.equals("")){
				q.put("insertedBegin", insertedBegin);
			}
			if(!insertedEnd.equals("")){
				q.put("insertedEnd", insertedEnd);
			}
			if (!classid.equals("")) {
				q.put("classid", classid);
			}
			if(StringUtils.isNotBlank(order)){
				q.put("order", order);
			}
			if(!sort.equals("")){
				q.put("sort", sort);
			}
		}
		return q;
	}
	
	/**
	 * convertDataTableSearchForTaskStatistics:(任务统计页面查询条件拼接)
	 * @author   meishen
	 * @Date	 2018	2018年10月26日		下午1:42:36 
	 * @return Map<String,Object>    
	 * @throws
	 */
	public static Map<String,Object> convertDataTableSearchForTaskStatistics(String data){
		Map<String,Object> q=new HashMap<String,Object>();
		if(null != data && 0 != data.length()){
			List<DataDto> datas=(List<DataDto>) JsonConverter.asList(data, DataDto.class);
			String insertedBegin = "";
			String insertedEnd = "";
			String iSortColNumb = getISortColNumb(datas);
			String order = "";
			String sort = "";
			for(DataDto ao : datas){
				if(ao.getName().equals("insertedBegin")){
					insertedBegin=ao.getValue();
				}
				if(ao.getName().equals("insertedEnd")){
					insertedEnd=ao.getValue();
				}
				if(StringUtils.isNotBlank(iSortColNumb)) {
					if(ao.getName().contains("mDataProp_"+iSortColNumb)) {
						order = ao.getValue();
						continue;
					}
				}
				if(ao.getName().contains("sSortDir_0")) {
					sort = ao.getValue();
					continue;
				}
			}
			if(!insertedBegin.equals("")){
				q.put("insertedBegin", insertedBegin);
			}
			if(!insertedEnd.equals("")){
				q.put("insertedEnd", insertedEnd);
			}
			if(StringUtils.isNotBlank(order)){
				q.put("order", order);
			}
			if(!sort.equals("")){
				q.put("sort", sort);
			}
		}
		return q;
	}
	
	/**
	 * convertSource:(来源添加实体)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月29日		下午5:02:58
	 * @param key
	 * @param name
	 * @return   
	 * @return Source    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Source convertSource(String key,String name) {
		Source source =new Source();
		source.setInsertedAt(DateUtil.getNowTimeStamp());
		source.setKey(key);
		source.setName(name);
		source.setUpdatedAt(DateUtil.getNowTimeStamp());
		return source;
	}
	
	/**
	 * convertContentPosterMapping:(内容海报关联添加实体)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月29日		下午5:01:29
	 * @param content
	 * @param poster
	 * @param position
	 * @return   
	 * @return ContentPosterMapping    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static ContentPosterMapping convertContentPosterMapping(Content content,Poster poster,int position) {
		ContentPosterMapping contentPosterMapping =new ContentPosterMapping();
		contentPosterMapping.setContent(content);
		contentPosterMapping.setPosition(position);
		contentPosterMapping.setPoster(poster);
		return contentPosterMapping;
	}
	
	/**
	 * convertMedia:(构建媒体添加实体)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月29日		下午2:29:55
	 * @param dto
	 * @return   
	 * @return Media    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Media convertMedia(AdiMovieDto dto,Content content) {
		Media media =new Media();
		media.setCheckSum(dto.getCheck_sum());
		media.setContent(content);
		media.setInsertedAt(DateUtil.getNowTimeStamp());
		media.setSize(dto.getSize());
		media.setStatus(Status.VALID.getValue());
		media.setType(dto.getType());
		media.setUpdatedAt(DateUtil.getNowTimeStamp());
		media.setUrlKey(dto.getUrl());
		media.setFileName(dto.getFile_name());
		media.setDuration(dto.getDuration());
		media.setBitrate(dto.getBitrate());
		media.setWidth(dto.getWidth());
		media.setHeight(dto.getHeight());
		media.setCdn_sync_status(CdnStatus.UNSYNCHRONIZED.getValue());
		return media;
	}
	
	/**
	 * convertPoster:(构建海报添加实体)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月29日		下午2:28:49
	 * @param dto
	 * @return   
	 * @return Poster    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Poster convertPoster(AdiImageDto dto,String source) {
		Poster poster =new Poster();
		poster.setCheckSum(dto.getCheck_sum());
		poster.setDescription("");
		poster.setHeight(dto.getHeight());
		poster.setInsertedAt(DateUtil.getNowTimeStamp());
		poster.setSize(dto.getSize());
		poster.setSource(source);
		poster.setStatus(Status.VALID.getValue());
		poster.setUpdatedAt(DateUtil.getNowTimeStamp());
		poster.setSourceUrl(dto.getUrl());
		poster.setWidth(dto.getWidth());
		poster.setFileName(dto.getFile_name());
		poster.setCdnSyncStatus(CdnStatus.UNSYNCHRONIZED.getValue());
		return poster;
	}
	
	/**
	 * convertStorageRequestDto:(构造存储请求参数)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年6月19日		下午2:18:32
	 * @param m
	 * @return   
	 * @return StorageRequestDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static StorageRequestDto convertStorageRequestDto(Media m) {
		StorageRequestDto dto = new StorageRequestDto();
		dto.setCheck_sum(m.getCheckSum());
		dto.setId(m.getId());
		dto.setType(ContentType.VIDEO.getValue());
		dto.setUrl(m.getUrlKey());
		return dto;
	}
	
	/**
	 * convertStorageRequestDto:(构造存储请求参数)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年6月19日		下午2:18:52
	 * @param p
	 * @return   
	 * @return StorageRequestDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static StorageRequestDto convertStorageRequestDto(Poster p) {
		StorageRequestDto dto = new StorageRequestDto();
		dto.setCheck_sum(p.getCheckSum());
		dto.setId(p.getId());
		dto.setType(ContentType.POSTER.getValue());
		dto.setUrl(p.getSourceUrl());
		return dto;
	}
	
	/**
	 * convertPoster:(构建页面显示海报dto)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月30日		下午8:24:50
	 * @param poster
	 * @return   
	 * @return AdiImageDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static AdiImageDto convertPoster(Poster poster) {
		AdiImageDto dto =new AdiImageDto();
		if(poster != null) {
			dto.setCheck_sum(poster.getCheckSum());
			dto.setHeight(poster.getHeight());
			dto.setSize(poster.getSize());
			dto.setUrl(poster.getUrl());
			dto.setWidth(poster.getWidth());
			//dto.setId(poster.getId());
		}
		return dto;
	}
	
	/**
	 * convertPoster:(构建页面显示海报dto)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月30日		下午8:24:50
	 * @param poster
	 * @return   
	 * @return AdiImageDto    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static PosterDto convertPosterN(Poster poster) {
		PosterDto dto = new PosterDto();
		dto.setCheck_sum(poster.getCheckSum());
		dto.setHeight(poster.getHeight());
		DecimalFormat df = new DecimalFormat("0.00");
		String number = df.format((float) poster.getSize() / 1024);
		dto.setSize(number + "kb");
		dto.setUrl(poster.getSourceUrl());
		dto.setWidth(poster.getWidth());
		dto.setId(poster.getId());
		String description = "";
		if (poster.getDescription() != null) {
			description = poster.getDescription();
		}
		dto.setDescription(description);
		return dto;
	}
	
	public static AdiMovieDto convertMedia(Media m) {
		AdiMovieDto dto =new AdiMovieDto();
		dto.setCheck_sum(m.getCheckSum());
		dto.setFile_name(m.getFileName());
		dto.setSize(m.getSize());
		dto.setType(m.getType());
		dto.setUrl(m.getUrlKey());
		dto.setDuration(m.getDuration());
		dto.setBitrate(m.getBitrate());
		dto.setWidth(m.getWidth());
		dto.setHeight(m.getHeight());
		return dto;
	}
	
	/**
	 * convertContent:(构建添加内容实体)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月29日		下午2:06:23
	 * @param dto
	 * @return   
	 * @return Content    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Content convertContent(AdiContentDto dto) {
		Content content =new Content();
		content.setAssetId(dto.getAsset_id());
		content.setCp(dto.getCp());
		content.setDescription(dto.getDescription());
		content.setInsertedAt(DateUtil.getNowTimeStamp());
		content.setLastShelvesTime(DateUtil.getNowTimeStamp());
		content.setPublishTime(DateUtil.getDate(dto.getPublish_time()));
		content.setSource(dto.getSource());
		content.setStatus(ContentStatus.UNSHELVE.getValue());
//		content.setTags(JsonConverter.format(dto.getTags()));
		content.setTitleAbbr(dto.getTitle_abbr());
		content.setTitle(dto.getTitle());
		content.setType(dto.getType());
		content.setUpdatedAt(DateUtil.getNowTimeStamp());
		content.setSourceChannel(dto.getSource_channel());
		content.setSourceColumn(dto.getSource_column());
		content.setCdn_sync_status(CdnStatus.UNSYNCHRONIZED.getValue());
		content.setPlayCount(0);
		return content;
	}
	
	/**
	 * buildRestResponse:(公有响应状态)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月29日		上午11:24:56
	 * @param status
	 * @param message
	 * @param data
	 * @return   
	 * @return RestResponse    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Map<String,Object> buildRestResponse(int status,String message,List<FieldError> data) {
		Map<String, Object> result =new HashMap<String,Object>();
		if(null != data)
			result.put("data", buildBindingResult(data));
		else
			result.put("data", data);
		result.put("statusCode",status);
		result.put("message",message);
		return result;
	}

	/**
	 * buildRestResponse:(构建rest响应参数)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月29日		下午1:35:58
	 * @param status
	 * @param message
	 * @param data
	 * @return   
	 * @return Map<String,Object>    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Map<String,Object> buildRestResponse(int status,String message,Object data) {
		Map<String, Object> result =new HashMap<String,Object>();
		result.put("data", data);
		result.put("statusCode",status);
		result.put("message",message);
		return result;
	}
	
	/**
	 * buildBindingResult:(验证BindingResult 错误参数)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月29日		下午1:35:19
	 * @param data
	 * @return   
	 * @return List<Object>    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static List<Object> buildBindingResult(List<FieldError> data){
		List<Object> errors = new ArrayList<Object>();
		for(FieldError error : data) {
			Map<String,Object> er = new HashMap<String,Object>();
			er.put(error.getObjectName()+"."+error.getField(), error.getDefaultMessage());
			errors.add(er);
		}
		return errors;
	}
	
	/**
	 * validatorListForImages:(验证adi海报参数)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月29日		下午1:34:15
	 * @param adiImageDtos
	 * @return
	 * @throws Exception   
	 * @return List<Object>    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static List<Object> validatorListForImages(List<AdiImageDto> adiImageDtos) throws Exception{
		List<Object> list=new ArrayList<Object>();
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		for(AdiImageDto image : adiImageDtos){
			Set<ConstraintViolation<AdiImageDto>> constraintViolations = validator.validate(image);
			for (ConstraintViolation<AdiImageDto> constraintViolation : constraintViolations) {  
				Map<String,String> map=new HashMap<String,String>();
	            map.put("adiImageDto."+constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage().toString());
	            list.add(map);
	        }    
		}
		return list;
	}
	
	/**
	 * validatorListForMovies:(验证adi视频参数)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月29日		下午1:34:39
	 * @param adiMovieDtos
	 * @return
	 * @throws Exception   
	 * @return List<Object>    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static List<Object> validatorListForMovies(List<AdiMovieDto> adiMovieDtos) throws Exception{
		List<Object> list=new ArrayList<Object>();
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		for(AdiMovieDto movie : adiMovieDtos){
			Set<ConstraintViolation<AdiMovieDto>> constraintViolations = validator.validate(movie);
			for (ConstraintViolation<AdiMovieDto> constraintViolation : constraintViolations) {  
				Map<String,String> map=new HashMap<String,String>();
	            map.put("adiMovieDto."+constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage().toString());
	            list.add(map);
	        }    
		}
		return list;
	}
	
	public static ClassificationContentMappingDto convertClassificationContent(
			ClassificationContentMapping cfcm) {
		ClassificationContentMappingDto dto = new ClassificationContentMappingDto();
		dto.setId(cfcm.getId());// id
		dto.setCid(cfcm.getContent().getId());// 内容id
		dto.setClassId(cfcm.getClassification().getId());// 分类id
		dto.setTitle(cfcm.getContent().getTitle());// 标题
		dto.setPublishTime(DateUtil.formatYMD.format(cfcm.getContent()
				.getPublishTime()));// 播放日期
		dto.setSource(cfcm.getContent().getSource());// 来源
		dto.setSourceChannel(cfcm.getContent().getSourceChannel() != null?cfcm.getContent().getSourceChannel():"无");
		dto.setSourceColumn(cfcm.getContent().getSourceColumn() != null?cfcm.getContent().getSourceColumn():"无");
		dto.setLastShelvesTime(DateUtil.formatYMD.format(cfcm.getContent()
				.getLastShelvesTime()));// 最后上架时间
		dto.setStatus(cfcm.getContent().getStatus());// 状态
		dto.setCdnSyncStatus(cfcm.getContent().getCdn_sync_status());//cdn状态
		dto.setUpdatedAt(DateUtil.formatYMD.format(cfcm.getContent()
				.getUpdatedAt()));// 更新时间
		dto.setInsertedAt(DateUtil.format.format(cfcm.getContent()
				.getInsertedAt()));// 入站时间
		dto.setRecommend(cfcm.getRecommend());// 推荐
		
		//设置海报
		List<AdiImageDto> images  = new ArrayList<AdiImageDto>();
		if(cfcm.getContent().getContentPosterMappings() != null) {
			Set<ContentPosterMapping> mappings = cfcm.getContent().getContentPosterMappings();
			for(ContentPosterMapping mapping : mappings) {
				images.add(ConvertDto.convertPoster(mapping.getPoster()));
			}
		}
		dto.setImages(images);
		
		if(cfcm.getContent().getMedias() != null) {
			List<Media> medias = cfcm.getContent().getMedias();
			for(Media m : medias) {
				dto.setDuration(m.getDuration());
			}
		}
		
		return dto;
	}
	
	/**
	 * convertDataTableSearchForContent:(内容管理检索条件)
	 *
	 * @author whl
	 * @Date 2018 2018年5月31日 下午5:45:00
	 * @param data
	 * @return
	 * @return Map<String,Object>
	 * @throws @since
	 *             Videoworks Ver 1.1
	 */
	public static Map<String, Object> convertDataTableSearchForTask(String data) {
		Map<String, Object> q = new HashMap<String, Object>();
		if (null != data && 0 != data.length()) {
			List<DataDto> datas = (List<DataDto>) JsonConverter.asList(data, DataDto.class);
			String title = "";
			String publishTimeBegin = "";
			String publishTimeEnd = "";
			String insertedBegin = "";
			String insertedEnd = "";
			String status = "";
			for (DataDto ao : datas) {
				if (ao.getName().equals("title")) {
					title = ao.getValue();
				}
				if (ao.getName().equals("status")) {
					status = ao.getValue();
				}

				if (ao.getName().equals("publishTimeBegin")) {
					publishTimeBegin = ao.getValue();
				}
				if (ao.getName().equals("publishTimeEnd")) {
					publishTimeEnd = ao.getValue();
				}
				if (ao.getName().equals("insertedBegin")) {
					insertedBegin = ao.getValue();
				}
				if (ao.getName().equals("insertedEnd")) {
					insertedEnd = ao.getValue();
				}
			}
			if (!title.equals("")) {
				q.put("title", title);
			}
			if (!status.equals("")) {
				q.put("status", status);
			}
			if (!publishTimeBegin.equals("")) {
				q.put("publishTimeBegin", publishTimeBegin);
			}
			if (!publishTimeEnd.equals("")) {
				q.put("publishTimeEnd", publishTimeEnd);
			}
			if (!insertedBegin.equals("")) {
				q.put("insertedBegin", insertedBegin);
			}
			if (!insertedEnd.equals("")) {
				q.put("insertedEnd", insertedEnd);
			}
		}
		return q;
	}

	public static TaskDto convertTask(Task task) {
		TaskDto taskDto = new TaskDto();
		Content content = task.getContent();
		taskDto.setId(task.getId());
		taskDto.setTitle(content.getTitle());
		Date publishTime = content.getPublishTime();
		String publish_time = DateUtil.dateToString(publishTime);
		taskDto.setPublish_time(publish_time);
		Timestamp updated_at = task.getUpdated_at();
		if(updated_at!=null) {
			String cdn_back_time = DateUtil.timeStampConvertStr(updated_at);
			taskDto.setUpdated_at(cdn_back_time);
		}
		Date inserted_at = content.getInsertedAt();
		taskDto.setRuzhan_time(DateUtil.getDate(inserted_at));
		String ruzhan_time = DateUtil.dateToString(inserted_at);
		taskDto.setInserted_at(ruzhan_time);
		Integer status = task.getStatus();
		if (TaskStatus.ONGOING.getValue() == status) {
			taskDto.setStatus(TaskStatus.ONGOING.getName());
		} else if (TaskStatus.SUCCESS.getValue() == status) {
			taskDto.setStatus(TaskStatus.SUCCESS.getName());
		} else if (TaskStatus.ERROR.getValue() == status) {
			taskDto.setStatus(TaskStatus.ERROR.getName());
		}
		taskDto.setMessage(task.getMessage());
		return taskDto;
	}
	
	/**
	 * 分类的entity转换为分类的Dto
	 * 
	 * @author whl
	 * @param classification
	 * @return
	 */

	public static ApiClassificationDto convertClassificationDtoSingle(Classification classification) {
		ApiClassificationDto dto = new ApiClassificationDto();
		dto.setId(classification.getId());
		dto.setName(classification.getName());
		dto.setType(classification.getType().getValue());
		dto.setIcon(classification.getIcon());
		dto.setLevel(classification.getLevel());
		dto.setSequence(classification.getSequence());
		dto.setAlias(classification.getAlias());
		dto.setpId(classification.getParent());
		if(null == classification.getRecommend())
			dto.setRecommend(ClassificationRecommend.NOTRECOMMEND.getValue());
		else
			dto.setRecommend(classification.getRecommend().getValue());
		return dto;

	}
	
	
	/**
	 * 获取栏目树.有根节点
	 */
	public static List<ClassificationDto> buildFullTreeList(List<ClassificationDto> all) {
//		Map<String, Object> tree = null;
//		// 获取分类根节点
//		if (all != null && all.size() > 1) {
//			for (int i = 1; i < all.size(); i++) {
//				long lft = all.get(i).getLft();
//				for (int j = i - 1; j >= 0; j--) {
//					long lftn = all.get(j).getLft();
//					long rgtn = all.get(j).getRgt();
//					if (lft > lftn && lft < rgtn) {
//						all.get(j).getChildren().add(all.get(i));
//						all.get(i).setpId(all.get(j).getId());// 设置父id
//						break;
//					}
//				}
//			}
//		}
//		if (tree == null) {
//			tree = new HashMap<String, Object>();
//		}
//		// 设置根节点父id
//
//		List<ClassificationDto> dtos = new ArrayList<ClassificationDto>();
//		for (ClassificationDto classificationDto : all) {
//			String getpId = classificationDto.getpId();
//			if (getpId == null) {
//				dtos.add(classificationDto);
//			}
//		}
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
		List<ClassificationDto> dtos = new ArrayList<ClassificationDto>();
		if (all != null && all.size() > 0) {
			for (ClassificationDto d : all) {
				if (d.getpId().equals("0")) {
					dtos.add(d);
					break;
				}
			}
		}
		return dtos;
	}

	
	//api
	/**
	 * 将内容的entity转为内容的Dto
	 * 
	 * @author whl
	 * @param content
	 * @return
	 */
	public static ApiContentDto convertContentToContentDto(Content content) {

		ApiContentDto contentDto = new ApiContentDto();

		contentDto.setId(content.getId());
		contentDto.setAsset_id(content.getAssetId());
		contentDto.setTitle(content.getTitle());
		contentDto.setTitle_abbr(content.getTitleAbbr());
		contentDto.setType(content.getType());
		contentDto.setDescription(content.getDescription());
		Date publishTime = content.getPublishTime();
		String publish_time_str = DateUtil.dateTimeToString(publishTime);
		contentDto.setPublish_time(publish_time_str);
		contentDto.setSource(content.getSource());
		ArrayList<PosterDto> posterDtos = new ArrayList<PosterDto>();
		Set<ContentPosterMapping> posters = content.getContentPosterMappings();
		for (ContentPosterMapping contentPosterMapping : posters) {
			Poster poster = contentPosterMapping.getPoster();
			if(null != poster) {
				PosterDto posterDto = new PosterDto();
				posterDto.setId(poster.getId());
				posterDto.setUrl(poster.getUrl());
				posterDto.setWidth(poster.getWidth());
				posterDto.setHeight(poster.getHeight());
				posterDtos.add(posterDto);
			}
		}

		contentDto.setPosters(posterDtos);

		ArrayList<ApiMediaDto> mediaDtos = new ArrayList<ApiMediaDto>();

		List<Media> medias = content.getMedias();
		for (Media media : medias) {
			ApiMediaDto mediaDto = new ApiMediaDto();
			mediaDto.setId(media.getId());
			mediaDto.setCdn_key(media.getCdnKey());
			mediaDto.setInner_key(media.getInnerKey());
			mediaDto.setSize(media.getSize());
			mediaDto.setType(media.getType());
			mediaDto.setUrl_key(media.getUrlKey());
			mediaDto.setDuration(media.getDuration());
			mediaDto.setBitrate(media.getBitrate());
			mediaDto.setWidth(media.getWidth());
			mediaDto.setHeight(media.getHeight());
			mediaDtos.add(mediaDto);
			contentDto.setDuration(media.getDuration());//兼容老版本cimp的盒子显示
		}

		contentDto.setMedias(mediaDtos);
		return contentDto;
	}
	
	
	/**
	 * 获取栏目树.有根节点
	 */
	@SuppressWarnings("unused")
	public static List<ApiClassificationDto> buildFullTreeListApiWithRoot(List<ApiClassificationDto> all) {
		ApiClassificationDto tree = null;
		// 获取分类根节点
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
			for (ApiClassificationDto d : all) {
				if(d.getpId().equals("0")){
					tree = d;
					break;
				}
			}
		}
		
		// 设置根节点父id

		List<ApiClassificationDto> dtos = new ArrayList<ApiClassificationDto>();
		for (ApiClassificationDto classificationDto : all) {
			String getpId = classificationDto.getpId();
				if(getpId.equals("0")) {
					
					dtos.add(classificationDto);
				}
			
		}
		return dtos;
	}
	
	
	
	public static List<ApiClassificationDto> buildFullTreeListApiNoRoot(List<ApiClassificationDto> all,Classification classificationParent) {
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
		
		ArrayList<ApiClassificationDto> arrayList = new ArrayList<>();
		
		// 设置根节点父id
		if (all != null && all.size() > 0) {
			for (ApiClassificationDto d : all) {
				
				if(d.getpId().equals(classificationParent.getId())){
					arrayList.add(d);
				}
			}
		}
	// 设置根节点父id
	return arrayList;
}
	
	
	public static List<ApiClassificationDto> buildClassification(List<ApiClassificationDto> all) {
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
		
		
		
	return all;
}
	
	/**
	 * convert Content.entity to CDNJobDto
	 */

	public static CDNJobDto convertContentEntityToCDNJobDto(Content content) {
		CDNJobDto cdnJobDto = new CDNJobDto();
		cdnJobDto.setContentId(content.getId());
		cdnJobDto.setAsset_id(content.getAssetId());
		cdnJobDto.setTitle(content.getTitle());
		cdnJobDto.setTitle_abbr(content.getTitleAbbr());
		cdnJobDto.setType(content.getType());
		cdnJobDto.setDescription(content.getDescription());
		Date publishTime = content.getPublishTime();
		if(publishTime!=null) {
			
			String publishTimeStr = DateUtil.dateTimeToString((publishTime));
			cdnJobDto.setPublish_time(publishTimeStr);
		}
		String tags = content.getTags();
		if(StringUtils.isNotBlank(tags)) {
			String str[] = tags.split(",");
			List<String> tagsList = Arrays.asList(str);
			cdnJobDto.setTags(tagsList);
		}
		cdnJobDto.setCp(content.getCp());
		cdnJobDto.setSource(content.getSource());

		List<Media> medias = content.getMedias();

		ArrayList<CDNMovieDto> mediaDtos = new ArrayList<CDNMovieDto>();
		if(null != medias) {
			for (Media media : medias) {
				CDNMovieDto cdnMovieDto = new CDNMovieDto();
				cdnMovieDto.setCheck_sum(media.getCheckSum());
				cdnMovieDto.setUrl(media.getUrlKey());
				cdnMovieDto.setType(media.getType());
				cdnMovieDto.setSize(media.getSize());
				cdnMovieDto.setFilename(media.getFileName());
				cdnMovieDto.setId(media.getId());
				cdnMovieDto.setDuration(media.getDuration());
				cdnMovieDto.setBitrate(media.getBitrate());
				
				cdnMovieDto.setWidth(media.getWidth());
				
				cdnMovieDto.setHeight(media.getHeight());
				mediaDtos.add(cdnMovieDto);
			}
		}
		ArrayList<CDNImageDto> cdnImageDtos = new ArrayList<CDNImageDto>();
		Set<ContentPosterMapping> contentPosterMappings = content.getContentPosterMappings();
		if(null != contentPosterMappings) {
			for (ContentPosterMapping contentPosterMapping : contentPosterMappings) {
				Poster poster = contentPosterMapping.getPoster();
				CDNImageDto cdnImageDto = new CDNImageDto();
				cdnImageDto.setUrl(poster.getSourceUrl()); //给cdn worker 海报的地址是存储的地址，由cdn worker 去上传apache
				cdnImageDto.setWidth(poster.getWidth());
				cdnImageDto.setHeight(poster.getHeight());
				cdnImageDto.setSize(poster.getSize());
				cdnImageDto.setCheck_sum(poster.getCheckSum());
				cdnImageDto.setFilename(poster.getFileName());
				cdnImageDto.setId(poster.getId());
				cdnImageDtos.add(cdnImageDto);
				
			}
		}
		
		cdnJobDto.setMovies(mediaDtos);
		cdnJobDto.setImages(cdnImageDtos);
		
		return cdnJobDto;
	
	
	}

	public static UserDto convertUser(User user) {
		UserDto userDto = new UserDto();
		if(user!=null) {
			userDto.setId(user.getId());
			userDto.setUsername(user.getUsername());
			userDto.setNickname(user.getNickname());
			userDto.setStatus(user.getStatus());
			Date inserted_at = user.getInserted_at();
			String inserted_at_str = DateUtil.dateTimeToString(inserted_at);
			userDto.setInserted_at(inserted_at_str);
			
			Date updated_at = user.getUpdated_at();
			String updated_at_str = DateUtil.dateTimeToString(updated_at);
			userDto.setUpdated_at(updated_at_str);
			
			Set<UserRoleMapping> userRoleMappings = user.getUserRoleMappings();
			Iterator<UserRoleMapping> iterator = userRoleMappings.iterator();
			StringBuffer roleName = new StringBuffer();
			while(iterator.hasNext()) {
				UserRoleMapping next = iterator.next();
				Role role = next.getRole();
				if(role!=null) {
					
					String name = role.getName();
					roleName.append(name+", " );
				}
			}
			userDto.setRoleName(roleName.toString());
			Integer type = user.getType()==null?UserType.UNSUPER.getValue():user.getType();
			userDto.setType(type);
		}
		
		
		return userDto;
	}

	public static Map<String, Object> convertDataTableSearchForUser(String data) {
		Map<String, Object> q = new HashMap<String, Object>();
		if (null != data && 0 != data.length()) {
			List<DataDto> datas = (List<DataDto>) JsonConverter.asList(data, DataDto.class);
			String username = "";
			String nickname = "";
			String status = "";
			for (DataDto ao : datas) {
				if (ao.getName().equals("username")) {
					username = ao.getValue();
				}
				if (ao.getName().equals("status")) {
					status = ao.getValue();
				}
				if (ao.getName().equals("nickname")) {
					nickname = ao.getValue();
				}
			}
			if (!username.equals("")) {
				q.put("username", username);
			}
			if (!status.equals("")) {
				q.put("status", status);
			}
			
			if (!nickname.equals("")) {
				q.put("nickname", nickname);
			}
		}
		return q;
	}
	public static Map<String, Object> convertDataTableSearchForApk(String data) {
		Map<String, Object> q = new HashMap<String, Object>();
		if (null != data && 0 != data.length()) {
			List<DataDto> datas = (List<DataDto>) JsonConverter.asList(data, DataDto.class);
			String type = "";
			for (DataDto ao : datas) {
				
				if (ao.getName().equals("type")) {
					type = ao.getValue();
				}
				
			}
			if (!type.equals("")) {
				q.put("type", type);
			}
			
			
		}
		return q;
	}
	public static RoleDto convertRole(Role role) {
		RoleDto roleDto = new RoleDto();
		if(role!=null) {
			roleDto.setId(role.getId());
			roleDto.setName(role.getName());
			roleDto.setDescription(role.getDescription());
			Date inserted_at = role.getInserted_at();
			String dateTimeToString = DateUtil.dateTimeToString(inserted_at);
			roleDto.setInserted_at(dateTimeToString);
			
			Date updated_at = role.getUpdated_at();
			String dateTimeToString2 = DateUtil.dateTimeToString(updated_at);
			roleDto.setUpdated_at(dateTimeToString2);
			
			
		}
		return roleDto;
	}

	public static PermissionDto convertPermission(Permission permission) {
		PermissionDto permissionDto = new PermissionDto();
		String realHandleName = "";
		if(permission!=null) {
			permissionDto.setId(permission.getId());
			Integer type = permission.getType();
			switch (type) {
			case 1:
				permissionDto.setTypeName(PermissionType.MENU.getName());
				List<PermissionMenuMapping> permissionMenuMappings = permission.getPermissionMenuMappings();
				if(permissionMenuMappings!=null&&permissionMenuMappings.size()>0) {
					PermissionMenuMapping permissionMenuMapping = permissionMenuMappings.get(0);
					Menu menu = permissionMenuMapping.getMenu();
					realHandleName = menu.getName();
				}
				break;
			case 2:
				permissionDto.setTypeName(PermissionType.OPERATION.getName());
				List<PermissionOperatoinMapping> permissionOperationMappings = permission.getPermissionOperationMappings();
				if(permissionOperationMappings!=null && permissionOperationMappings.size()>0) {
					PermissionOperatoinMapping permissionOperatoinMapping = permissionOperationMappings.get(0);
					Operation operation = permissionOperatoinMapping.getOperation();
					realHandleName = operation.getName();
				}
				
				break;
			case 3:
				permissionDto.setTypeName(PermissionType.CLASSIFICATION.getName());
				break;
			default:
				break;
			}
			
		}
		permissionDto.setRealHandleName(realHandleName);
		
		return permissionDto;
	}

	public static OperationDto convertOperationToDto(Operation operation) {
		OperationDto operationDto = new OperationDto();
		operationDto.setChildren(new ArrayList<OperationDto>());
		operationDto.setParentTId(operation.getParent());
		operationDto.setId(operation.getId());
		operationDto.setName(operation.getName());
		operationDto.setCode(operation.getCode());
		operationDto.setUrl(operation.getUrl());
		List<PermissionOperatoinMapping> permissionOperationMappings = operation.getPermissionOperationMappings();
		if(permissionOperationMappings!=null&&permissionOperationMappings.size()>0) {
			PermissionOperatoinMapping permissionOperatoinMapping = permissionOperationMappings.get(0);
			Permission permisson = permissionOperatoinMapping.getPermisson();
			if( permisson!=null) {
				operationDto.setPermission(permisson.getId());
			}
		}
		return operationDto;
	}

	public static ApkDto convertApkVersion(ApkVersion apkVersion) {
		ApkDto apkDto = new ApkDto();
		apkDto.setId(apkVersion.getId());
		apkDto.setStatus(apkVersion.getStatus());
		apkDto.setType(apkVersion.getType());
		apkDto.setDescription(apkVersion.getDescription());
		apkDto.setMainVersion(apkVersion.getMainVersion());
		apkDto.setChildVersion(apkVersion.getChildVersion());
		apkDto.setIsForce(apkVersion.getIsForce());
		apkDto.setEntry(apkVersion.getEntry());
		apkDto.setMd5(apkVersion.getMd5());
		apkDto.setArchive(apkVersion.getArchvie());
		apkDto.setIsForce(apkVersion.getIsForce());
		apkDto.setSize(apkVersion.getSize());
		return apkDto;
	}

	public static ApiClassificationDto convertClassificationDtoSingle(Classification classification,
			List<Classification> cfs) {
		ApiClassificationDto dto = new ApiClassificationDto();
		dto.setId(classification.getId());
		dto.setName(classification.getName());
		dto.setType(classification.getType().getValue());
		dto.setIcon(classification.getIcon());
		dto.setLevel(classification.getLevel());
		dto.setSequence(classification.getSequence());
		dto.setAlias(classification.getAlias());
		dto.setpId(classification.getParent());
		//path
		String hierarchyName = "";
		if (cfs != null) {
			for (int i = cfs.size()-1; i >=0; i--) {
				if (i == cfs.size()-1)
					hierarchyName =hierarchyName+ cfs.get(i).getName();
				else
					hierarchyName =hierarchyName+"/"+ cfs.get(i).getName() ;
			}
			dto.setPath(hierarchyName);
			// 组装内容详情数据
		}
		
		return dto;
	}
	
	public static ApiClassificationDto convertClassificationDtoSingle(Classification classification,
			List<Classification> cfs,String areaValue) {
		ApiClassificationDto dto = new ApiClassificationDto();
		dto.setId(classification.getId());
		String name = classification.getName();
		dto.setName(name);
		dto.setType(classification.getType().getValue());
		dto.setIcon(classification.getIcon());
		dto.setLevel(classification.getLevel());
		dto.setSequence(classification.getSequence());
		dto.setAlias(classification.getAlias());
		dto.setpId(classification.getParent());
		if(null == classification.getRecommend())
			dto.setRecommend(ClassificationRecommend.NOTRECOMMEND.getValue());
		else
			dto.setRecommend(classification.getRecommend().getValue());
		//path
		if(areaValue!=null && name.trim().equals(areaValue.trim())) {
			dto.setPath(areaValue);
		}else {
			String hierarchyName = "";
			if (cfs != null) {
				for (int i = cfs.size()-1; i >=0; i--) {
					if (i == cfs.size()-1)
						hierarchyName =hierarchyName+ cfs.get(i).getName();
					else
						hierarchyName =hierarchyName+"/"+ cfs.get(i).getName() ;
				}
				dto.setPath(hierarchyName);
				// 组装内容详情数据
			}
			
		}
		
		return dto;
	}
}