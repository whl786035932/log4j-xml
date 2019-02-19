/**
 * ContentPosterMappingService.java
 * cn.videoworks.cms.service
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月29日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.service;

import java.util.List;
import java.util.Set;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.ContentPosterMapping;

/**
 * ClassName:ContentPosterMappingService
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月29日		下午3:08:35
 *
 * @see 	 
 */
public interface ContentPosterMappingService {

	void save(ContentPosterMapping contentPosterMapping);
	void delete(Long id);
	void update(ContentPosterMapping contentPosterMapping);
	ContentPosterMapping get(int id);
	List<ContentPosterMapping> list(String order,Page page);
	Page paging(Page page);
	
	List<ContentPosterMapping> list(Long contentId );
	void deleteByCid(Long id);
	
	Set<ContentPosterMapping> getByContentId(Long contentId);
	List<ContentPosterMapping> getByContentId1(Long contentId);
}

