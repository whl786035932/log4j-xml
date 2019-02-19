package cn.videoworks.cms.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "cms_instorage_statistics")
public class InstorageStatistics {

	@Id
	@GeneratedValue
	private Integer id; // 主键
	
	@Column(name = "statistice_at")
	private String statisticeAt; // 每天
	
	@Column(name = "name")
	private String name; // 统计名称/频道/栏目/
	
	@ManyToOne
	@JoinColumn(name = "parent")
	@NotFound(action = NotFoundAction.IGNORE)
	private InstorageStatistics parent;// 父级
	
	@Column(name = "value")
	private String value; //
	
	@Column(name = "inserted_at")
	private Date insertedAt; //
	
	@Column(name = "updated_at")
	private Date updatedAt; //
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
	@OrderBy("insertedAt ASC")
	private Set<InstorageStatistics> children;// 一个InstorageStatistics有很多个子节点

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the statisticeAt
	 */
	public String getStatisticeAt() {
		return statisticeAt;
	}

	/**
	 * @param statisticeAt the statisticeAt to set
	 */
	public void setStatisticeAt(String statisticeAt) {
		this.statisticeAt = statisticeAt;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parent
	 */
	public InstorageStatistics getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(InstorageStatistics parent) {
		this.parent = parent;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the insertedAt
	 */
	public Date getInsertedAt() {
		return insertedAt;
	}

	/**
	 * @param insertedAt the insertedAt to set
	 */
	public void setInsertedAt(Date insertedAt) {
		this.insertedAt = insertedAt;
	}

	/**
	 * @return the updatedAt
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the children
	 */
	public Set<InstorageStatistics> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(Set<InstorageStatistics> children) {
		this.children = children;
	}


}
