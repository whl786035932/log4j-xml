package cn.videoworks.cms.enumeration;

public enum ClassificationRecommend {

	NOTRECOMMEND(0, "不推荐"), 
	RECOMMEND(1, "推荐");
	
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

	private ClassificationRecommend(int value, String name) {
		this.value = value;
		this.name = name;
	}
}
