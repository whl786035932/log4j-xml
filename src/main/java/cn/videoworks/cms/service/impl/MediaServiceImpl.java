/**
 * MediaServiceImpl.java
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

import cn.videoworks.cms.dao.MediaDao;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Media;
import cn.videoworks.cms.service.MediaService;

/**
 * ClassName:MediaServiceImpl
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月29日		下午2:00:32
 *
 * @see 	 
 */
@Service
public class MediaServiceImpl implements MediaService {

	@Resource
	private MediaDao dao;
	
	@Override
	public void save(Media media) {
		dao.save(media);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
	}

	@Override
	public void update(Media media) {
		dao.update(media);
	}

	@Override
	public Media get(Long id) {
		return dao.get(id);
	}

	@Override
	public List<Media> list(String order, Page page) {
		return dao.list(order, page);
	}

	@Override
	public Page paging(Page page) {
		page.setRecordCount(dao.count());
		return page;
	}
	
	@Override
	public List<Media> getByContentIdAndCdnStatus(Long contentId, int cdnStatus) {
		return dao.getByContentIdAndCdnStatus(contentId,cdnStatus);
	}

	@Override
	public List<Media> getByContentId(Long id) {
		return dao.getByContentId(id);
	}

	@Override
	public Media getByInnerKey(String innerKey) {
		return dao.getByInnerKey(innerKey);
	}

	@Override
	public Media getByCdnKey(String cdnKey) {
		return dao.getByCdnKey(cdnKey);
	}


}

