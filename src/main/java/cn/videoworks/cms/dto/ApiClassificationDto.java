package cn.videoworks.cms.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(value = { "lft", "rgt" })
public class ApiClassificationDto {

	private String id;
	private String name;
	private String icon;
	private Integer status;
	private Integer type;
	private Long lft;
	private Long rgt;
	private String pId;// 父id
	private Integer level; // 层级
	private Integer sequence; // 排序
	private String alias; // 别名

	private String path; // 栏目的全路径
	
	private Integer recommend;//是否推荐

	@JsonInclude(Include.NON_NULL)
	private List<ApiContentDto> contents;
	
	private List<ApiClassificationDto> children = new ArrayList<ApiClassificationDto>();
	
	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}

	public String getAlias() {
		return alias;
	}

	public List<ApiClassificationDto> getChildren() {
		return children;
	}

	public List<ApiContentDto> getContents() {
		return contents;
	}

	public String getIcon() {
		return icon;
	}

	public String getId() {
		return id;
	}

	public Integer getLevel() {
		return level;
	}

	public Long getLft() {
		return lft;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getpId() {
		return pId;
	}

	public Long getRgt() {
		return rgt;
	}

	public Integer getSequence() {
		return sequence;
	}

	public Integer getStatus() {
		return status;
	}

	public Integer getType() {
		return type;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setChildren(List<ApiClassificationDto> children) {
		this.children = children;
	}

	public void setContents(List<ApiContentDto> contents) {
		this.contents = contents;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setLft(Long lft) {
		this.lft = lft;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public void setRgt(Long rgt) {
		this.rgt = rgt;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
