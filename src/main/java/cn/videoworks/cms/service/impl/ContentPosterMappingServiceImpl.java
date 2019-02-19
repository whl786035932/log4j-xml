/**
 * ContentPosterMappingServiceImpl.java
 * cn.videoworks.cms.service.impl
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月29日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.ContentPosterMappingDao;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.ContentPosterMapping;
import cn.videoworks.cms.service.ContentPosterMappingService;

/**
 * ClassName:ContentPosterMappingServiceImpl
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月29日		下午3:09:34
 *
 * @see 	 
 */
@Service
public class ContentPosterMappingServiceImpl implements ContentPosterMappingService{

	@Resource
	private ContentPosterMappingDao dao;
	
	@Override
	public void save(ContentPosterMapping contentPosterMapping) {
		dao.save(contentPosterMapping);
	}

	@Override
	public void delete(Long id) {
		dao.delete(id);
	}

	@Override
	public void update(ContentPosterMapping contentPosterMapping) {
		dao.update(contentPosterMapping);
	}

	@Override
	public ContentPosterMapping get(int id) {
		return dao.get(id);
	}

	@Override
	public List<ContentPosterMapping> list(String order, Page page) {
		return dao.list(order, page);
	}

	@Override
	public Page paging(Page page) {
		page.setRecordCount(dao.count());
		return page;
	}

	@Override
	public List<ContentPosterMapping> list(Long contentId) {
		return dao.list(contentId);
	}

	@Override
	public void deleteByCid(Long id) {
		String sql = "DELETE FROM ContentPosterMapping WHERE content.id = ?";
		dao.deleteByCid(sql, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<ContentPosterMapping> getByContentId(Long contentId) {
		return (Set<ContentPosterMapping>) dao.getByContentId(contentId);
	}

	@Override
	public List<ContentPosterMapping> getByContentId1(Long contentId) {
		return dao.getByContentId1(contentId);
	}
}

