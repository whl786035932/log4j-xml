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
@Table(name="cms_user_role")
public class UserRoleMapping {
	@Id
	@GeneratedValue
	private Integer id;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JoinColumn(name="user_id")
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	private User user;

	/**
	 * 内容id
	 */
	@JoinColumn(name="role_id")
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	private Role role;

	public Role getRole() {
		return role;
	}

	public User getUser() {
		return user;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
