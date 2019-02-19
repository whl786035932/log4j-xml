package cn.videoworks.cms.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Permission;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

@Repository
public class PermissionDao extends AdvancedHibernateDao<Permission> {

	public List<Permission> list(String orderField, Page page) {
		Criteria criteria = super.getCurrentSession().createCriteria(Permission.class,"permission");
		criteria.setFirstResult(page.getOffSet());
		criteria.setMaxResults(page.getSize());
		List<Permission> list = criteria.list();
		return list;
	}
	
}
