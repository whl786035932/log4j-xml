package cn.videoworks.cms.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class PosterDto {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 海报地址
	 */
	@NotBlank
	private String url;

	/**
	 * 0:未知,1:海报,2:XXX
	 */
	@NotNull
	private Integer type;

	/**
	 * 宽
	 */
	@NotNull
	private Integer width;

	/**
	 * 高
	 */
	@NotNull
	private Integer height;

	/**
	 * 大小 kb
	 */
	@NotNull
	private String size;

	/**
	 * 校验媒体是否丢失数据，校验码 例如md5等
	 */
	@NotBlank
	private String check_sum;

	/**
	 * 文件名称
	 */
	@NotBlank
	public String file_name;

	/**
	 * 内容状态,0:禁用,1:启用
	 */
	@Column(name = "status")
	public int status;

	/**
	 * CDN状态,0:未同步,1:已同步,2:同步失败
	 */
	@Column(name = "sync_status")
	public int syncStatus;

	/**
	 * 内容来源，比如视频工厂、媒资、第三方
	 */
	@Column(name = "source")
	public String source;

	/**
	 * 描述
	 */
	public String description;

	/**
	 * 海报MD5加密
	 */
	@Column(name = "check_sum")
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

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCheck_sum() {
		return check_sum;
	}

	public void setCheck_sum(String check_sum) {
		this.check_sum = check_sum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
