package cn.videoworks.cms.vo;

import java.util.List;

public class InstorageStatisticsVo {
	private Integer id; // 主键

	private String statisticeAt; // 每天

	private String name; // 统计名称/频道/栏目/

	private Integer pId;// 父级

	private String value; //

	private String insertedAt; //

	private String updatedAt; //

	private List<InstorageStatisticsVo> children;// 一个Classification有很多个子节点

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param statisticeAt
	 *            the statisticeAt to set
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
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the pId
	 */
	public Integer getpId() {
		return pId;
	}

	/**
	 * @param pId
	 *            the pId to set
	 */
	public void setpId(Integer pId) {
		this.pId = pId;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the insertedAt
	 */
	public String getInsertedAt() {
		return insertedAt;
	}

	/**
	 * @param insertedAt
	 *            the insertedAt to set
	 */
	public void setInsertedAt(String insertedAt) {
		this.insertedAt = insertedAt;
	}

	/**
	 * @return the updatedAt
	 */
	public String getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt
	 *            the updatedAt to set
	 */
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the children
	 */
	public List<InstorageStatisticsVo> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(List<InstorageStatisticsVo> children) {
		this.children = children;
	}

}
