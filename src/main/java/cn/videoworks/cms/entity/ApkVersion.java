package cn.videoworks.cms.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cms_apk_version")
public class ApkVersion {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = "main_version")
	private String mainVersion;
	
	@Column(name = "child_version")
	private Integer childVersion;

	@Column(name = "md5")
	private String md5;
	
	@Column(name = "size")
	private Long size;

	@Column(name = "type")
	private Integer type;

	@Column(name = "status")
	private Integer status;
	
	/**
	 * 是否强制升级
	 */
	@Column(name = "is_force")
	private Boolean isForce;

	@Column(name = "description")
	private String description;

	@Column(name = "inserted_at")
	private Timestamp inserted_at;

	@Column(name = "updated_at")
	private Timestamp updated_at;

	@Column(name="archive")
	private String archvie;
	
	/**
	 * apk主程序入口
	 */
	@Column(name="entry")
	private String entry;
	
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMainVersion() {
		return mainVersion;
	}

	public void setMainVersion(String mainVersion) {
		this.mainVersion = mainVersion;
	}

	public Integer getChildVersion() {
		return childVersion;
	}

	public void setChildVersion(Integer childVersion) {
		this.childVersion = childVersion;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getIsForce() {
		return isForce;
	}

	public void setIsForce(Boolean isForce) {
		this.isForce = isForce;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getInserted_at() {
		return inserted_at;
	}

	public void setInserted_at(Timestamp inserted_at) {
		this.inserted_at = inserted_at;
	}

	public Timestamp getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Timestamp updated_at) {
		this.updated_at = updated_at;
	}

	public String getArchvie() {
		return archvie;
	}

	public void setArchvie(String archvie) {
		this.archvie = archvie;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

}
