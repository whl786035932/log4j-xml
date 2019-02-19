/**
 * MediaDao.java
 * cn.videoworks.cms.dao
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月29日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Media;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

/**
 * ClassName:MediaDao
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月29日		下午1:59:05
 *
 * @see 	 
 */
@Repository
public class MediaDao extends AdvancedHibernateDao<Media>{
	
	public List<Media> list(String order,Page page) {
		Criteria criteria = super.getCurrentSession().createCriteria(Media.class);
		if(StringUtils.isNotBlank(order))
			criteria.addOrder(Order.desc(order));
		criteria.setFirstResult(page.getOffSet());
		criteria.setMaxResults(page.getSize());
		@SuppressWarnings("unchecked")
		List<Media> list = criteria.list();
		return list;
	}
	
	public long count() {
		Criteria criteria = super.getCurrentSession().createCriteria(Media.class);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().iterator().next();
	}
	

	@SuppressWarnings("unchecked")
	public List<Media> getByContentIdAndCdnStatus(Long contentId, int cdnStatus) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(Media.class,"media").createAlias("media.content", "content");
		createCriteria.add(Restrictions.eq("content.id", contentId));
		createCriteria.add(Restrictions.eq("cdn_sync_status", cdnStatus));
		return  createCriteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Media> getByContentId(Long contentId) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(Media.class,"media").createAlias("media.content", "content");
		createCriteria.add(Restrictions.eq("content.id", contentId));
		return  createCriteria.list();
	}
	
	/**
	 * 通过cdnkey 查询
	 * @param cdnKey
	 * @return
	 */
	@SuppressWarnings({"rawtypes" })
	public Media getByInnerKey(String innerKey) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(Media.class);
		createCriteria.add(Restrictions.eq("innerKey", innerKey));
		List list = createCriteria.list();
		if(null != list && list.size() >0)
			return (Media) list.get(0);
		return null;
	}
	
	@SuppressWarnings({"rawtypes" })
	public Media getByCdnKey(String cdnKey) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(Media.class);
		createCriteria.add(Restrictions.eq("cdnKey", cdnKey));
		List list = createCriteria.list();
		if(null != list && list.size() >0)
			return (Media) list.get(0);
		return null;
	}
}

