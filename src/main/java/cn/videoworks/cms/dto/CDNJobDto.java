package cn.videoworks.cms.dto;

import java.util.List;

public class CDNJobDto {
	private Long contentId;
	private String  asset_id;// "媒资id"
	private String title; // "标题", //必填
	private String title_abbr;// "标题首字母", //必填
	private Integer type;// 1, //1:视频,2:广告 //必填
	private String description;// "描述", //可选
	private String publish_time;// "2006-02-09 09:30:43", //播放时间 //必填
	private Long duration;// 60, //时长 秒 //必填
	private List<String> tags;// ["推荐"，"国内"], //必填
	private String cp;// "内容提供商", //必填
	private String source;// :"视频工" //内容来源，比如视频工厂、媒资 //必填
	private List<CDNMovieDto> movies;// :
	private List<CDNImageDto> images;
	private Integer isUploadCdn;
	private Long taskId;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getAsset_id() {
		return asset_id;
	}

	public Integer getIsUploadCdn() {
		return isUploadCdn;
	}

	public void setIsUploadCdn(Integer isUploadCdn) {
		this.isUploadCdn = isUploadCdn;
	}

	public void setAsset_id(String  asset_id) {
		this.asset_id = asset_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle_abbr() {
		return title_abbr;
	}

	public void setTitle_abbr(String title_abbr) {
		this.title_abbr = title_abbr;
	}

	public Integer getType() {
		return type;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public Long getDuration() {
		return duration;
	}



	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
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

	public List<CDNMovieDto> getMovies() {
		return movies;
	}

	public void setMovies(List<CDNMovieDto> movies) {
		this.movies = movies;
	}

	public List<CDNImageDto> getImages() {
		return images;
	}

	public void setImages(List<CDNImageDto> images) {
		this.images = images;
	}

}
