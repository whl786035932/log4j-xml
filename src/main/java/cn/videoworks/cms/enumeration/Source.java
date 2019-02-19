package cn.videoworks.cms.enumeration;

public enum Source {
	LOCAL("本地","本地");
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
	private Source(String value, String name){
		this.value=value;
		this.name=name;
	}
}
