package cn.videoworks.cms.vo;

import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * @author   meishen
 * @Date	 2018	2018年9月18日		下午3:17:28
 * @Description 方法描述: 菜单web页面结构
 */
public class MenuVo implements Comparable<MenuVo>{

	private int id;
	private String name;
	private String url;
	private int sequence;
	private String parent;
	private String menuClass;
	private List<MenuVo> children;
	
	public String getMenuClass() {
		return menuClass;
	}
	public void setMenuClass(String menuClass) {
		this.menuClass = menuClass;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	private Boolean checked;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public List<MenuVo> getChildren() {
		return children;
	}
	public void setChildren(List<MenuVo> children) {
		this.children = children;
	}
	@Override
	public int compareTo(MenuVo o) {
		 return new CompareToBuilder().append(sequence, o.sequence).toComparison();
	}
}
