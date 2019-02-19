package cn.videoworks.cms.dto;

import java.util.HashMap;
import java.util.Map;

import cn.videoworks.cms.util.DateUtil;

public class OperationConvertDto {

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
}
