package cn.videoworks.cms.dto;

public class ApkDto {
	private Integer id;
	private String mainVersion;
	private Integer childVersion;
	private String apkName;
	private String apkPath;
	private Integer status;
	private Integer type;
	private String description;
	private String entry;
	private Long size;
	private String md5;
	private String archive;
	private Boolean isForce;//是否强制升级
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public String getApkPath() {
		return apkPath;
	}
	public void setApkPath(String apkPath) {
		this.apkPath = apkPath;
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
	public Boolean getIsForce() {
		return isForce;
	}
	public void setIsForce(Boolean isForce) {
		this.isForce = isForce;
	}
	public String getEntry() {
		return entry;
	}
	public void setEntry(String entry) {
		this.entry = entry;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getArchive() {
		return archive;
	}
	public void setArchive(String archive) {
		this.archive = archive;
	}
 
}
