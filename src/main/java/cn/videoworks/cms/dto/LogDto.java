package cn.videoworks.cms.dto;

import java.util.Map;

public class LogDto {

	/**
	 * 日志创建时间
	 */
	private String inserted_at;
	/**
	 * 日志类型 operation_log(操作日志)  system_log(系统日志)
	 */
	private String type;
	/**
	 * 日志级别  标准的日志级别格式
	 */
	private String log_level;
	/**
	 * 日志来源
	 */
	private String source;
	/**
	 * 日志具体信息
	 */
	private Map<String,Object> content;
	
	public String getInserted_at() {
		return inserted_at;
	}
	public void setInserted_at(String inserted_at) {
		this.inserted_at = inserted_at;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLog_level() {
		return log_level;
	}
	public void setLog_level(String log_level) {
		this.log_level = log_level;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Map<String, Object> getContent() {
		return content;
	}
	public void setContent(Map<String, Object> content) {
		this.content = content;
	}
	
	
}
