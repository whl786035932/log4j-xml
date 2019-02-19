package cn.videoworks.cms.dto;

import java.text.DecimalFormat;
import java.util.Date;

import cn.videoworks.cms.entity.Poster;

public class PPDto {
	/**
	 * 主键
	 */
	public Long id;

	/**
	 * 海报时间访问地址，没有key,不用换串
	 */
	public String url;

	/**
	 * 内容状态,0:禁用,1:启用
	 */
	public int status;

	/**
	 * CDN状态,0:未同步,1:已同步,2:同步失败
	 */
	public int syncStatus;

	/**
	 * 海报宽
	 */
	public int width;

	/**
	 * 海报高
	 */
	public int height;

	/**
	 * 内容来源，比如视频工厂、媒资、第三方
	 */
	public String source;

	/**
	 * 描述
	 */
	public String description;

	/**
	 * 海报大小 ,单位byte
	 */
	public String size;

	/**
	 * 海报MD5加密
	 */
	public String checkSum;

	/**
	 * 创建时间
	 */
	public Date insertedAt;

	/**
	 * 修改时间
	 */
	public Date updatedAt;

	/**
	 * 文件名称
	 */
	public String fileName;

	public PPDto() {
	}

	public PPDto(Poster poster) {
		id = poster.getId();
		url = poster.getSourceUrl();

		status = poster.getStatus();

		width = poster.getWidth();

		height = poster.getHeight();

		source = poster.getSource();

		description = poster.getDescription();
		DecimalFormat df = new DecimalFormat("0.00");

		String number = df.format((float) poster.getSize() / 1024);
		size = number + "kb";

		checkSum = poster.getCheckSum();

		insertedAt = poster.getInsertedAt();
		updatedAt = poster.getUpdatedAt();

		fileName = poster.getFileName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(int syncStatus) {
		this.syncStatus = syncStatus;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
