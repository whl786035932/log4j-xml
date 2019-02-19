package cn.videoworks.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "cms_classification_content_mapping")
public class ClassificationContentMapping {

	/**
	 *  主键
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long id;
	
	/**
	 * 分类id
	 */
	@JoinColumn(name="classification_id")
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	private Classification classification;

	/**
	 * 内容id
	 */
	@JoinColumn(name="content_id")
	@ManyToOne
	private Content content;
	
	/**
	 * 排序
	 */
	@Column(name = "sequence")
	private Integer sequence;
	
	/**
	 * 是否推荐
	 */
	@Column(name = "recommend")
	private Integer recommend;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}
	
	
}
