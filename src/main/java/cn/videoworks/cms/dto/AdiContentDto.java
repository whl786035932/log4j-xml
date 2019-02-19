/**
 * AdiContent.java
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

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * ClassName:AdiContent
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月28日		下午8:32:17
 *
 * @see 	 
 */
public class AdiContentDto {
	/**
	 * 媒资id
	 */
	@NotBlank
	private String asset_id;
	
	/**
	 * 标题
	 */
	@NotBlank
	private String title;
	
	/**
	 * 标题首字母
	 */
	@NotBlank
	private String title_abbr;
	
	/**
	 * 内容类型，1:视频,2:专题,3:广告  //必填
	 */
	@NotNull
	private Integer type;
	
	/**
	 * 描述
	 */
	@NotNull
	private String description;
	
	/**
	 * 播放时间，毫秒
	 */
	@NotNull
	private Long publish_time;
	
	
	/**
	 * 标签
	 */
	@NotNull
	private List<String> tags;
	
	/**
	 * 指定需要绑定的分类
	 */
	@NotNull
	private List<String> classifications;
	
	/**
	 * 指定需要绑定的地区
	 */
	@NotNull
	private List<String> areas;
	
	/**
	 * 内容提供商
	 */
	@NotBlank
	private String cp;
	
	/**
	 * 内容来源，比如视频工厂、媒资  //必填
	 */
	@NotBlank
	private String source;
	
	/**
	 * 海报   第三方上传有可能没有海报图片
	 */
	@NotNull
	private List<AdiImageDto> images;
	
	/**
	 * 媒体
	 */
	@NotEmpty
	private List<AdiMovieDto> movies;
	
	/**
	 * 来源频道 
	 */
	private String source_channel;
	
	/**
	 * 来源栏目
	 */
	private String source_column;
	
	public String getSource_channel() {
		return source_channel;
	}

	public void setSource_channel(String source_channel) {
		this.source_channel = source_channel;
	}

	public String getSource_column() {
		return source_column;
	}

	public void setSource_column(String source_column) {
		this.source_column = source_column;
	}

	public List<String> getClassifications() {
		return classifications;
	}

	public void setClassifications(List<String> classifications) {
		this.classifications = classifications;
	}

	public List<String> getAreas() {
		return areas;
	}

	public void setAreas(List<String> areas) {
		this.areas = areas;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(Long publish_time) {
		this.publish_time = publish_time;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<AdiImageDto> getImages() {
		return images;
	}

	public void setImages(List<AdiImageDto> images) {
		this.images = images;
	}

	public List<AdiMovieDto> getMovies() {
		return movies;
	}

	public void setMovies(List<AdiMovieDto> movies) {
		this.movies = movies;
	}

}

