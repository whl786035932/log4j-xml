package cn.videoworks.cms.dto;

public class AreaDto {
	private int id;

	private String name;// 名称

	private int sequence;// 排序

	private String path;// 父级+当前ID

	private String parent;// 父级

	private String insertedAt;// 创建时间

	private String updatedAt;// 创建时间

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

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getInsertedAt() {
		return insertedAt;
	}

	public void setInsertedAt(String insertedAt) {
		this.insertedAt = insertedAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
}
