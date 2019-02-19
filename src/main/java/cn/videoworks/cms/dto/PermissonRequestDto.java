package cn.videoworks.cms.dto;

import java.util.List;

import org.omg.CORBA.INTERNAL;

public class PermissonRequestDto {

	private List<Integer> operations;
	private List<Integer> menus;
	private List<Integer> roles;
	public List<Integer> getRoles() {
		return roles;
	}
	public void setRoles(List<Integer> roles) {
		this.roles = roles;
	}
	public List<Integer> getMenus() {
		return menus;
	}
	public List<Integer> getOperations() {
		return operations;
	}
	public void setMenus(List<Integer> menus) {
		this.menus = menus;
	}
	public void setOperations(List<Integer> operations) {
		this.operations = operations;
	}
	
}
