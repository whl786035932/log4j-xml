package cn.videoworks.cms.dto;

public class CDNImageDto {
	private Long id;
	private Integer width;// :50， //宽

	private Integer height;// :30， //高

	private String url;// http://xxx.jpg";//实际地址 //必填
	private String type;// 0:未知,1:海报,2:xxx //必填
	private Integer size;// 大小 byte //必填
	private String check_sum;// :"D78-DDSF-9D-8F" //校验码 例如md5等 //必填
	private String filename;// 文件名称
	public String getCheck_sum() {
		return check_sum;
	}
	public String getFilename() {
		return filename;
	}

	public Integer getHeight() {
		return height;
	}

	public Long getId() {
		return id;
	}

	public Integer getSize() {
		return size;
	}

	public String getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public Integer getWidth() {
		return width;
	}

	public void setCheck_sum(String check_sum) {
		this.check_sum = check_sum;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSize(Integer  size) {
		this.size = size;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}
}
