package cn.videoworks.cms.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 智能推荐的返回结果
 * @author whl
 *
 */
public class IntellRecommendRespDto  implements Serializable{
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getScene_id() {
		return scene_id;
	}

	public void setScene_id(String scene_id) {
		this.scene_id = scene_id;
	}

	

	public IntellRecommendRespDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}



	private Integer code;
	private String scene_id;
	private String msg;
	private List<String> data;
}
