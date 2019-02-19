/**
 * ContentDao.java
 * cn.videoworks.cms.dao
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月28日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Content;
import cn.videoworks.cms.enumeration.CdnStatus;
import cn.videoworks.cms.enumeration.ContentStatus;
import cn.videoworks.cms.enumeration.Sort;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

/**
 * ClassName:ContentDao
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月28日		下午8:22:26
 *
 * @see 	 
 */
@Repository
public class ContentDao extends AdvancedHibernateDao<Content> {

	/**
	 * get:(内容判重,不包含删除状态)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月29日		下午4:56:19
	 * @param assetId
	 * @return   
	 * @return Content    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public Content get(String title,String publishTime,String cp,String sourceChannel,String sourceColumn) {
		Criteria criteria = super.getCurrentSession().createCriteria(Content.class);
		if(StringUtils.isNotBlank(title))
			criteria.add(Restrictions.eq("title",title));
		
		 if(StringUtils.isNotBlank(publishTime)) 
			 criteria.add(Restrictions.eq("publishTime",DateUtil.getTimeStamp(DateUtil.dateTime(Long.valueOf(publishTime)))));
		 
		 if(StringUtils.isNotBlank(cp))
				criteria.add(Restrictions.eq("cp",cp));
		 
		 if(StringUtils.isNotBlank(sourceChannel)) 
			 criteria.add(Restrictions.eq("sourceChannel",sourceChannel));
		 
		 if(StringUtils.isNotBlank(sourceColumn)) 
			 criteria.add(Restrictions.eq("sourceColumn",sourceColumn));
		
		 criteria.add(Restrictions.ne("status", ContentStatus.DELETE.getValue()));//排除删除状态的
		 
		@SuppressWarnings("unchecked")
		List<Content> list = criteria.list();
		if(list.size()>0)
			return list.get(0);
		return null;
	}
	
	/**
	 * get:(自动上架判断)
	 * @author   meishen
	 * @Date	 2018	2018年11月19日		下午4:04:24 
	 * @return Content    
	 * @throws
	 */
	public Content get(String title,int status) {
		Criteria criteria = super.getCurrentSession().createCriteria(Content.class);
		criteria.add(Restrictions.eq("title",title));
		criteria.add(Restrictions.ne("status", ContentStatus.DELETE.getValue()));//排除删除状态的
		criteria.add(Restrictions.eq("status",status));
		@SuppressWarnings("unchecked")
		List<Content> list = criteria.list();
		if(list.size()>0)
			return list.get(0);
		return null;
	}
	
	/**
	 * list:(web页面检索内容)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年5月30日		下午7:16:34
	 * @param q
	 * @param order
	 * @param page
	 * @return   
	 * @return List<Content>    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public List<Content> list(Map<String,Object> q,String order,String sort,Page page) {
		/**
		 * WEB页面内容检索 
		 */
		String title = q.containsKey("title")==true?q.get("title").toString():"";
		String status = q.containsKey("status")==true?q.get("status").toString():"";
		String sourceChannel = q.containsKey("sourceChannel")==true?q.get("sourceChannel").toString():"";
		String sourceColumn = q.containsKey("sourceColumn")==true?q.get("sourceColumn").toString():"";
		String publishTimeBegin = q.containsKey("publishTimeBegin")==true?q.get("publishTimeBegin").toString():"";
		String publishTimeEnd = q.containsKey("publishTimeEnd")==true?q.get("publishTimeEnd").toString():"";
		String insertedBegin = q.containsKey("insertedBegin")==true?q.get("insertedBegin").toString():"";
		String insertedEnd = q.containsKey("insertedEnd")==true?q.get("insertedEnd").toString():"";
		
		Criteria criteria = super.getCurrentSession().createCriteria(Content.class);
		
		if(StringUtils.isNotBlank(title)) criteria.add(Restrictions.like("title", "%"+title+"%"));
        
        if(StringUtils.isNotBlank(status)) criteria.add(Restrictions.eq("status", Integer.valueOf(status)));
        
        if(StringUtils.isNotBlank(sourceChannel)) criteria.add(Restrictions.eq("sourceChannel", sourceChannel));
        if(StringUtils.isNotBlank(sourceColumn)) criteria.add(Restrictions.eq("sourceColumn", sourceColumn));
        
