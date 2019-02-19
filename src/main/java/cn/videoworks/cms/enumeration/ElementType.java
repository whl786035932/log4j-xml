package cn.videoworks.cms.enumeration;

public enum ElementType {

	CLASSIFICATION("classification","分类"),
	PROGRAM("program","节目"),
	MEDIA("media","媒体"),
	POSTER("poster","海报");
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
	private ElementType(String value, String name){
		this.value=value;
		this.name=name;
	}
}
