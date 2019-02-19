/**
 * SourceService.java
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
import cn.videoworks.cms.entity.Source;

/**
 * ClassName:SourceService
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月29日		下午3:00:57
 *
 * @see 	 
 */
public interface SourceService {

	void save(Source source);
	void delete(int id);
	void update(Source source);
	Source get(int id);
	Source get(String name);
	List<Source> list(String order);
}

