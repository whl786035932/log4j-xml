package cn.videoworks.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cms_classification_rule")
public class ClassificationRule {
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long id;// 唯一标识

	@Column(name = "tags")
	public String tags; // 标签值列表

	@Column(name = "classification_id")
	public String classificationId; // 分类id

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(String classificationId) {
		this.classificationId = classificationId;
	}

}
