package cn.videoworks.cms.dto;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class ApiContentDto {
	private Long id;
	private String asset_id;
	private  String title;
	private String title_abbr;
	private Integer type;
	private String description;
	private String publish_time;
	private Integer  duration;
	private Integer play_count;
	private List<PosterDto> posters;
	private List<ApiMediaDto> medias;
	private String source;
	private Integer sequence;
	private Integer  recommend=0;
	
	public Integer getRecommend() {
		return recommend;
	}
	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAsset_id() {
		return asset_id;
	}
	public String getDescription() {
		return description;
	}
	public Integer getDuration() {
		return duration;
	}
	public Long getId() {
		return id;
	}
	public List<ApiMediaDto> getMedias() {
		return medias;
	}
	public Integer getPlay_count() {
		return play_count;
	}
	public List<PosterDto> getPosters() {
		return posters;
	}
	public String getTitle() {
		return title;
	}
	public String getTitle_abbr() {
		return title_abbr;
	}
	public Integer getType() {
		return type;
	}
	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setMedias(List<ApiMediaDto> medias) {
		this.medias = medias;
	}
	public void setPlay_count(Integer play_count) {
		this.play_count = play_count;
	}
	public void setPosters(List<PosterDto> posters) {
		this.posters = posters;
	}
	
	public String getPublish_time() {
		return publish_time;
	}
	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setTitle_abbr(String title_abbr) {
		this.title_abbr = title_abbr;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
