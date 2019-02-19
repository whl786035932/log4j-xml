/**
 * PosterServiceImpl.java
 * cn.videoworks.cms.service.impl
 *
 * Function： TODO 
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

import cn.videoworks.cms.dao.PosterDao;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Poster;
import cn.videoworks.cms.service.PosterService;

/**
 * ClassName:PosterServiceImpl
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月28日		下午2:18:58
 *
 * @see 	 
 */
@Service
public class PosterServiceImpl implements PosterService {
	
	@Resource
	private PosterDao dao;
	
	@Override
	public void save(Poster poster) {
		dao.save(poster);
	}

	@Override
	public void delete(Long id) {
		dao.delete(id);
	}

	@Override
	public void update(Poster poster) {
		dao.update(poster);
	}

	@Override
	public Poster get(Long id) {
		return dao.get(id);
	}

	@Override
	public List<Poster> list(Map<String,String> q,String order, Page page) {
		return dao.list(q,order, page);
	}

	@Override
	public Page paging(Map<String,String> q,Page page) {
		page.setRecordCount(dao.count(q));
		return page;
	}

}

