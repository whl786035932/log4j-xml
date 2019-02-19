package cn.videoworks.cms.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.entity.Operation;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

@Repository
public class OperationDao extends AdvancedHibernateDao<Operation> {

	public List<Operation> getChildOperationByParentId(Integer parentId) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(Operation.class,"operation");
		 createCriteria.add(Restrictions.eq("parent", parentId));
		
		return createCriteria.list();
	}

	public List<Operation> list(String orderField) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(Operation.class,"operation");

		createCriteria.addOrder(Order.asc(orderField));
		return createCriteria.list();
	}

	public void deleteAll() {
		String sql ="delete from cms_operation ";
		Query querySql = this.getCurrentSession().createSQLQuery(sql);
		querySql.executeUpdate();
	}

}
