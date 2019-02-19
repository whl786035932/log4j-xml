package cn.videoworks.cms.dto;

import java.util.Map;

/**
 * 贵州之智能推荐
 * 
 * @author whl
 *
 */
public class IntellRecommendRequestDto {

	private String MD5;

	private Integer service_type;

	private String request_id;

	private String bid;

	private Integer uid_type;

	private String uid;

	private String scene_id;

	private Integer request_num;

	private Map<String, Object> geo;

	public String getBid() {
		return bid;
	}

	public Map<String, Object> getGeo() {
		return geo;
	}

	public String getMD5() {
		return MD5;
	}

	public String getRequest_id() {
		return request_id;
	}

	public Integer getRequest_num() {
		return request_num;
	}

	public String getScene_id() {
		return scene_id;
	}

	public Integer getService_type() {
		return service_type;
	}

	public String getUid() {
		return uid;
	}

	public Integer getUid_type() {
		return uid_type;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}
	public void setGeo(Map<String, Object> geo) {
		this.geo = geo;
	}

	public void setMD5(String mD5) {
		MD5 = mD5;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public void setRequest_num(Integer request_num) {
		this.request_num = request_num;
	}
	public void setScene_id(String scene_id) {
		this.scene_id = scene_id;
	}

	public void setService_type(Integer service_type) {
		this.service_type = service_type;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setUid_type(Integer uid_type) {
		this.uid_type = uid_type;
	}

}
