package cn.videoworks.cms.dto;

import java.util.List;

public class OperationDto {
	private Integer id;
	private List<OperationDto> children;
	private String name;
	private Integer parentTId;
	private Boolean nocheck;
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	private String url;
	private Boolean checked;
	public Integer getPermission() {
		return permission;
	}
	public void setPermission(Integer permission) {
		this.permission = permission;
	}
	private String code;
	private Integer permission;
	public List<OperationDto> getChildren() {
		return children;
	}
	public String getCode() {
		return code;
	}
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public Boolean getNocheck() {
		return nocheck;
	}
	public Integer getParent() {
		return parentTId;
	}
	public Integer getParentTId() {
		return parentTId;
	}
	public String getUrl() {
		return url;
	}
	public void setChildren(List<OperationDto> children) {
		this.children = children;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setNocheck(Boolean nocheck) {
		this.nocheck = nocheck;
	}
	public void setParentTId(Integer parentTId) {
		this.parentTId = parentTId;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	

}
