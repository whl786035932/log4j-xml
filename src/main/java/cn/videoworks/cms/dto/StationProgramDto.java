package cn.videoworks.cms.dto;

public class StationProgramDto {

	private String element_type;
	private String id;
	private String action;
	
	private String asset_id;
	private String title;
	private String title_abbr;
	private String description;
	private String publish_time;
	private String last_shelves_time;
	private String source;
	private int type;
	private int status;
	private String cp;
	
	public String getElement_type() {
		return element_type;
	}
	public void setElement_type(String element_type) {
		this.element_type = element_type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getAsset_id() {
		return asset_id;
	}
	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle_abbr() {
		return title_abbr;
	}
	public void setTitle_abbr(String title_abbr) {
		this.title_abbr = title_abbr;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPublish_time() {
		return publish_time;
	}
	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}
	public String getLast_shelves_time() {
		return last_shelves_time;
	}
	public void setLast_shelves_time(String last_shelves_time) {
		this.last_shelves_time = last_shelves_time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	
}
