package cn.videoworks.cms.dto;

import java.util.List;

import cn.videoworks.cms.vo.ContentClassificationVo;

public class ClassificationContentMappingDto {
	private Long id;// 唯一标识

	private Long cid;// 内容id

	private String classId;// 分类id

	private int status;// 状态

	private String title;// 标题

	public String updatedAt;// 修改时间

	public String publishTime;// 播放时间

	public String source;// 内容来源，比如视频工厂，媒资等
	private String sourceChannel;//来源频道
	private String sourceColumn;//来源栏目

	public String lastShelvesTime;// 最后一次上架时间

	public String insertedAt;// 入站时间

	public int recommend;// 推荐
	
	public List<AdiImageDto> images;//海报
	
	private int cdnSyncStatus;// 状态
	
	private int duration;
	
	private List<ContentClassificationVo> classifs; //类别显示所有分类
	
	public List<ContentClassificationVo> getClassifs() {
		return classifs;
	}

	public void setClassifs(List<ContentClassificationVo> classifs) {
		this.classifs = classifs;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getCdnSyncStatus() {
		return cdnSyncStatus;
	}

	public void setCdnSyncStatus(int cdnSyncStatus) {
		this.cdnSyncStatus = cdnSyncStatus;
	}

	public List<AdiImageDto> getImages() {
		return images;
	}

	public void setImages(List<AdiImageDto> images) {
		this.images = images;
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

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLastShelvesTime() {
		return lastShelvesTime;
	}

	public void setLastShelvesTime(String lastShelvesTime) {
		this.lastShelvesTime = lastShelvesTime;
	}

	public String getInsertedAt() {
		return insertedAt;
	}

	public void setInsertedAt(String insertedAt) {
		this.insertedAt = insertedAt;
	}

}
