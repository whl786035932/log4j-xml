package cn.videoworks.cms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="cms_permissions")
public class Permission {

	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name="type")
	private Integer type;

	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,mappedBy="permisson",orphanRemoval=true,fetch=FetchType.LAZY)
	private List<PermissionOperatoinMapping> permissionOperationMappings;
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,mappedBy="permisson",orphanRemoval=true,fetch=FetchType.LAZY)
	private List<PermissionMenuMapping> permissionMenuMappings;

	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,mappedBy="permisson",orphanRemoval=true,fetch=FetchType.LAZY)
	private List<RolePermissionMapping> permissionRoleMappings;

	public Integer getId() {
		return id;
	}
	
	public List<PermissionMenuMapping> getPermissionMenuMappings() {
		return permissionMenuMappings;
	}
	
	public List<PermissionOperatoinMapping> getPermissionOperationMappings() {
		return permissionOperationMappings;
	}

	public List<RolePermissionMapping> getPermissionRoleMappings() {
		return permissionRoleMappings;
	}

	public Integer getType() {
		return type;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPermissionMenuMappings(List<PermissionMenuMapping> permissionMenuMappings) {
		this.permissionMenuMappings = permissionMenuMappings;
	}

	public void setPermissionOperationMappings(List<PermissionOperatoinMapping> permissionOperationMappings) {
		this.permissionOperationMappings = permissionOperationMappings;
	}

	public void setPermissionRoleMappings(List<RolePermissionMapping> permissionRoleMappings) {
		this.permissionRoleMappings = permissionRoleMappings;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
