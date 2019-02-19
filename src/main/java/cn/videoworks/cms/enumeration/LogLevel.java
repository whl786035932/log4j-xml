package cn.videoworks.cms.enumeration;

public enum LogLevel {
	INFO("INFO","指定能够突出在粗粒度级别的应用程序运行情况的信息的消息"),
	ALL("ALL","各级包括自定义级别"),
	TRACE("TRACE","指定细粒度比DEBUG更低的信息事件"),
	FATAL("FATAL","指定非常严重的错误事件，这可能导致应用程序中止"),
	OFF("OFF","这是最高等级，为了关闭日志记录"),
	ERROR("ERROR","错误事件可能仍然允许应用程序继续运行"),
	DEBUG("DEBUG","指定细粒度信息事件是最有用的应用程序调试"),
	WARN("WARN","指定具有潜在危害的情况");
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
	private LogLevel(String value, String name){
		this.value=value;
		this.name=name;
	}
}
