package cn.videoworks.cms.vo;

import java.util.Date;
import java.util.List;

import cn.videoworks.cms.dto.ClassificationDto;
import cn.videoworks.cms.dto.OperationDto;

/**
 * @author   meishen
 * @Date	 2018	2018年9月25日		下午2:11:45
 * @Description 方法描述: 用户VO
 */
public class UserVo {

	private int id;
	private String username;
	private  List<MenuVo> menus;
	private  List<OperationDto> operations;
	private ClassificationDto classifications;
	private  Integer type;
	private Date last_login_time;
	
	public Date getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(Date last_login_time) {
		this.last_login_time = last_login_time;
	}

	public List<OperationDto> getOperations() {
		return operations;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setOperations(List<OperationDto> operations) {
		this.operations = operations;
	}
	
	public ClassificationDto getClassifications() {
		return classifications;
	}

	public void setClassifications(ClassificationDto classifications) {
		this.classifications = classifications;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<MenuVo> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuVo> menus) {
		this.menus = menus;
	}

}
