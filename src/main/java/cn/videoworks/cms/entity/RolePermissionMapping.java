package cn.videoworks.cms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="cms_role_permissions")
public class RolePermissionMapping {

	@Id
	@GeneratedValue
	private  Integer id;
	
	@ManyToOne
	@JoinColumn(name="permissions_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Permission permisson;
	
	
	@ManyToOne
	@JoinColumn(name="role_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Role role;

	public RolePermissionMapping() {
		
	}

	public Integer getId() {
		return id;
	}

	public Permission getPermisson() {
		return permisson;
	}

	public Role getRole() {
		return role;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPermisson(Permission permisson) {
		this.permisson = permisson;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	
}
