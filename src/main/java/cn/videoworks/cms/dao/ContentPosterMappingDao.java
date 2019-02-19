/**
 * ContentPosterMappingDao.java
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
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.ContentPosterMapping;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

/**
 * ClassName:ContentPosterMappingDao
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月29日		下午3:07:25
 *
 * @see 	 
 */
@Repository
public class ContentPosterMappingDao extends AdvancedHibernateDao<ContentPosterMapping> {

	public List<ContentPosterMapping> list(String order,Page page) {
		Criteria criteria = super.getCurrentSession().createCriteria(ContentPosterMapping.class);
		if(StringUtils.isNotBlank(order))
			criteria.addOrder(Order.desc(order));
		criteria.setFirstResult(page.getOffSet());
		criteria.setMaxResults(page.getSize());
		@SuppressWarnings("unchecked")
		List<ContentPosterMapping> list = criteria.list();
		return list;
	}
	
	public long count() {
		Criteria criteria = super.getCurrentSession().createCriteria(ContentPosterMapping.class);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	public List<ContentPosterMapping> list(Long contentId ) {
		Criteria criteria = super.getCurrentSession().createCriteria(ContentPosterMapping.class,"contentPosterMapping").createAlias("contentPosterMapping.content","content");
		criteria.add(Restrictions.eq("content.id",contentId ));
		List<ContentPosterMapping> list = criteria.list();
		return list;
	}

	public void deleteByCid(String sql, Object... params) {
		try {
			Session session = this.getCurrentSession();
			Query queryupdate = session.createQuery(sql);
			for (int i = 0; i < params.length; i++) {
				queryupdate.setParameter(i, params[i]);
			}
			int a = queryupdate.executeUpdate();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ContentPosterMapping> getByContentId(Long contentId) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(ContentPosterMapping.class,"mapping").createAlias("mapping.content", "content").createAlias("mapping.poster", "poster");;
		createCriteria.add(Restrictions.eq("content.id", contentId));
		createCriteria.add(Restrictions.isNotNull("poster.sourceUrl"));
		return createCriteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ContentPosterMapping> getByContentId1(Long contentId) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(ContentPosterMapping.class,"mapping").createAlias("mapping.content", "content").createAlias("mapping.poster", "poster");;
		createCriteria.add(Restrictions.eq("content.id", contentId));
		return createCriteria.list();
	}
}

