package cn.videoworks.cms.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
@Table(name = "cms_task")
public class Task {
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long id; // 主键
	@ManyToOne
	@JoinColumn(name = "content_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Content content; // 内容

	@Column(name = "type")
	public int type; // 1:cdn入库，2：xxx

	@Column(name = "status")
	public Integer  status; // 默认0，0：初始化1：成功2：失败3：进行中
	
	@Column(name="data")
	private String data;
	
	@Column(name = "description")
	public String description; // 任务描述

	@Column(name = "inserted_at")
	public Timestamp inserted_at; //

	@Column(name = "updated_at")
	public Timestamp updated_at; // 插入时间
	
	
	@Column(name="msgid")
	private String msgid;
	
	@Column(name="message")
	private String message;
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public Content getContent() {
		return content;
	}

	public String getData() {
		return data;
	}

	public String getDescription() {
		return description;
	}

	public Long getId() {
		return id;
	}

	public Timestamp getInserted_at() {
		return inserted_at;
	}

	public Integer getStatus() {
		return status;
	}

	public int getType() {
		return type;
	}

	public Timestamp getUpdated_at() {
		return updated_at;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setInserted_at(Timestamp inserted_at) {
		this.inserted_at = inserted_at;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setUpdated_at(Timestamp updated_at) {
		this.updated_at = updated_at;
	}

}
