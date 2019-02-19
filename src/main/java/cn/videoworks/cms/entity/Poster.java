package cn.videoworks.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ClassName:Poster
 * Function: TODO ADD FUNCTION
 * Reason:	海报实体类
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018	2018年5月28日		下午1:40:17
 *
 * @see
 */
@Entity
@Table(name = "cms_poster")
public class Poster implements Serializable {
	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long id; 
	
	/**
	 * 海报时间访问地址，没有key,不用换串
	 */
	@Column(name = "url")
	public String url; 

	/**
	 * 内容状态,0:禁用,1:启用
	 */
	@Column(name = "status")
	public int status;
	
	/**
	 * 海报宽
	 */
	@Column(name = "width")
	public int width;
	
	/**
	 * 海报高
	 */
	@Column(name = "height")
	public int height;
	
	/**
	 * 内容来源，比如视频工厂、媒资、第三方
	 */
	@Column(name = "source")
	public String source; 
	
	/**
	 * 描述
	 */
	@Column(name = "description")
	public String description; 

	/**
	 * 海报大小 ,单位byte
	 */
	@Column(name = "size")
	public int size;
	
	/**
	 * 海报MD5加密
	 */
	@Column(name = "check_sum")
	public String checkSum; 

	/**
	 * 创建时间
	 */
	@Column(name = "inserted_at")
	public Date insertedAt; 

	public Integer getCdnSyncStatus() {
		return cdnSyncStatus;
	}

	public void setCdnSyncStatus(Integer cdnSyncStatus) {
		this.cdnSyncStatus = cdnSyncStatus;
	}

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

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	/**
	 * CDN状态,0:未同步,1:已同步,2:同步失败
	 */
	@Column(name = "cdn_sync_status")
	public Integer cdnSyncStatus;
	
	/**
	 * 海报源地址。cms同步地址
	 */
	@Column(name = "source_url")
	public String sourceUrl; 
	
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
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

	
}
