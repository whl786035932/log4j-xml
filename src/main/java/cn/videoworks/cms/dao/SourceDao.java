/**
 * SourceDao.java
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
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.entity.Source;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

/**
 * ClassName:SourceDao
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月29日		下午2:59:54
 *
 * @see 	 
 */
@Repository
public class SourceDao extends AdvancedHibernateDao<Source> {

	/**
	 * get:(根据来源名称获取来源)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月29日		下午4:56:44
	 * @param assetId
	 * @return   
	 * @return Content    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public Source get(String name) {
		Criteria criteria = super.getCurrentSession().createCriteria(Source.class);
		if(StringUtils.isNotBlank(name))
			criteria.add(Restrictions.eq("name",name));
		@SuppressWarnings("unchecked")
		List<Source> list = criteria.list();
		if(list.size()>0)
			return list.get(0);
		return null;
	}
	
	/**
	 * list:(获取来源列表)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月30日		下午5:41:01
	 * @param order
	 * @return   
	 * @return List<Source>    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public List<Source> list(String order) {
		Criteria criteria = super.getCurrentSession().createCriteria(Source.class);
		if(StringUtils.isNotBlank(order))
			criteria.addOrder(Order.desc(order));
		List<Source> list = criteria.list();
		return list;
	}
	
}

