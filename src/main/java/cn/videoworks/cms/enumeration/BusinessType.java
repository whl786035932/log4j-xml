package cn.videoworks.cms.enumeration;

public enum BusinessType {

	ADD("添加","添加"),
	DELETE("删除","删除"),
	GET("查询","查询"),
	UPDATE("修改","修改"),
	UP("上架","上架"),
	ORDERCLASSIFICATION("分类排序","分类排序"),
	REMOVECLASSIFICATION("移除分类","移除分类"),
	DOWN("下架","下架"),
	ADDCLASSIFICATION("添加分类","添加分类"),
	SYNCBOOTHS("同步站点","同步站点"),
	WRITESTORAGE("注入存储","注入存储"),
	WRITECDN("注入CDN","注入CDN"),
	WRITECMS("注入CMS","注入CMS");
	private String value;
	private String name;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private BusinessType(String value, String name){
		this.value=value;
		this.name=name;
	}
}
