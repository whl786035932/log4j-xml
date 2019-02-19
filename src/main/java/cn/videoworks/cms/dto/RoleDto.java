package cn.videoworks.cms.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class RoleDto {
	private Integer id;

	private String name;

	private String description;

	private String  inserted_at;

	private String  updated_at;

	public String getDescription() {
		return description;
	}

	public Integer getId() {
		return id;
	}

	public String getInserted_at() {
		return inserted_at;
	}

	public String getName() {
		return name;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInserted_at(String inserted_at) {
		this.inserted_at = inserted_at;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
}
