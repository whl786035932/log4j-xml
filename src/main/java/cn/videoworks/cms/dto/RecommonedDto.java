package cn.videoworks.cms.dto;

import java.util.List;


public class RecommonedDto {
	private Long oldId;
	private Long id;
	private Integer recommoned;
	private String url;
	private String title;
	private String size;
	private String wh;
	private List<RecommonedDto> dtos;
	public Long getOldId() {
		return oldId;
	}

	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRecommoned() {
		return recommoned;
	}

	public void setRecommoned(Integer recommoned) {
		this.recommoned = recommoned;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getWh() {
		return wh;
	}

	public void setWh(String wh) {
		this.wh = wh;
	}

	public List<RecommonedDto> getDtos() {
		return dtos;
	}

	public void setDtos(List<RecommonedDto> dtos) {
		this.dtos = dtos;
	}
}
