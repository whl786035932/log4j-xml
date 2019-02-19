/**
 * ContentServiceImpl.java
 * cn.videoworks.cms.service.impl
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月28日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.ContentDao;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Content;
import cn.videoworks.cms.service.ContentService;

/**
 * ClassName:ContentServiceImpl
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月28日		下午8:22:04
 *
 * @see 	 
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Resource
	private ContentDao dao;
	
	@Override
	public void save(Content content) {
		dao.save(content);
	}

	@Override
	public void delete(Long id) {
		dao.delete(id);
	}

	@Override
	public void update(Content content) {
		dao.update(content);
	}

	@Override
	public Content get(Long id) {
		return dao.get(id);
	}

	@Override
	public List<Content> list(Map<String,Object> q,String order,String sort, Page page) {
		return dao.list(q,order,sort, page);
	}

	@Override
	public Page paging(Map<String,Object> q,Page page) {
		page.setRecordCount(dao.count(q));
		return page;
	}

	@Override
	public Content get(String title,String publishTime,String cp,String sourceChannel,String sourceColumn) {
		return dao.get(title, publishTime, cp, sourceChannel, sourceColumn);
	}

	@Override
	public List<Content> list(String beginTime) {
		return dao.list(beginTime);
	}

	/**
	 * list:(查询内容小于发布时间，且状态是删除的内容)
	 * @author   meishen
	 * @Date	 2018	2018年9月10日		下午2:38:30 
	 * @return List<Content>    
	 * @throws
	 */
	@Override
	public List<Content> list(String publishTime, Page page) {
		return dao.list(publishTime, page);
	}
	
	//api
	@Override
	public List<Content> findByTitleAbbr(String keyword,Integer limit, String publish_time) {
		return  dao.findByTitleAbbr(keyword,limit, publish_time);
	}
	
	
	@Override
	public Integer count(String title_abbr) {
		return dao.count(title_abbr);
	}

	@Override
	public List<Content> findByChannleColumnPublishTime(String channel, String column, Integer limit,
			String publish_time) {
		return dao.findByChannleColumnPublishTime(channel,column,limit,publish_time);
	}

	@Override
	public Content get(String title, int status) {
		return dao.get(title, status);
	}

}

