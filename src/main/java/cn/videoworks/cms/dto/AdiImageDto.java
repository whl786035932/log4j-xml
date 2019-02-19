/**
 * AdiPoster.java
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
 * ClassName:AdiPoster
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月28日		下午8:32:27
 *
 * @see 	 
 */
public class AdiImageDto {
	
	/**
	 * 海报地址
	 */
	@NotBlank
	private String url;
	
	/**
	 * 0:未知,1:海报,2:XXX
	 */
	@NotNull
	private Integer type;
	
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
	 * 大小  kb
	 */
	@NotNull
	private Integer size;
	
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

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getCheck_sum() {
		return check_sum;
	}

	public void setCheck_sum(String check_sum) {
		this.check_sum = check_sum;
	}

}

