package cn.videoworks.cms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cms_task_statistics")
public class TaskStatistics {

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long id;
	
	/**
	 * 当天总数
	 */
	@Column(name = "total_number")
	public Integer totalNumber=0; // 默认0
	
	/**
	 * 当天成功数
	 */
	@Column(name = "success_number")
	public Integer successNumber=0; // 默认0
	
	/**
	 * 当天失败数
	 */
	@Column(name = "failure_number")
	public Integer failureNumber=0; // 默认0
	
	/**
	 * 当天进行中数
	 */
	@Column(name = "waiting_number")
	public Integer waitingNumber=0; // 默认0
	
	/**
	 * 当天重复数
	 */
	@Column(name = "repeat_number")
	public Integer repeatNumber=0; // 默认0
	
	/**
	 * 当天重复数据
	 */
	@Column(name = "repeat_data")
	public String repeatData;
	
	/**
	 * 创建时间
	 */
	@Column(name = "statistics_at")
	public String statisticsAt;
	
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
	
	public String getRepeatData() {
		return repeatData;
	}

	public void setRepeatData(String repeatData) {
		this.repeatData = repeatData;
	}

	public String getStatisticsAt() {
		return statisticsAt;
	}

	public void setStatisticsAt(String statisticsAt) {
		this.statisticsAt = statisticsAt;
	}

	public Integer getRepeatNumber() {
		return repeatNumber;
	}

	public void setRepeatNumber(Integer repeatNumber) {
		this.repeatNumber = repeatNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Integer getSuccessNumber() {
		return successNumber;
	}

	public void setSuccessNumber(Integer successNumber) {
		this.successNumber = successNumber;
	}

	public Integer getFailureNumber() {
		return failureNumber;
	}

	public void setFailureNumber(Integer failureNumber) {
		this.failureNumber = failureNumber;
	}

	public Integer getWaitingNumber() {
		return waitingNumber;
	}

	public void setWaitingNumber(Integer waitingNumber) {
		this.waitingNumber = waitingNumber;
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
