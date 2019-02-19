package cn.videoworks.cms.dto;

public class RecommendRequestDto {
	private Integer service_type;
	
	private String bid;
	private Integer uid_type;
	private String uid;
	private String scene_id;
	private Integer request_num;
	
	private String token;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getService_type() {
		return service_type;
	}
	public void setService_type(Integer service_type) {
		this.service_type = service_type;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public Integer getUid_type() {
		return uid_type;
	}
	public void setUid_type(Integer uid_type) {
		this.uid_type = uid_type;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getScene_id() {
		return scene_id;
	}
	public void setScene_id(String scene_id) {
		this.scene_id = scene_id;
	}
	public Integer getRequest_num() {
		return request_num;
	}
	public void setRequest_num(Integer request_num) {
		this.request_num = request_num;
	}
}
