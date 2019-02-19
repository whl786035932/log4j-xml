package cn.videoworks.cms.dto;

public class StationMediaDto {

	private String element_type;
	private String id;
	private String action;
	
	private String url_key;
	private int status;
	private int type;
	private long size;
	private String check_sum;
	private String file_name;
	
	/**
	 * 时长 单位秒
	 */
	private Integer duration;
	
	/**
	 * 宽
	 */
	private Integer width;
	
	/**
	 * 高
	 */
	private Integer height;
	
	/**
	 * 码率
	 */
	private Long bitrate;
	
	
	
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
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
	public Long getBitrate() {
		return bitrate;
	}
	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getElement_type() {
		return element_type;
	}
	public void setElement_type(String element_type) {
		this.element_type = element_type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getUrl_key() {
		return url_key;
	}
	public void setUrl_key(String url_key) {
		this.url_key = url_key;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getCheck_sum() {
		return check_sum;
	}
	public void setCheck_sum(String check_sum) {
		this.check_sum = check_sum;
	}
	
	
}
