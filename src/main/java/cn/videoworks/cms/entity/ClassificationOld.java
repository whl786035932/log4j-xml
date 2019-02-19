package cn.videoworks.cms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.springframework.data.repository.NoRepositoryBean;


import cn.videoworks.cms.enumeration.ClassificationStatus;
import cn.videoworks.cms.enumeration.ClassificationType;

@Entity
@Table(name = "cms_classification1")
public class ClassificationOld {
	@Id
	@Column(name = "id")
	private String id; // 唯一标识

	@Column(name = "name")
	private String name; // 名称

	@Column(name = "icon")
	private String icon; // 图标

	@Column(name = "description")
	private String description; // 描述

	@Column(name = "status")
	private ClassificationStatus status; // 状态

	@Column(name = "lft")
	private Long lft;// 左值

	@Column(name = "rgt")
	private Long rgt;// 右值

	@Column(name = "type")
	private ClassificationType type; // 类型

	@Column(name = "inserted_at")
	private Date inserted_at; // 创建时间

	@Column(name = "updated_at")
	private Date updated_at;// 修改时间

	@Column(name = "sync_status")
	private int syncStatus;// 0: 未同步，1：已同步，
	
	@Transient
	private String parent;// 父id
	
	@Transient
	private List<ClassificationOld> children;// 子节点
	
	@Transient
	private Integer level; // 层级
	
	@Transient
	private Integer sequence; // 排序
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ClassificationStatus getStatus() {
		return status;
	}

	public void setStatus(ClassificationStatus status) {
		this.status = status;
	}

	public Date getInserted_at() {
		return inserted_at;
	}

	public void setInserted_at(Date inserted_at) {
		this.inserted_at = inserted_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getLft() {
		return lft;
	}

	public void setLft(Long lft) {
		this.lft = lft;
	}

	public Long getRgt() {
		return rgt;
	}

	public void setRgt(Long rgt) {
		this.rgt = rgt;
	}

	public ClassificationType getType() {
		return type;
	}

	public void setType(ClassificationType type) {
		this.type = type;
	}

	public int getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(int syncStatus) {
		this.syncStatus = syncStatus;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public List<ClassificationOld> getChildren() {
		return children;
	}

	public void setChildren(List<ClassificationOld> children) {
		this.children = children;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
