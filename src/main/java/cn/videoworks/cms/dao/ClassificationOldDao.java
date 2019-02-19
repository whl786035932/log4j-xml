package cn.videoworks.cms.dao;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.entity.ClassificationOld;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

@Repository
public class ClassificationOldDao extends AdvancedHibernateDao<ClassificationOld> {
	public List<ClassificationOld> getAll(){
	try {
		Criteria criteria = super.getCurrentSession().createCriteria(ClassificationOld.class);
		criteria.addOrder(Order.asc("lft"));
		@SuppressWarnings("unchecked")
		List<ClassificationOld> list = criteria.list();
		if (list.size() > 0)
			return list;
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return new ArrayList<ClassificationOld>();
	}
}
