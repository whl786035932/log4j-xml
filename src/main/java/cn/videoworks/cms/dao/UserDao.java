package cn.videoworks.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.User;
import cn.videoworks.cms.enumeration.Status;
import cn.videoworks.cms.enumeration.UserType;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

@Repository
public class UserDao  extends AdvancedHibernateDao<User>{

	@SuppressWarnings("unchecked")
	public List<User> list(Map<String, Object> q, String orderField, Page page) {
		Criteria criteria = super.getCurrentSession().createCriteria(User.class,"user");
		String username = q.containsKey("username")==true?q.get("username").toString():"";
		String status = q.containsKey("status")==true?q.get("status").toString():"";
		String nickname = q.containsKey("nickname")==true?q.get("nickname").toString():"";
		if(StringUtils.isNotBlank(username)) criteria.add(Restrictions.like("user.username", "%"+username+"%"));
		if(StringUtils.isNotBlank(status)) criteria.add(Restrictions.eq("status",Integer.parseInt(status)));
		if(StringUtils.isNotBlank(nickname)) criteria.add(Restrictions.like("nickname","%"+nickname+"%"));
		
		criteria.add(Restrictions.eq("type",UserType.UNSUPER.getValue()));//不获取超级管理员
		
		criteria.setFirstResult(page.getOffSet());
		criteria.setMaxResults(page.getSize());
		List<User> list = criteria.list();
		return list;
	}

	public long count(Map<String, Object> q) {
		Criteria criteria = super.getCurrentSession().createCriteria(User.class,"user");
		String username = q.containsKey("username")==true?q.get("username").toString():"";
		String status = q.containsKey("status")==true?q.get("status").toString():"";
		String nickname = q.containsKey("nickname")==true?q.get("nickname").toString():"";
		

		if(StringUtils.isNotBlank(username)) criteria.add(Restrictions.like("user.username", "%"+username+"%"));
		if(StringUtils.isNotBlank(status)) criteria.add(Restrictions.eq("status",Integer.parseInt(status)));
		if(StringUtils.isNotBlank(nickname)) criteria.add(Restrictions.like("nickname","%"+nickname+"%"));
		
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().iterator().next();
	}

	@SuppressWarnings("rawtypes")
	public User getByNameAndPassword(String username, String password) {
		Criteria criteria = super.getCurrentSession().createCriteria(User.class,"user");
		criteria.add(Restrictions.eq("username", username));
		criteria.add(Restrictions.eq("password", password));
		criteria.add(Restrictions.eq("status", Status.VALID.getValue()));
		List list = criteria.list();
		if(list!=null&&list.size()>0) {
			return (User) list.get(0);
		}
		return null;
	}

	/**
	 * getUser:(初始化超级管理员查询)
	 * @author   meishen
	 * @Date	 2018	2018年9月26日		下午6:18:33 
	 * @return User    
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public User getUser(String username, int type) {
		Criteria criteria = super.getCurrentSession().createCriteria(User.class,"user");
		criteria.add(Restrictions.eq("username", username));
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.eq("status", Status.VALID.getValue()));
		List list = criteria.list();
		if(list!=null&&list.size()>0) {
			return (User) list.get(0);
		}
		return null;
	}
}
