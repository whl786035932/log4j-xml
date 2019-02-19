package cn.videoworks.cms.dto;

import java.util.Date;

public class StationDto {

	/**
	 * 主键
	 */
	public Long id; 
	
	/**
	 * 站点实际地址，http://ip:port/booths
	 */
	public String url; 
	
	/**
	 * 名称
	 */
	public String name; 
	
	/**
	 * 状态， 0:禁用,1:启用
	 */
	public Integer status; 
	
	/**
	 * 描述
	 */
	public String description; 
	
	/**
	 * 最后一次修改时间
	 */
	public String lastUpdateTime; 
	
	/**
	 * 创建时间
	 */
	public String insertedAt;

	/**
	 * 修改时间
	 */
	public String updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getInsertedAt() {
		return insertedAt;
	}

	public void setInsertedAt(String insertedAt) {
		this.insertedAt = insertedAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
}
