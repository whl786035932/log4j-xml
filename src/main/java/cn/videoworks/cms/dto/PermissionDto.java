package cn.videoworks.cms.dto;

public class PermissionDto {
	private Integer id;
	private Integer type;
	private String typeName;
	//实际的权限操作名称，如某个菜单，某个操作，某个分类
	private String realHandleName;
	public String getTypeName() {
		return typeName;
	}
	public String getRealHandleName() {
		return realHandleName;
	}
	public void setRealHandleName(String realHandleName) {
		this.realHandleName = realHandleName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	

}
