package cn.videoworks.cms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 * @author   meishen
 * @Date	 2018	2018年9月18日		上午9:45:08
 * @Description 方法描述: 菜单实体
 */
@Entity
@Table(name="cms_menu")
public class Menu {
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * 名称
	 */
	@Column(name="name")
	private String name;
	
	/**
	 * 父级
	 */
	@Column(name = "parent")
	private Integer parent;
	
	/**
	 * 访问地址
	 */
	@Column(name="url")
	private String url;
	
	/**
	 * 排序
	 */
	@Column(name="sequence")
	private int sequence;
	
	/**
	 * 菜单样式
	 */
	@Column(name="menu_class")
	private String menu_class;
	
	/**
	 * 创建时间
	 */
	@Column(name="inserted_at")
	private String inserted_at;
	
	/**
	 * 修改时间
	 */
	@Column(name="updated_at")
	private String updated_at;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="menu",orphanRemoval=true,fetch=FetchType.LAZY)
	private List<PermissionMenuMapping> permissionMenuMappings;
	
	public String getMenu_class() {
		return menu_class;
	}

	public void setMenu_class(String menu_class) {
		this.menu_class = menu_class;
	}

	public List<PermissionMenuMapping> getPermissionMenuMappings() {
		return permissionMenuMappings;
	}

	public void setPermissionMenuMappings(List<PermissionMenuMapping> permissionMenuMappings) {
		this.permissionMenuMappings = permissionMenuMappings;
	}

	public Menu() {
		
	}

	public Menu(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
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

	public String getInserted_at() {
		return inserted_at;
	}

	public void setInserted_at(String inserted_at) {
		this.inserted_at = inserted_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

}
