package cn.videoworks.cms.dto;
/**
 * 注入cdn后的返回结果
 * @author whl
 *
 */
public class CDNReturnDto {

	private  Long id;
	private String cdn_key;
	private Integer type;
	private String file_name;
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	
	
	
}
