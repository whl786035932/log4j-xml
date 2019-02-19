package cn.videoworks.cms.dto;

import java.util.List;

public class CDNWorkerResponseDto {
	
	private Long taskId;
	private String msgid;
	private String contentId;
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	private List<CDNReturnDto> cdns;

	public List<CDNReturnDto> getCdns() {
		return cdns;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setCdns(List<CDNReturnDto> cdns) {
		this.cdns = cdns;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
}
