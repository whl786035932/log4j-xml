/**
 * AdiMedia.java
 * cn.videoworks.cms.dto
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月28日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * ClassName:AdiMedia
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月28日		下午8:32:37
 *
 * @see 	 
 */
public class AdiMovieDto {

	/**
	 * 视频地址
	 */
	@NotBlank
	private String url;
	
	/**
	 * /0:未知,1:正片,2:预告片
	 */
	@NotNull
	private Integer type;
	
	/**
	 * 大小  kb
	 */
	@NotNull
	private Long size;
	
	/**
	 * 校验媒体是否丢失数据，校验码  例如md5等 
	 */
	@NotBlank
	private String check_sum;
	
	/**
	 * 文件名称
	 */
	@NotBlank
	public String file_name;
	
	/**
	 * 时长 单位豪秒
	 */
	@NotNull
	private Integer duration;
	
	/**
	 * 宽
	 */
	@NotNull
	private Integer width;
	
	/**
	 * 高
	 */
	@NotNull
	private Integer height;
	
	/**
	 * 码率
	 */
	@NotNull
	private Long bitrate;
	
	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Long getBitrate() {
		return bitrate;
	}

	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getCheck_sum() {
		return check_sum;
	}

	public void setCheck_sum(String check_sum) {
		this.check_sum = check_sum;
	}

}

