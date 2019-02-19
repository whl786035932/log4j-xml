package cn.videoworks.cms.dto;

import java.util.List;
import java.util.Map;

import cn.videoworks.cms.vo.ContentClassificationVo;

public class ContentDto {

	/**
	 * 主键
	 */
	public Long id;

	/**
	 * 媒资id
	 */
	public String assetId; 
	
	/**
	 * 标题
	 */
	public String title;
	
	/**
	 * 标题首字母
	 */
	public String titleAbbr; 

	/**
	 * 内容类型， 1：视频 2：专题
	 */
	public int type;

	/**
	 * 描述
	 */
	public String description;

	/**
	 * 播放时间
	 */
	public String publishTime;

	/**
	 * 时长
	 */
	public int duration;

	/**
	 * 状态,0：删除,1:未上架，2：已上架,3:数据不合法(未入存储)
	 */
	public int status;
	

	/**
	 * 最后一次上架时间
	 */
	public String lastShelvesTime; 

	/**
	 * 标签
	 */
	public String tags; 

	/**
	 * 内容提供商
	 */
	public String cp;
	
	/**
	 * 内容来源，比如视频工厂，媒资等
	 */
	public String source;

	/**
	 * 创建时间
	 */
	public String insertedAt;

	/**
	 * 修改时间
	 */
	public String updatedAt; 

	public List<AdiImageDto> images;

	public List<PosterDto> imagesN;
	
	public List<AdiMovieDto> movies;
	
	private List<Map<String,Object>> classifications;//详情页显示所有分类
	
	
	private List<ContentClassificationVo> classifs; //类别显示所有分类
	
	private List<Long> imgId;
	
	private String sourceChannel;//来源频道
	
	private String sourceColumn;//来源栏目
	
	private List<Long> orignalPosters;
	
	public int cdnSyncStatus;
	
	public int getCdnSyncStatus() {
		return cdnSyncStatus;
	}

	public void setCdnSyncStatus(int cdnSyncStatus) {
		this.cdnSyncStatus = cdnSyncStatus;
	}

	public List<ContentClassificationVo> getClassifs() {
		return classifs;
	}

	public void setClassifs(List<ContentClassificationVo> classifs) {
		this.classifs = classifs;
	}

	public List<Long> getOrignalPosters() {
		return orignalPosters;
	}

	public void setOrignalPosters(List<Long> orignalPosters) {
		this.orignalPosters = orignalPosters;
	}

	public String getAssetId() {
		return assetId;
	}
	
	public List<Map<String, Object>> getClassifications() {
		return classifications;
	}
	
	public String getCp() {
		return cp;
	}

	public String getDescription() {
		return description;
	}

	public int getDuration() {
		return duration;
	}

	public Long getId() {
		return id;
	}

	public List<AdiImageDto> getImages() {
		return images;
	}

	public List<PosterDto> getImagesN() {
		return imagesN;
	}

	public List<Long> getImgId() {
		return imgId;
	}

	public String getInsertedAt() {
		return insertedAt;
	}

	public String getLastShelvesTime() {
		return lastShelvesTime;
	}

	public List<AdiMovieDto> getMovies() {
		return movies;
	}

	public List<Long> getOriganlPosters() {
		return orignalPosters;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public String getSource() {
		return source;
	}

	public String getSourceChannel() {
		return sourceChannel;
	}

	public String getSourceColumn() {
		return sourceColumn;
	}

	public int getStatus() {
		return status;
	}

	public String getTags() {
		return tags;
	}

	public String getTitle() {
		return title;
	}

	public String getTitleAbbr() {
		return titleAbbr;
	}

	public int getType() {
		return type;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public void setClassifications(List<Map<String, Object>> classifications) {
		this.classifications = classifications;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setImages(List<AdiImageDto> images) {
		this.images = images;
	}

	public void setImagesN(List<PosterDto> imagesN) {
		this.imagesN = imagesN;
	}

	public void setImgId(List<Long> imgId) {
		this.imgId = imgId;
	}

	public void setInsertedAt(String insertedAt) {
		this.insertedAt = insertedAt;
	}

	public void setLastShelvesTime(String lastShelvesTime) {
		this.lastShelvesTime = lastShelvesTime;
	}

	public void setMovies(List<AdiMovieDto> movies) {
		this.movies = movies;
	}

	public void setOriganlPosters(List<Long> origanlPosters) {
		this.orignalPosters = origanlPosters;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setSourceChannel(String sourceChannel) {
		this.sourceChannel = sourceChannel;
	}

	public void setSourceColumn(String sourceColumn) {
		this.sourceColumn = sourceColumn;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitleAbbr(String titleAbbr) {
		this.titleAbbr = titleAbbr;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

}
