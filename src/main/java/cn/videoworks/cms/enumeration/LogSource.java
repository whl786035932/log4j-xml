package cn.videoworks.cms.enumeration;

public enum LogSource {
	VIDEOFACTORY("video_factory","视频工厂"),
	CMS("cms","内容管理系统"),
	BOOTHS("booths","站点"),
	STORAGEWORK("storage_work","存储work"),
	BAIDUCDNWORK("gzcdn_work","贵州cdn work"),
	GZCDNWORK("baiducdn_work","百度cdnwork");
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
	private LogSource(String value, String name){
		this.value=value;
		this.name=name;
	}
}
