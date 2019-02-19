package cn.videoworks.cms.enumeration;

public enum TaskStatus {
	ONGOING(0, "进行中"), SUCCESS(1, "成功"), ERROR(2, "失败");
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

	private TaskStatus(int value, String name) {
		this.value = value;
		this.name = name;
	}
}
