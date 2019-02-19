package cn.videoworks.cms.enumeration;

public enum Action {

	UPDATE("update","修改/添加"),
	DELETE("update","删除");
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
	private Action(String value, String name){
		this.value=value;
		this.name=name;
	}
}
