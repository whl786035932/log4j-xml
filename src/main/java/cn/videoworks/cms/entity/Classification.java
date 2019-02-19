package cn.videoworks.cms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.videoworks.cms.enumeration.ClassificationDele;
import cn.videoworks.cms.enumeration.ClassificationEdit;
import cn.videoworks.cms.enumeration.ClassificationRecommend;
import cn.videoworks.cms.enumeration.ClassificationStatus;
import cn.videoworks.cms.enumeration.ClassificationType;

@Entity
@Table(name = "cms_classification")
public class Classification {
	@Id
	@Column(name = "id")
	private String id; // 唯一标识

	@Column(name = "name")
	private String name; // 名称
	
	@Column(name = "alias")
	private String alias; // 别名

	@Column(name = "icon")
	private String icon; // 图标

	@Column(name = "description")
	private String description; // 描述

	@Column(name = "status")
	private ClassificationStatus status; // 状态
	
	@Column(name = "parent")
	private String parent; // 父级
	
	@Column(name = "editable")
	private ClassificationEdit editable; // 是否可编辑
	
	@Column(name = "deletable")
	private ClassificationDele deletable; // 是否可删除
	
	@Column(name = "recommend")
	private ClassificationRecommend recommend; // 是否推荐
	
	@Column(name = "level")
	private Integer level; // 层级
	
	@Column(name = "sequence")
	private Integer sequence; // 排序
	
	@Column(name = "type")
	private ClassificationType type; // 类型

	@Column(name = "inserted_at")
	private Date inserted_at; // 创建时间

	@Column(name = "updated_at")
	private Date updated_at;// 修改时间
	
	public Classification() {
		
	}

	public Classification(String id) {
		this.id = id;
	}
	

	public ClassificationRecommend getRecommend() {
		return recommend;
	}

	public void setRecommend(ClassificationRecommend recommend) {
		this.recommend = recommend;
	}

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

	public ClassificationType getType() {
		return type;
	}

	public void setType(ClassificationType type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public ClassificationEdit getEditable() {
		return editable;
	}

	public void setEditable(ClassificationEdit editable) {
		this.editable = editable;
	}

	public ClassificationDele getDeletable() {
		return deletable;
	}

	public void setDeletable(ClassificationDele deletable) {
		this.deletable = deletable;
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

//	public Set<Classification> getChildren() {
//		return children;
//	}
//
//	public void setChildren(Set<Classification> children) {
//		this.children = children;
//	}

}