        if(StringUtils.isNotBlank(publishTimeBegin)) criteria.add(Restrictions.ge("publishTime",DateUtil.getTimeStamp(publishTimeBegin)));
        if(StringUtils.isNotBlank(publishTimeEnd)) criteria.add(Restrictions.le("publishTime",DateUtil.getTimeStamp(publishTimeEnd)));
        
        if(StringUtils.isNotBlank(insertedBegin))  criteria.add(Restrictions.ge("insertedAt",DateUtil.getTimeStamp(insertedBegin)));
        if(StringUtils.isNotBlank(insertedEnd)) criteria.add(Restrictions.le("insertedAt",DateUtil.getTimeStamp(insertedEnd)));
        
        if(StringUtils.isNotBlank(order)){
			if(StringUtils.isNotBlank(sort) && sort.equals(Sort.ASC.getValue()))
				criteria.addOrder(Order.asc(order));
			else
				criteria.addOrder(Order.desc(order));
		}
        
//        criteria.add(Restrictions.ne("status", ContentStatus.DELETE.getValue()));
//        criteria.add(Restrictions.ne("status", ContentStatus.ILLEGAL.getValue()));
        
        if(null != page) {
			criteria.setFirstResult(page.getOffSet());
			criteria.setMaxResults(page.getSize());
        }
		List<Content> list = criteria.list();
		return list;
	}
	
	/**
	 * WEB页面检索分页
	 * (non-Javadoc)
	 * @see cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao#count()
	 */
	public long count(Map<String,Object> q) {
		String title = q.containsKey("title")==true?q.get("title").toString():"";
		String status = q.containsKey("status")==true?q.get("status").toString():"";
		String sourceChannel = q.containsKey("sourceChannel")==true?q.get("sourceChannel").toString():"";
		String sourceColumn = q.containsKey("sourceColumn")==true?q.get("sourceColumn").toString():"";
		String publishTimeBegin = q.containsKey("publishTimeBegin")==true?q.get("publishTimeBegin").toString():"";
		String publishTimeEnd = q.containsKey("publishTimeEnd")==true?q.get("publishTimeEnd").toString():"";
		String insertedBegin = q.containsKey("insertedBegin")==true?q.get("insertedBegin").toString():"";
		String insertedEnd = q.containsKey("insertedEnd")==true?q.get("insertedEnd").toString():"";
		
		Criteria criteria = super.getCurrentSession().createCriteria(Content.class);
		
		if(StringUtils.isNotBlank(title)) criteria.add(Restrictions.like("title", "%"+title+"%"));
        
        if(StringUtils.isNotBlank(status)) criteria.add(Restrictions.eq("status", Integer.valueOf(status)));
        
        if(StringUtils.isNotBlank(sourceChannel)) criteria.add(Restrictions.eq("sourceChannel", sourceChannel));
        if(StringUtils.isNotBlank(sourceColumn)) criteria.add(Restrictions.eq("sourceColumn", sourceColumn));
        
        if(StringUtils.isNotBlank(publishTimeBegin)) criteria.add(Restrictions.ge("publishTime",DateUtil.getTimeStamp(publishTimeBegin)));
        if(StringUtils.isNotBlank(publishTimeEnd)) criteria.add(Restrictions.le("publishTime",DateUtil.getTimeStamp(publishTimeEnd)));
        
        if(StringUtils.isNotBlank(insertedBegin))  criteria.add(Restrictions.ge("insertedAt",DateUtil.getTimeStamp(insertedBegin)));
        if(StringUtils.isNotBlank(insertedEnd)) criteria.add(Restrictions.le("insertedAt",DateUtil.getTimeStamp(insertedEnd)));
        
//		criteria.add(Restrictions.ne("status", ContentStatus.DELETE.getValue()));
//        criteria.add(Restrictions.ne("status", ContentStatus.ILLEGAL.getValue()));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	public List<Content> list(String beginTime) {
		String sql = "select * from cms_content where inserted_at > '"+beginTime+"'";
		return this.getCurrentSession().createSQLQuery(sql).addEntity(Content.class).list();
	}
	
	/**
	 * list:(查询内容小于publishTime时间且是删除状态的内容)
	 * @author   meishen
	 * @Date	 2018	2018年9月10日		下午2:36:49 
	 * @return List<Content>    
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List<Content> list(String publishTime,Page page) {
		Criteria criteria = super.getCurrentSession().createCriteria(Content.class);
		
		if(StringUtils.isNotBlank(publishTime))
			criteria.add(Restrictions.le("publishTime", DateUtil.getTimeStamp(publishTime)));
		
		criteria.add(Restrictions.ne("status", ContentStatus.DELETE.getValue()));
		criteria.add(Restrictions.ne("status", ContentStatus.DELETESTORAGE.getValue()));
		
		if(null != page) {
			criteria.setFirstResult(page.getOffSet());
			criteria.setMaxResults(page.getSize());
        }
		
		List<Content> list = criteria.list();
		return list;
	}
	
	
	//api
	@SuppressWarnings("unchecked")
	public List<Content> findByTitleAbbr(String keyword,Integer limit, String publish_time) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(Content.class,"content");
		if(StringUtils.isNotBlank(keyword)){
			createCriteria.add(Restrictions.ilike("content.titleAbbr", "%"+keyword+"%"));//不区分大小写
		}
		if(StringUtils.isNotBlank(publish_time)){
			Date timeStamp = null;
			if(StringUtils.isNotBlank(publish_time)){
				if(publish_time.contains(":")) {
					
					 timeStamp = DateUtil.getDate(publish_time);
				}else {
					 timeStamp = DateUtil.getDateYMD(publish_time);
					
				}
			}
 			createCriteria.add(Restrictions.lt("publishTime", timeStamp));
		}
		createCriteria.add(Restrictions.eq("cdn_sync_status", CdnStatus.SYNCHRONIZED.getValue()));
		createCriteria.add(Restrictions.eq("status", ContentStatus.SHELVED.getValue()));
		createCriteria.setMaxResults(limit);
		createCriteria.addOrder(Order.desc("publishTime"));
		return createCriteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Content> listByShelveStatus(int shelveStatus, int cdnStatus) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(Content.class,"content");
		createCriteria.add(Restrictions.eq("cdn_sync_status",cdnStatus));
		createCriteria.add(Restrictions.eq("status", shelveStatus));
		return createCriteria.list();
	}

	public Integer count(String title_abbr) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(Content.class,"content");
		if(StringUtils.isNotBlank(title_abbr)){
			createCriteria.add(Restrictions.ilike("content.titleAbbr", "%"+title_abbr+"%"));//不区分大小写
		}
	
		createCriteria.add(Restrictions.eq("cdn_sync_status", CdnStatus.SYNCHRONIZED.getValue()));
		createCriteria.add(Restrictions.eq("status", ContentStatus.SHELVED.getValue()));
		return createCriteria.list().size();
	}

	public List<Content> findByChannleColumnPublishTime(String channel, String column, Integer limit,
			String publish_time) {
		Criteria createCriteria = super.getCurrentSession().createCriteria(Content.class,"content");
		if(StringUtils.isNotBlank(channel)){
			createCriteria.add(Restrictions.eq("content.sourceChannel", channel));//不区分大小写
		}
		if(StringUtils.isNotBlank(column)){
			createCriteria.add(Restrictions.eq("content.sourceColumn", column));//不区分大小写
		}
		
		if(StringUtils.isNotBlank(publish_time)){
			Date timeStamp = null;
			if(StringUtils.isNotBlank(publish_time)){
				if(publish_time.contains(":")) {
					
					 timeStamp = DateUtil.getDate(publish_time);
				}else {
					 timeStamp = DateUtil.getDateYMD(publish_time);
					
				}
			}
 			createCriteria.add(Restrictions.lt("publishTime", timeStamp));
		}
		createCriteria.add(Restrictions.eq("cdn_sync_status", CdnStatus.SYNCHRONIZED.getValue()));
		createCriteria.add(Restrictions.eq("status", ContentStatus.SHELVED.getValue()));
		createCriteria.setMaxResults(limit);
		createCriteria.addOrder(Order.desc("publishTime"));
		return createCriteria.list();
		
		
	}
}

