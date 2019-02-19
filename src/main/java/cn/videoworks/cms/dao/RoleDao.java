package cn.videoworks.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Role;
import cn.videoworks.cms.entity.User;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

@Repository
public class RoleDao  extends AdvancedHibernateDao<Role>{

	public List<Role> list(int valid_status) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(Role.class,"role");
		return createCriteria.list();
	}

	public List<Role> list(Map<String, Object> q, String orderField, Page page) {
		Criteria criteria = super.getCurrentSession().createCriteria(Role.class,"role");
		criteria.setFirstResult(page.getOffSet());
		criteria.setMaxResults(page.getSize());
		List<Role> list = criteria.list();
		return list;
	}

}
