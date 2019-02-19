package cn.videoworks.cms.dto;

public class ApiMediaDto {
	private Long id;
	private String url_key;
	private String cdn_key;
	private String inner_key;
	private Integer type;
	private Long size;

	private Integer duration;

	private Long bitrate;

	private Integer width;

	private Integer height;
	
	public String getInner_key() {
		return inner_key;
	}
	public void setInner_key(String inner_key) {
		this.inner_key = inner_key;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Long getBitrate() {
		return bitrate;
	}
	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUrl_key() {
		return url_key;
	}
	public void setUrl_key(String url_key) {
		this.url_key = url_key;
	}
	public String getCdn_key() {
		return cdn_key;
	}
	public void setCdn_key(String cdn_key) {
		this.cdn_key = cdn_key;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	
	
}
