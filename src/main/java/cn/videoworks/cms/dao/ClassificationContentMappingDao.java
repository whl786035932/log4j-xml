package cn.videoworks.cms.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.ClassificationContentMapping;
import cn.videoworks.cms.enumeration.CdnStatus;
import cn.videoworks.cms.enumeration.ContentStatus;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

@Repository
public class ClassificationContentMappingDao extends
		AdvancedHibernateDao<ClassificationContentMapping> {

	/**
	 * list:(查询内容下的分类)
	 * 
	 * @author meishen
	 * @Date 2018 2018年5月31日 下午7:55:14
	 * @param content_id
	 * @return
	 * @return List<ClassificationContentMapping>
	 * @throws
	 * @since Videoworks　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public List<ClassificationContentMapping> list(Long content_id) {
		Criteria criteria = super.getCurrentSession()
				.createCriteria(ClassificationContentMapping.class, "ccm")
				.createAlias("ccm.content", "content");
		criteria.add(Restrictions.eq("content.id", content_id));
		List<ClassificationContentMapping> list = criteria.list();
		return list;
	}

	/**
	 * list:(查询分类+内容 判断是否已经存在)
	 * 
	 * @author meishen
	 * @Date 2018 2018年6月5日 上午9:32:24
	 * @param classification_id
	 * @param content_id
	 * @return
	 * @return List<ClassificationContentMapping>
	 * @throws
	 * @since Videoworks　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public List<ClassificationContentMapping> list(String classification_id,
			Long content_id) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from cms_classification_content_mapping ");

		if (StringUtils.isNotBlank(classification_id))
			sql.append("where classification_id = :classification_id ");

		if (null != content_id)
			sql.append("and content_id = :content_id ");

		SQLQuery sqlQuery = this.getCurrentSession()
				.createSQLQuery(sql.toString())
				.addEntity(ClassificationContentMapping.class);

		if (StringUtils.isNotBlank(classification_id))
			sqlQuery.setParameter("classification_id", classification_id);

		if (null != content_id)
			sqlQuery.setParameter("content_id", content_id);

		return sqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	public List<ClassificationContentMapping> list(String classification_id,
			String title, int status, String order, Page page) {
		Criteria criteria = super.getCurrentSession()
				.createCriteria(ClassificationContentMapping.class, "ccm")
				.createAlias("ccm.content", "content")
				.createCriteria("ccm.classification", "classification");
		criteria.add(Restrictions.eq("classification.id", classification_id));

		if (-1 != status) {
			criteria.add(Restrictions.eq("content.status", status));
		}
		criteria.add(Restrictions.gt("content.status", 0));
		criteria.add(Restrictions.lt("content.status", 3));
		if (StringUtils.isNotBlank(title))
			criteria.add(Restrictions.like("content.title", "%" + title + "%"));
		criteria.addOrder(Order.desc("ccm.recommend"));
		if (StringUtils.isNotBlank(order))
			criteria.addOrder(Order.desc("content."+order));
		criteria.setFirstResult(page.getOffSet());
		criteria.setMaxResults(page.getSize());
		List<ClassificationContentMapping> list = criteria.list();
		return list;
	}

	/**
	 * count:(WEB分类编排，查询分类下的内容分页)
	 * 
	 * @author meishen
	 * @Date 2018 2018年5月31日 下午8:00:47
	 * @param title
	 * @param status
	 * @return
	 * @return long
	 * @throws
	 * @since Videoworks　Ver 1.1
	 */
	public long count(String classification_id, String title, int status) {
		Criteria criteria = super.getCurrentSession()
				.createCriteria(ClassificationContentMapping.class, "ccm")
				.createAlias("ccm.content", "content")
				.createCriteria("ccm.classification", "classification");
		if (StringUtils.isNotBlank(classification_id))
			criteria.add(Restrictions
					.eq("classification.id", classification_id));

		if (-1 != status) {
			criteria.add(Restrictions.eq("content.status", status));
		}
		criteria.add(Restrictions.gt("content.status", 0));
		criteria.add(Restrictions.lt("content.status", 3));
		if (StringUtils.isNotBlank(title))
			criteria.add(Restrictions.like("content.title", "%" + title + "%"));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().iterator().next();
	}

	public int deletec(String sql, Object... params) {
		try {
			Session session = this.getCurrentSession();
			Query queryupdate = session.createQuery(sql);
			for (int i = 0; i < params.length; i++) {
				queryupdate.setParameter(i, params[i]);
			}
			return queryupdate.executeUpdate();
		} catch (HibernateException e) {
			e.printStackTrace();
			return 0;
		}

	}
	@SuppressWarnings("unchecked")
	public List<ClassificationContentMapping> getRecommoned(String id) {
		Criteria criteria = super.getCurrentSession().createCriteria(ClassificationContentMapping.class, "ccm")
				.createAlias("ccm.content", "content")
				.createCriteria("ccm.classification", "classification");
		List<ClassificationContentMapping> list = null;
		try {
			if (StringUtils.isNotBlank(id))
				criteria.add(Restrictions
						.eq("classification.id", id));
			criteria.add(Restrictions.gt("ccm.recommend", 0));
			criteria.add(Restrictions.gt("content.status", 0));
			list = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//api
	@SuppressWarnings("unchecked")
	public List<ClassificationContentMapping> listContentByClassId(
			String classification_id,Integer limit, Integer sequence) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(ClassificationContentMapping.class,"classificationContent").createAlias("classificationContent.classification", "classification").createAlias("classificationContent.content", "content");
		createCriteria.add(Restrictions.eq("classification.id", classification_id));
		createCriteria.add(Restrictions.eq("content.cdn_sync_status", CdnStatus.SYNCHRONIZED.getValue()));
		createCriteria.add(Restrictions.eq("content.status", ContentStatus.SHELVED.getValue()));
		if(sequence!=null){
			createCriteria.add(Restrictions.lt("sequence", sequence));
		}
		createCriteria.addOrder(Order.desc("recommend"));
		createCriteria.addOrder(Order.desc("content.publishTime"));
		createCriteria.setMaxResults(limit);
		return createCriteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ClassificationContentMapping> listContentByClassIdOrderByPublishTime(String classification_id,
			Integer limit, Integer page) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(ClassificationContentMapping.class,"classificationContent").createAlias("classificationContent.classification", "classification").createAlias("classificationContent.content", "content");
		createCriteria.add(Restrictions.eq("classification.id", classification_id));
		createCriteria.add(Restrictions.eq("content.cdn_sync_status", CdnStatus.SYNCHRONIZED.getValue()));
		createCriteria.add(Restrictions.eq("content.status", ContentStatus.SHELVED.getValue()));
		
		createCriteria.addOrder(Order.desc("recommend"));
		createCriteria.addOrder(Order.desc("content.publishTime"));
		createCriteria.setFirstResult((page-1)*limit);
		createCriteria.setMaxResults(limit);
		return createCriteria.list();
	}
	
	public long count(String classification_id) {
		Criteria criteria = super.getCurrentSession().createCriteria(ClassificationContentMapping.class,"classificationContent").createAlias("classificationContent.classification", "classification").createAlias("classificationContent.content", "content");
		criteria.add(Restrictions.eq("classification.id", classification_id));
		criteria.add(Restrictions.eq("content.cdn_sync_status", CdnStatus.SYNCHRONIZED.getValue()));
		criteria.add(Restrictions.eq("content.status", ContentStatus.SHELVED.getValue()));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().iterator().next();
	}
	
	/**
	 * getContents:(随机获取N条分类下内容--贵州推荐演示)
	 * @author   meishen
	 * @Date	 2018	2018年11月12日		下午2:41:41 
	 * @return List<ClassificationContentMapping>    
	 * @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ClassificationContentMapping> getContents(String classification_id,Integer limit) {
 		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM cms_classification_content_mapping cc LEFT JOIN cms_content c on cc.content_id = c.id where c.publish_time >=DATE_SUB(CURDATE(), INTERVAL 7 DAY)  ");
		sql.append(" and  cc.classification_id = '"+classification_id+"'");
		sql.append(" and  cc.recommend <= 0 ");
		sql.append(" ORDER BY  RAND() LIMIT "+limit);
		Query querySql = this.getCurrentSession().createSQLQuery(sql.toString()).addEntity(ClassificationContentMapping.class);
		List list = querySql.list();
		return list;
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<String> getClassifications(Long contentId) {
 		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.name as name FROM cms_classification_content_mapping cc LEFT JOIN cms_classification c on cc.classification_id = c.id");
		sql.append(" where 1=1 and  cc.content_id = '"+contentId+"'");
		Query querySql = this.getCurrentSession().createSQLQuery(sql.toString());
		List<String> list = querySql.list();
		return list;
	}
}
