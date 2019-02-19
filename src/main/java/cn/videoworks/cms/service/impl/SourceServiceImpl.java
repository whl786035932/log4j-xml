/**
 * SourceServiceImpl.java
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

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.SourceDao;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Source;
import cn.videoworks.cms.service.SourceService;

/**
 * ClassName:SourceServiceImpl
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月29日		下午3:01:47
 *
 * @see 	 
 */
@Service
public class SourceServiceImpl implements SourceService {

	@Resource
	private SourceDao dao;
	
	@Override
	public void save(Source source) {
		dao.save(source);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
	}

	@Override
	public void update(Source source) {
		dao.update(source);
	}

	@Override
	public Source get(int id) {
		return dao.get(id);
	}

	@Override
	public List<Source> list(String order) {
		return dao.list(order);
	}

	@Override
	public Source get(String name) {
		return dao.get(name);
	}

}

