/**
 * Source.java
 * cn.videoworks.cms.entity
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月29日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ClassName:Source
 * Function: TODO ADD FUNCTION
 * Reason:	 来源实体类
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月29日		下午2:56:23
 *
 * @see 	 
 */
@Entity
@Table(name = "cms_source")
public class Source {
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long id; 
	
	/**
	 * 来源名称
	 */
	@Column(name = "name")
	public String name; 
	
	/**
	 * 来源标识
	 */
	@Column(name = "`key`")
	public String key; 
	
	/**
	 * 创建时间
	 */
	@Column(name = "inserted_at")
	public Date insertedAt;

	/**
	 * 修改时间
	 */
	@Column(name = "updated_at")
	public Date updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getInsertedAt() {
		return insertedAt;
	}

	public void setInsertedAt(Date insertedAt) {
		this.insertedAt = insertedAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}

