package cn.videoworks.cms.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.entity.InstorageStatistics;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

@Repository
public class InstorageStatisticsDao extends AdvancedHibernateDao<InstorageStatistics> {

	@SuppressWarnings("unchecked")
	public List<InstorageStatistics> list(String date) {
		Criteria criteria = super.getCurrentSession().createCriteria(InstorageStatistics.class);
		 List<InstorageStatistics> list = null;
		try {
			if (StringUtils.isNotBlank(date)) {
				criteria.add(Restrictions.eq("statisticeAt", date));
			}
			criteria.add(Restrictions.isNull("parent.id"));
			list = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * get:(安装名称获取)
	 * @author   meishen
	 * @Date	 2018	2018年11月30日		下午6:51:44 
	 * @return InstorageStatistics    
	 * @throws
	 */
	public InstorageStatistics get(String name,String statisticsAt,InstorageStatistics parent) {
		Criteria criteria = super.getCurrentSession().createCriteria(InstorageStatistics.class);
		criteria.add(Restrictions.eq("name",name));
		criteria.add(Restrictions.eq("statisticeAt",statisticsAt));
		if(null != parent)
			criteria.add(Restrictions.eq("parent",parent));
		@SuppressWarnings("unchecked")
		List<InstorageStatistics> list = criteria.list();
		if(list.size()>0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<InstorageStatistics> getRoots(String dateBegin, String dateEnd) {
		Criteria criteria = super.getCurrentSession().createCriteria(InstorageStatistics.class);
		 List<InstorageStatistics> list = null;
		try {
			if (StringUtils.isNotBlank(dateBegin)) {
				criteria.add(Restrictions.ge("statisticeAt", dateBegin));
			}
			if (StringUtils.isNotBlank(dateEnd)) {
				criteria.add(Restrictions.le("statisticeAt", dateEnd));
			}
			criteria.add(Restrictions.isNull("parent.id"));
			criteria.addOrder(Order.desc("statisticeAt"));
			list = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
