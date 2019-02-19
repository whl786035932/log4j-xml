package cn.videoworks.cms.dto;

import java.util.List;

public class UserDto {
	
	private Integer id;
	private String username;
	private String nickname;
	private Integer status;
	private  String password;
	private List<Integer> role;  //角色的id
	private String roleName;  //角色的name
	private Integer type;
	private String inserted_at;
	private String updated_at;
	private String classificationStr;

	public String getClassificationStr() {
		return classificationStr;
	}

	public void setClassificationStr(String classificationStr) {
		this.classificationStr = classificationStr;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public  List<Integer>  getRole() {
		return role;
	}

	public void setRole( List<Integer>  role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Integer getId() {
		return id;
	}

	public String getInserted_at() {
		return inserted_at;
	}

	public String getNickname() {
		return nickname;
	}

	public Integer getStatus() {
		return status;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public String getUsername() {
		return username;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInserted_at(String inserted_at) {
		this.inserted_at = inserted_at;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
