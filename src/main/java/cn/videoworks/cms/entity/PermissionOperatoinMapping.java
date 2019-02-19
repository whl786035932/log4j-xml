package cn.videoworks.cms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.junit.Ignore;

@Entity
@Table(name="cms_permissions_operation")
public class PermissionOperatoinMapping {

	
	@Id
	@GeneratedValue
	private  Integer id;
	
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	@ManyToOne
	@JoinColumn(name="permissions_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Permission permisson;
	
	
	@ManyToOne
	@JoinColumn(name="operation_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Operation operation;


	public Operation getOperation() {
		return operation;
	}


	public Permission getPermisson() {
		return permisson;
	}


	public void setOperation(Operation operation) {
		this.operation = operation;
	}


	public void setPermisson(Permission permisson) {
		this.permisson = permisson;
	}
	
	
}
