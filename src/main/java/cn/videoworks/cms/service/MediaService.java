/**
 * MediaService.java
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

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Media;

/**
 * ClassName:MediaService
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月29日		下午1:59:44
 *
 * @see 	 
 */
public interface MediaService {
	void save(Media media);
	void delete(int id);
	void update(Media media);
	Media get(Long id);
	List<Media> list(String order,Page page);
	Page paging(Page page);
	List<Media> getByContentIdAndCdnStatus(Long id, int value);
	List<Media> getByContentId(Long id);
	
	Media getByInnerKey(String innerKey);
	
	Media getByCdnKey(String cdnKey);
}

