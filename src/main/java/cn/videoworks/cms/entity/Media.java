package cn.videoworks.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "cms_media")
public class Media implements Serializable{
	/**
	 *  主键
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long id;

	/**
	 * 内容id
	 */
	@ManyToOne
	@JoinColumn(name="content_id")
	private Content content;

	/**
	 * 存储key，需要换串
	 */
	@Column(name = "url_key")
	public String urlKey; 

	/**
	 * 默认0， 0：禁用 1：启用
	 */
	@Column(name = "status")
	public int status;

	/**
	 * 类型，0：未知，1：正片，2：预告片
	 */
	@Column(name = "type")
	public int type;
	
	/**
	 * 大小，单位byte
	 */
	@Column(name = "size")
	public long size;
	
	/**
	 * 校验码，md5加密
	 */
	@Column(name = "check_sum")
	public String checkSum;
	
	/**
	 * 时长 单位豪秒
	 */
	@Column(name = "duration")
	public Integer duration;
	
	/**
	 * 宽
	 */
	@Column(name = "width")
	public Integer width;
	
	/**
	 * 高
	 */
	@Column(name = "height")
	public Integer height;
	
	/**
	 * 码率
	 */
	@Column(name = "bitrate")
	public Long bitrate;

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
	
	/**
	 * 文件名称
	 */
	@Column(name = "file_name")
	public String fileName;
	
	@Column(name = "cdn_sync_status")
	public Integer cdn_sync_status; // cdn状态 默认0 0：未同步 1：已同步 2：同步失败
	
	@Column(name = "cdn_key")
	public String cdnKey; // cdn key，需要换串
	
	@Column(name = "inner_key")
	public String innerKey; // cdn 内部key，智能推荐使用

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Long getBitrate() {
		return bitrate;
	}

	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public String getUrlKey() {
		return urlKey;
	}

	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
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

	public Integer getCdn_sync_status() {
		return cdn_sync_status;
	}

	public void setCdn_sync_status(Integer cdn_sync_status) {
		this.cdn_sync_status = cdn_sync_status;
	}

	public String getCdnKey() {
		return cdnKey;
	}

	public void setCdnKey(String cdnKey) {
		this.cdnKey = cdnKey;
	}

	public String getInnerKey() {
		return innerKey;
	}

	public void setInnerKey(String innerKey) {
		this.innerKey = innerKey;
	}

}
