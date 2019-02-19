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
@Table(name="cms_user")
public class User {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name="username")
	private String username;
	
	
	@Column(name="password")
	private String password;
	
	@Column(name="nickname")
	private String nickname;

	@Column(name="status")
	private Integer status;
	
	@Column(name="type")
	private Integer type;

	@Column(name="inserted_at")
	private Date inserted_at;

	@Column(name="updated_at")
	private Date updated_at;

	@Column(name="last_login_time")
	private Date last_login_time;

	@Column(name="count")
	private Integer count=0;
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,mappedBy="user",orphanRemoval=true,fetch=FetchType.LAZY)
	@OrderBy("id asc")
	private Set<UserRoleMapping> userRoleMappings;
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,mappedBy="user",orphanRemoval=true,fetch=FetchType.LAZY)
	private Set<UserClassificationMapping> userClassificationMappings;
	
	public User() {
		
	}
	
	public User(Integer id) {
		this.id = id;
	}

	public Set<UserClassificationMapping> getUserClassificationMappings() {
		return userClassificationMappings;
	}

	public void setUserClassificationMappings(Set<UserClassificationMapping> userClassificationMappings) {
		this.userClassificationMappings = userClassificationMappings;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Set<UserRoleMapping> getUserRoleMappings() {
		return userRoleMappings;
	}

	public void setUserRoleMappings(Set<UserRoleMapping> userRoleMappings) {
		this.userRoleMappings = userRoleMappings;
	}
	
	public Integer getCount() {
		return count;
	}

	public Integer getId() {
		return id;
	}
	
	public Date getInserted_at() {
		return inserted_at;
	}
	
	public Date getLast_login_time() {
		return last_login_time;
	}

	public String getNickname() {
		return nickname;
	}

	public String getPassword() {
		return password;
	}

	public Integer getStatus() {
		return status;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public String getUsername() {
		return username;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInserted_at(Date inserted_at) {
		this.inserted_at = inserted_at;
	}

	public void setLast_login_time(Date last_login_time) {
		this.last_login_time = last_login_time;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

}
