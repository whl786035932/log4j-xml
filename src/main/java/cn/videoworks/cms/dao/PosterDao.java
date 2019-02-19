/**
 * Poster.java
 * cn.videoworks.cms.dao
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月28日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Poster;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

/**
 * ClassName:Poster
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月28日		下午2:11:24
 *
 * @see 	 
 */
@Repository
public class PosterDao extends AdvancedHibernateDao<Poster>{

	@SuppressWarnings("unchecked")
	public List<Poster> list(Map<String,String> q,String order,Page page) {
		String source = null;
		if(null != q) {
			source = q.containsKey("source")==true?q.get("source").toString():"";
		}
		
		Criteria criteria = super.getCurrentSession().createCriteria(Poster.class);
		if(StringUtils.isNotBlank(source)) criteria.add(Restrictions.eq("source", source));
		if(StringUtils.isNotBlank(order))  criteria.addOrder(Order.desc(order));
		if(null != page) {
			criteria.setFirstResult(page.getOffSet());
			criteria.setMaxResults(page.getSize());
    	}
		List<Poster> list = criteria.list();
		return list;
	}
	
	public long count(Map<String,String> q) {
		Criteria criteria = super.getCurrentSession().createCriteria(Poster.class);
		
		String source = null;
		if(null != q) {
			source = q.containsKey("source")==true?q.get("source").toString():"";
		}
		
	    if(StringUtils.isNotBlank(source))
		    criteria.add(Restrictions.eq("source",source));
		  
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().iterator().next();
	}
}

