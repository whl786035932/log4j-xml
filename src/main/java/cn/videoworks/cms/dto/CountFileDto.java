package cn.videoworks.cms.dto;

public class CountFileDto {
	//用户名
	//用户卡号
	private String deviceId;
	//播放节目
	private String playContent;
	//播放时间
	private String playTime;
	//播放时长
	private Integer playDuration;
	private String category;
	private String assetId;
	public String getAssetId() {
		return assetId;
	}
	public String getCategory() {
		return category;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public String getPlayContent() {
		return playContent;
	}
	public Integer getPlayDuration() {
		return playDuration;
	}
	public String getPlayTime() {
		return playTime;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public void setPlayContent(String playContent) {
		this.playContent = playContent;
	}
	public void setPlayDuration(Integer playDuration) {
		this.playDuration = playDuration;
	}
	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}
	
	
	
	
}
