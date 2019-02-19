/**
 * PosterService.java
 * cn.videoworks.cms.service
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月28日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.service;

import java.util.List;
import java.util.Map;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Poster;

/**
 * ClassName:PosterService
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月28日		下午2:13:53
 *
 * @see 	 
 */
public interface PosterService {

	void save(Poster poster);
	void delete(Long id);
	void update(Poster poster);
	Poster get(Long id);
	List<Poster> list(Map<String,String> q,String order,Page page);
	Page paging(Map<String,String> q,Page page);
}

