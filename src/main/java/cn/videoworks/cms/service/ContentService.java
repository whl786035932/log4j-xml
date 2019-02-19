/**
 * ContentService.java
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
import cn.videoworks.cms.entity.Content;

/**
 * ClassName:ContentService
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月28日		下午8:21:09
 *
 * @see 	 
 */
public interface ContentService {

	void save(Content content);
	void delete(Long id);
	void update(Content content);
	Content get(Long id);
	 Content get(String title,int status) ;
	Content get(String title,String publishTime,String cp,String sourceChannel,String sourceColumn);
	
	List<Content> list(String beginTime);
	
	/**
	 * 内容管理查询
	 */
	List<Content> list(Map<String,Object> q,String order,String sort,Page page);
	Page paging(Map<String,Object> q,Page page);
	
	
	/**
	 * list:(查询内容小于发布时间，且状态是删除的内容)
	 * @author   meishen
	 * @Date	 2018	2018年9月10日		下午2:38:30 
	 * @return List<Content>    
	 * @throws
	 */
	List<Content> list(String publishTime,Page page);
	
	//api
	List<Content> findByTitleAbbr(String keyword,Integer limit, String publish_time);
	
	Integer count(String title_abbr);
	List<Content> findByChannleColumnPublishTime(String channel, String column, Integer limit, String publish_time);
}

