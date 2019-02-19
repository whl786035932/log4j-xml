package cn.videoworks.cms.enumeration;

public enum Sort {

	ASC("asc","升序"),
	DESC("desc","降序");
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
	private Sort(String value, String name){
		this.value=value;
		this.name=name;
	}
}
