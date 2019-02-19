package cn.videoworks.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.ApkVersion;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

@Repository 
public class ApkVersionDao  extends AdvancedHibernateDao<ApkVersion>{

	@SuppressWarnings("unchecked")
	public List<ApkVersion> list(Map<String, Object> q, String orderField, Page page) {
		Criteria criteria = super.getCurrentSession().createCriteria(ApkVersion.class,"apkversion");
		String type = q.containsKey("type")==true?q.get("type").toString():"";
		if(StringUtils.isNotBlank(type)) criteria.add(Restrictions.eq("type",Integer.parseInt(type)));
		criteria.setFirstResult(page.getOffSet());
		criteria.setMaxResults(page.getSize());
		criteria.addOrder(Order.desc(orderField));
		List<ApkVersion> list = criteria.list();
		return list;
	}

	public long count(Map<String, Object> q) {
		Criteria criteria = super.getCurrentSession().createCriteria(ApkVersion.class,"apkversion");
		String type = q.containsKey("type")==true?q.get("type").toString():"";
		if(StringUtils.isNotBlank(type)) criteria.add(Restrictions.eq("type",Integer.parseInt(type)));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().iterator().next();
	}

	@SuppressWarnings("unchecked")
	public List<ApkVersion> listByStatus(int status,Integer type) {
		Criteria criteria = super.getCurrentSession().createCriteria(ApkVersion.class,"apkversion");
		if(status!=-1) {
			criteria.add(Restrictions.eq("status", status));
		}
		criteria.add(Restrictions.eq("type", type));
		return criteria.list();
	}

	public void updateOtherStatus(Integer type, Integer status) {
		Session currentSession = super.getCurrentSession();
		String sql ="update cms_apk_version set status="+status+"   where type="+type ;
		SQLQuery createSQLQuery = currentSession.createSQLQuery(sql);
		createSQLQuery.executeUpdate();
	}

	@SuppressWarnings("rawtypes")
	public ApkVersion getByStatusAndType(Integer type, int status) {
		Criteria criteria = super.getCurrentSession().createCriteria(ApkVersion.class,"apkversion");
		if(status!=-1) {
			criteria.add(Restrictions.eq("status", status));
		}
		criteria.add(Restrictions.eq("type", type));
		List list = criteria.list();
		if(list!=null&&list.size()>0) {
			Object object = list.get(0);
			return (ApkVersion) object;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public ApkVersion getByType(Integer type,String mainVersion,Integer childVersion) {
		Criteria criteria = super.getCurrentSession().createCriteria(ApkVersion.class);
		criteria.add(Restrictions.eq("type", type.intValue()));
		criteria.add(Restrictions.eq("mainVersion", mainVersion));
		criteria.add(Restrictions.eq("childVersion", childVersion));
		List list = criteria.list();
		if(list!=null&&list.size()>0) {
			Object object = list.get(0);
			return (ApkVersion) object;
		}
		return null;
	}
	
}
