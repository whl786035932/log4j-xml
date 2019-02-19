package cn.videoworks.cms.enumeration;

public enum ClassificationEdit {
	FORBIDDEN(0, "不可编辑"), STARTUSE(1, "可编辑");
	private int value;
	private String name;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private ClassificationEdit(int value, String name) {
		this.value = value;
		this.name = name;
	}
}
