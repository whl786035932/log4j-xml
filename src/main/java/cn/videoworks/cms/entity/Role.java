package cn.videoworks.cms.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="cms_role")
public class Role {
	
	@Id
	@GeneratedValue
	private Integer id;

	@Column(name="name")
	private String name;

	@Column(name="parent")
	private Integer parent;

	@Column(name="description")
	private String description;

	@Column(name="inserted_at")
	private Date  inserted_at;

	public Set<RolePermissionMapping> getPermissionRoleMappings() {
		return permissionRoleMappings;
	}

	public void setPermissionRoleMappings(Set<RolePermissionMapping> permissionRoleMappings) {
		this.permissionRoleMappings = permissionRoleMappings;
	}

	@Column(name="updated_at")
	private Date  updated_at;

	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,mappedBy="role",orphanRemoval=true,fetch=FetchType.LAZY)
	@OrderBy("id asc")
	private Set<UserRoleMapping> userRoleMappings;
	
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,mappedBy="role",orphanRemoval=true,fetch=FetchType.LAZY)
	@OrderBy("id asc")
	private Set<RolePermissionMapping> permissionRoleMappings;
	

	public String getDescription() {
		return description;
	}

	public Integer getId() {
		return id;
	}

	public Date getInserted_at() {
		return inserted_at;
	}

	public String getName() {
		return name;
	}

	public Integer getParent() {
		return parent;
	}

	public Date getUpdated_at() {
		return updated_at;
	}
	
	
	public Set<UserRoleMapping> getUserRoleMappings() {
		return userRoleMappings;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public void setInserted_at(Date inserted_at) {
		this.inserted_at = inserted_at;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	
	public void setUserRoleMappings(Set<UserRoleMapping> userRoleMappings) {
		this.userRoleMappings = userRoleMappings;
	}
	

}
