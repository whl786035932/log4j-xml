package cn.videoworks.cms.dto;

import cn.videoworks.cms.dto.Page;

public class SpecailTopDto {
	public Integer id;// 专题id
	public String title;// 专题名
	public Integer status;// 状态
	public Page page;// 分页信息

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
