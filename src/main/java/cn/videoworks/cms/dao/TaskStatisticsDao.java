package cn.videoworks.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.TaskStatistics;
import cn.videoworks.cms.enumeration.Sort;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

@Repository
public class TaskStatisticsDao  extends AdvancedHibernateDao<TaskStatistics>{
	
	/**
	 * list:(按照某个字段进行排序；分页)
	 * @author   meishen
	 * @Date	 2018	2018年10月26日		上午11:06:48 
	 * @return List<TaskStatistics>    
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List<TaskStatistics> list(Map<String,Object> q,String order,String sort,Page page) {
		/**
		 * WEB页面内容检索 
		 */
		String insertedBegin = q.containsKey("insertedBegin")==true?q.get("insertedBegin").toString():"";
		String insertedEnd = q.containsKey("insertedEnd")==true?q.get("insertedEnd").toString():"";
		
		Criteria criteria = super.getCurrentSession().createCriteria(TaskStatistics.class);
		
        if(StringUtils.isNotBlank(insertedBegin))  criteria.add(Restrictions.ge("insertedAt",DateUtil.getTimeStamp(insertedBegin)));
        if(StringUtils.isNotBlank(insertedEnd)) criteria.add(Restrictions.le("insertedAt",DateUtil.getTimeStamp(insertedEnd)));
        
        if(StringUtils.isNotBlank(order)){
			if(StringUtils.isNotBlank(sort) && sort.equals(Sort.ASC.getValue()))
				criteria.addOrder(Order.asc(order));
			else
				criteria.addOrder(Order.desc(order));
		}
        
        if(null != page) {
			criteria.setFirstResult(page.getOffSet());
			criteria.setMaxResults(page.getSize());
        }
		List<TaskStatistics> list = criteria.list();
		return list;
	}
	
	/**
	 * WEB页面内容检索 
	 */
	public long count(Map<String,Object> q) {
		String insertedBegin = q.containsKey("insertedBegin")==true?q.get("insertedBegin").toString():"";
		String insertedEnd = q.containsKey("insertedEnd")==true?q.get("insertedEnd").toString():"";
		
		Criteria criteria = super.getCurrentSession().createCriteria(TaskStatistics.class);
        
        if(StringUtils.isNotBlank(insertedBegin))  criteria.add(Restrictions.ge("insertedAt",DateUtil.getTimeStamp(insertedBegin)));
        if(StringUtils.isNotBlank(insertedEnd)) criteria.add(Restrictions.le("insertedAt",DateUtil.getTimeStamp(insertedEnd)));
        
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().iterator().next();
	}
	
	/**
	 * get:(获取某个时间的单条任务统计)
	 * @author   meishen
	 * @Date	 2018	2018年10月26日		上午11:10:36 
	 * @return TaskStatistics    
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public TaskStatistics get(String statisticsAt) {
		Criteria criteria = super.getCurrentSession().createCriteria(TaskStatistics.class);
		if(StringUtils.isBlank(statisticsAt)) 
			return null;
		
		criteria.add(Restrictions.eq("statisticsAt",statisticsAt));
		List<TaskStatistics> list = criteria.list();
		return list.size() >0?list.get(0):null;
	}
	
}
