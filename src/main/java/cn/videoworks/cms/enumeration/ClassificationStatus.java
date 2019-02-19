package cn.videoworks.cms.enumeration;

public enum ClassificationStatus {
	FORBIDDEN(0, "禁用"), STARTUSE(1, "启用");
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

	private ClassificationStatus(int value, String name) {
		this.value = value;
		this.name = name;
	}
}
