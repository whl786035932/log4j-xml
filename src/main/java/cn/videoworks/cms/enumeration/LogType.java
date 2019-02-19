package cn.videoworks.cms.enumeration;

public enum LogType {

	OPERATIONLOG("operation_log","操作日志"),
	SYSTEMLOG("system_log","系统日志");
	private String value;
	private String name;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private LogType(String value, String name){
		this.value=value;
		this.name=name;
	}
}
