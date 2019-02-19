/**
 * ContentPosterMapping.java
 * cn.videoworks.cms.entity
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月29日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * ClassName:ContentPosterMapping
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月29日		下午3:04:46
 *
 * @see 	 
 */
@Entity
@Table(name = "cms_content_poster_mapping")
public class ContentPosterMapping implements Serializable {

	/**
	 *  主键
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long id;

	/**
	 * 内容id
	 */
	@JoinColumn(name="content_id")
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	private Content content;
	
	/**
	 * 海报id
	 */
	@JoinColumn(name="poster_id")
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	private Poster poster;
	
	/**
	 * 未知
	 */
	@Column(name = "position")
	private Integer position;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public Poster getPoster() {
		return poster;
	}

	public void setPoster(Poster poster) {
		this.poster = poster;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
	
}

