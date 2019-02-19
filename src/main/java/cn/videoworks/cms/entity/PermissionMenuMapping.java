package cn.videoworks.cms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="cms_permissions_menu")
public class PermissionMenuMapping {

	@Id
	@GeneratedValue
	private  Integer id;
	
	@ManyToOne
	@JoinColumn(name="permissions_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Permission permisson;
	
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="menu_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Menu menu;

	public PermissionMenuMapping() {
		
	}

	public PermissionMenuMapping(Permission permisson, Menu menu) {
		this.permisson = permisson;
		this.menu = menu;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Permission getPermisson() {
		return permisson;
	}


	public void setPermisson(Permission permisson) {
		this.permisson = permisson;
	}


	public Menu getMenu() {
		return menu;
	}


	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	
}
