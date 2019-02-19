package cn.videoworks.cms.enumeration;

public enum ClassificationType {
	COMMON(0, "普通"), SPECIAL(1, "专题"),MULTILTVEL(2, "多级");
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

	private ClassificationType(int value, String name) {
		this.value = value;
		this.name = name;
	}
}
