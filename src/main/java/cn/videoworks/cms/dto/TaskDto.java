package cn.videoworks.cms.dto;

public class TaskDto {
	private Long id;
	private String title;
	private String status;
	
	private String publish_time;

	private String source;

	//cdn回调时间
	private String updated_at;
	
	//入站时间
	private String inserted_at;
	private String ruzhan_time;//创建时间 兼容错误
	
	private String message;

	public String getRuzhan_time() {
		return ruzhan_time;
	}

	public void setRuzhan_time(String ruzhan_time) {
		this.ruzhan_time = ruzhan_time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getInserted_at() {
		return inserted_at;
	}

	public void setInserted_at(String inserted_at) {
		this.inserted_at = inserted_at;
	}

	public Long getId() {
		return id;
	}

	public String getPublish_time() {
		return publish_time;
	}
	public String getSource() {
		return source;
	}
	
	public String getStatus() {
		return status;
	}

	public String getTitle() {
		return title;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	

	

	public void setId(Long id) {
		this.id = id;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	
}
