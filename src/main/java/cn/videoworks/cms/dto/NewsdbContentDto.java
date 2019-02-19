package cn.videoworks.cms.dto;

/**
 * @author   meishen
 * @Date	 2018	2018年8月24日		上午10:20:06
 * @Description 方法描述: 注入新闻通参数
 */
public class NewsdbContentDto {
	private String asset_id;
	private String title;
	private String title_abbr;
	private Integer type;
	private String description;
	private Long publish_time;
	private String cp;
	private String source;
	private String source_column;
	private String source_channel;
	private String url;
	private String poster;
	private String classification;//分类标签
	
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getAsset_id() {
		return asset_id;
	}
	public void setAsset_id(String asset_id) {
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
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getPublish_time() {
		return publish_time;
	}
	public void setPublish_time(Long publish_time) {
		this.publish_time = publish_time;
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
	public String getSource_column() {
		return source_column;
	}
	public void setSource_column(String source_column) {
		this.source_column = source_column;
	}
	public String getSource_channel() {
		return source_channel;
	}
	public void setSource_channel(String source_channel) {
		this.source_channel = source_channel;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
}
