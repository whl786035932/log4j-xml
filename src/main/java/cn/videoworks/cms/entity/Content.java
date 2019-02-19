package cn.videoworks.cms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * ClassName:Content
 * Function: TODO ADD FUNCTION
 * Reason:	 内容实体类
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018	2018年5月28日		下午8:13:22
 *
 * @see
 */
@Entity
@Table(name = "cms_content")

@JsonIgnoreProperties({"medias","contentPosterMappings"})
public class Content implements Serializable {
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
	 * 媒资id
	 */
	@Column(name = "asset_id")
	public String assetId; 
	
	/**
	 * 标题
	 */
	@Column(name = "title")
	public String title;
	
	/**
	 * 标题首字母
	 */
	@Column(name = "title_abbr")
	public String titleAbbr; 

	/**
	 * 内容类型， 1：视频 2：专题
	 */
	@Column(name = "type")
	public int type;

	/**
	 * 描述
	 */
	@Column(name = "description")
	public String description;

	/**
	 * 播放时间
	 */
	@Column(name = "publish_time")
	public Date publishTime;

	/**
	 * 状态,0：删除,1:未上架，2：已上架,3:数据不合法(未入存储)
	 */
	@Column(name = "status")
	public int status;
	
	/**
	 * 最后一次上架时间
	 */
	@Column(name = "last_shelves_time")
	public Date lastShelvesTime; 

	/**
	 * 标签
	 */
	@Column(name = "tags")
	public String tags; 

	/**
	 * 内容提供商
	 */
	@Column(name = "cp")
	public String cp;
	
	/**
	 * 内容来源，比如视频工厂，媒资等
	 */
	@Column(name = "source")
	public String source; 
	
	/**
	 * 内容来源频道，比如视频工厂，媒资等
	 */
	@Column(name = "source_channel")
	public String sourceChannel; 
	
	/**
	 * 内容来源栏目，比如视频工厂，媒资等
	 */
	@Column(name = "source_column")
	public String sourceColumn; 

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
	
	@Column(name="cdn_sync_status")
	private Integer cdn_sync_status;
	
	@Column(name = "play_count")
	public Integer playCount=0; // 默认0
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,mappedBy="content",orphanRemoval=true,fetch=FetchType.LAZY)
	private List<Media> medias;
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,mappedBy="content",orphanRemoval=true,fetch=FetchType.LAZY)
	@OrderBy("id asc")
	private Set<ContentPosterMapping> contentPosterMappings;
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,mappedBy="content",fetch=FetchType.LAZY)
	@OrderBy("id asc")
	private Set<ClassificationContentMapping> classificationContentMappingMappings;

	public Content() {
		
	}
	public Integer getCdn_sync_status() {
		return cdn_sync_status;
	}

	public void setCdn_sync_status(Integer cdn_sync_status) {
		this.cdn_sync_status = cdn_sync_status;
	}
	
	public Integer getPlayCount() {
		return playCount;
	}

	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}

	public String getSourceChannel() {
		return sourceChannel;
	}

	public void setSourceChannel(String sourceChannel) {
		this.sourceChannel = sourceChannel;
	}

	public String getSourceColumn() {
		return sourceColumn;
	}

	public void setSourceColumn(String sourceColumn) {
		this.sourceColumn = sourceColumn;
	}

	public Set<ClassificationContentMapping> getClassificationContentMappingMappings() {
		return classificationContentMappingMappings;
	}

	public void setClassificationContentMappingMappings(
			Set<ClassificationContentMapping> classificationContentMappingMappings) {
		this.classificationContentMappingMappings = classificationContentMappingMappings;
	}

	public Content(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleAbbr() {
		return titleAbbr;
	}

	public void setTitleAbbr(String titleAbbr) {
		this.titleAbbr = titleAbbr;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getLastShelvesTime() {
		return lastShelvesTime;
	}

	public void setLastShelvesTime(Date lastShelvesTime) {
		this.lastShelvesTime = lastShelvesTime;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public List<Media> getMedias() {
		return medias;
	}

	public void setMedias(List<Media> medias) {
		this.medias = medias;
	}
	
	public Set<ContentPosterMapping> getContentPosterMappings() {
		return contentPosterMappings;
	}

	public void setContentPosterMappings(Set<ContentPosterMapping> contentPosterMappings) {
		this.contentPosterMappings = contentPosterMappings;
	}

}
