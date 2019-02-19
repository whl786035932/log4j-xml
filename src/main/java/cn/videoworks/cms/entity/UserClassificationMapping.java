package cn.videoworks.cms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * @author   meishen
 * @Date	 2018	2018年9月28日		下午3:32:21
 * @Description 方法描述: 用户与分类关系
 */
@Entity
@Table(name="cms_user_classification")
public class UserClassificationMapping {

	@Id
	@GeneratedValue
	private Integer id;
	
	@JoinColumn(name="user_id")
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	private User user;

	/**
	 * 分类id
	 */
	@JoinColumn(name="classification_id")
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	private Classification classification;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}
	
}
