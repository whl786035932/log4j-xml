package cn.videoworks.cms.enumeration;

public enum ClassificationDele {
	FORBIDDEN(0, "不可删除"), STARTUSE(1, "可删除");
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

	private ClassificationDele(int value, String name) {
		this.value = value;
		this.name = name;
	}
}
