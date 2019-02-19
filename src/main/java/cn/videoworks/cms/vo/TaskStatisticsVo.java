package cn.videoworks.cms.vo;

import java.util.List;

public class TaskStatisticsVo {

	/**
	 * 主键
	 */
	public Long id;
	
	/**
	 * 当天总数
	 */
	public Integer totalNumber=0; // 默认0
	
	/**
	 * 当天成功数
	 */
	public Integer successNumber=0; // 默认0
	
	/**
	 * 当天失败数
	 */
	public Integer failureNumber=0; // 默认0
	
	/**
	 * 当天进行中数
	 */
	public Integer waitingNumber=0; // 默认0
	
	/**
	 * 当天重复数
	 */
	public Integer repeatNumber=0; // 默认0
	
	/**
	 * 创建时间
	 */
	public String insertedAt;

	/**
	 * 修改时间
	 */
	public String updatedAt;
	
	/**
	 * 统计时间 yyyy-MM-dd
	 */
	public String statisticsAt;
	
	public List<String> repeatData;
	
	public List<String> getRepeatData() {
		return repeatData;
	}

	public void setRepeatData(List<String> repeatData) {
		this.repeatData = repeatData;
	}

	public Integer getRepeatNumber() {
		return repeatNumber;
	}

	public void setRepeatNumber(Integer repeatNumber) {
		this.repeatNumber = repeatNumber;
	}

	public String getStatisticsAt() {
		return statisticsAt;
	}

	public void setStatisticsAt(String statisticsAt) {
		this.statisticsAt = statisticsAt;
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
