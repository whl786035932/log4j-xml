package cn.videoworks.cms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="cms_operation")
public class Operation {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	
	@Column(name="name")
	private String name;

	@Column(name="code")
	private String code;

	@Column(name="url")
	private String url;

	public List<PermissionOperatoinMapping> getPermissionOperationMappings() {
		return permissionOperationMappings;
	}


	public void setPermissionOperationMappings(List<PermissionOperatoinMapping> permissionOperationMappings) {
		this.permissionOperationMappings = permissionOperationMappings;
	}

	@Column(name="inserted_at")
	private Date inserted_at;

	@Column(name="updated_at")
	private Date updated_at;

	@Column(name="parent")
	private Integer parent;

	@Column(name="sequence")
	private Integer sequence;
	
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,mappedBy="operation",orphanRemoval=true,fetch=FetchType.LAZY)
	private List<PermissionOperatoinMapping> permissionOperationMappings;
	
	
	public String getCode() {
		return code;
	}
	
	
	public Integer getId() {
		return id;
	}
	
	public Date getInserted_at() {
		return inserted_at;
	}

	public String getName() {
		return name;
	}

	public Integer getParent() {
		return parent;
	}

	public Integer getSequence() {
		return sequence;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public String getUrl() {
		return url;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInserted_at(Date inserted_at) {
		this.inserted_at = inserted_at;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	
	
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
