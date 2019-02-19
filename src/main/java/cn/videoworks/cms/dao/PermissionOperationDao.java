package cn.videoworks.cms.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.entity.PermissionOperatoinMapping;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

@Repository
public class PermissionOperationDao  extends AdvancedHibernateDao<PermissionOperatoinMapping>{

	public void deleteAll() {
		String sql ="delete from cms_permissions_operation ";
		Query querySql = this.getCurrentSession().createSQLQuery(sql);
		querySql.executeUpdate();
	}

}
