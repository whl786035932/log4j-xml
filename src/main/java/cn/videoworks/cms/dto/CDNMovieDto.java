package cn.videoworks.cms.dto;

public class CDNMovieDto {
	private Long id;
	private String url;// http://xxx.jpg";//实际地址 //必填

	private Integer type;// 1, //0:未知,1:正片,2:预告片 //必填

	private Long size;// 23， //大小 kb //必填
	private String check_sum;// :"D78-DDSF-9D-8F" //校验码 例如md5等 //必填
	private String filename;// 文件名称

	private Integer duration;

	private Long bitrate;

	private Integer width;

	private Integer height;

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

	public String getCheck_sum() {
		return check_sum;
	}

	public String getFilename() {
		return filename;
	}

	public Long getId() {
		return id;
	}

	public Long getSize() {
		return size;
	}

	public Integer getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public void setCheck_sum(String check_sum) {
		this.check_sum = check_sum;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
