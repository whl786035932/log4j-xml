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
import cn.videoworks.cms.dto.TaskDto;
import cn.videoworks.cms.entity.Task;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

@Repository
public class TaskDao extends AdvancedHibernateDao<Task> {

	public Map<String, Object> list(TaskDto dto) {
//		Map<String, Object> reparam = null;
//		Criteria criteria = this.getCurrentSession().createCriteria(Task.class);
//		// 正题名
//		if (dto.getTitle() != null) {
//			criteria.add(Restrictions.like("content.title",
//					"%" + dto.getTitle() + "%"));
//		}
//		// 入站状态
//		if (dto.getStatus() != null) {
//			criteria.add(Restrictions.eq("status", dto.getStatus()));
//		}
//		criteria.addOrder(Order.desc("insertedAt"));
//		
//		
//		// 总量
//		long count = criteria.list().size();
//		criteria.setFirstResult(dto.getPage().getIndex()
//				* dto.getPage().getSize());
//		criteria.setMaxResults(dto.getPage().getSize());
//		@SuppressWarnings("unchecked")
//		List<Task> list = criteria.list();
//		// 设置返回参数
//		if (reparam == null) {
//			reparam = new HashMap<String, Object>();
//		}
//		reparam.put("list", list);
//		reparam.put("count", count);
//		return reparam;
		return null;
	}

	public List<Task> list(Map<String, Object> q, String order, Page page) {
		Criteria criteria = super.getCurrentSession().createCriteria(Task.class,"task").createAlias("task.content", "content");
		String title = q.containsKey("title")==true?q.get("title").toString():"";
		String status = q.containsKey("status")==true?q.get("status").toString():"";
		String publishTimeBegin = q.containsKey("publishTimeBegin")==true?q.get("publishTimeBegin").toString():"";
		String publishTimeEnd = q.containsKey("publishTimeEnd")==true?q.get("publishTimeEnd").toString():"";
		String insertedBegin = q.containsKey("insertedBegin")==true?q.get("insertedBegin").toString():"";
		String insertedEnd = q.containsKey("insertedEnd")==true?q.get("insertedEnd").toString():"";
		
		if(StringUtils.isNotBlank(title)) criteria.add(Restrictions.like("content.title", "%"+title+"%"));
		if(StringUtils.isNotBlank(status)) criteria.add(Restrictions.eq("status",Integer.parseInt(status)));
		
		if(StringUtils.isNotBlank(publishTimeBegin)) 
			criteria.add(Restrictions.ge("content.publishTime", DateUtil.getDate(publishTimeBegin)));
		if(StringUtils.isNotBlank(publishTimeEnd))
			criteria.add(Restrictions.le("content.publishTime", DateUtil.getDate(publishTimeEnd)));
		if(StringUtils.isNotBlank(insertedBegin) ) 
			criteria.add(Restrictions.ge("inserted_at", DateUtil.getDate(insertedBegin)));
		if(StringUtils.isNotBlank(insertedEnd)){
			criteria.add(Restrictions.le("inserted_at", DateUtil.getDate(insertedEnd)));
		}
		if(StringUtils.isNotBlank(order))
			criteria.addOrder(Order.desc("content.insertedAt"));
		criteria.setFirstResult(page.getOffSet());
		criteria.setMaxResults(page.getSize());
		@SuppressWarnings("unchecked")
		List<Task> list = criteria.list();
		return list;
	}
	/**
	 * WEB页面检索分页
	 * (non-Javadoc)
	 * @see cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao#count()
	 */
	public long count(Map<String, Object> q) {
		Criteria criteria = super.getCurrentSession().createCriteria(Task.class,"task").createAlias("task.content", "content");
		String title = q.containsKey("title")==true?q.get("title").toString():"";
		String status = q.containsKey("status")==true?q.get("status").toString():"";
		String publishTimeBegin = q.containsKey("publishTimeBegin")==true?q.get("publishTimeBegin").toString():"";
		String publishTimeEnd = q.containsKey("publishTimeEnd")==true?q.get("publishTimeEnd").toString():"";
		String insertedBegin = q.containsKey("insertedBegin")==true?q.get("insertedBegin").toString():"";
		String insertedEnd = q.containsKey("insertedEnd")==true?q.get("insertedEnd").toString():"";
		
		if(StringUtils.isNotBlank(title)) criteria.add(Restrictions.like("content.title", "%"+title+"%"));
		if(StringUtils.isNotBlank(status)) criteria.add(Restrictions.eq("status",Integer.parseInt(status)));
		
		if(StringUtils.isNotBlank(publishTimeBegin)) 
			criteria.add(Restrictions.ge("content.publishTime", DateUtil.getDate(publishTimeBegin)));
		if(StringUtils.isNotBlank(publishTimeEnd))
			criteria.add(Restrictions.le("content.publishTime", DateUtil.getDate(publishTimeEnd)));
		if(StringUtils.isNotBlank(insertedBegin) ) 
			criteria.add(Restrictions.ge("inserted_at", DateUtil.getDate(insertedBegin)));
		if(StringUtils.isNotBlank(insertedEnd)){
			criteria.add(Restrictions.le("inserted_at", DateUtil.getDate(insertedEnd)));
		}
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().iterator().next();
	}

	@SuppressWarnings("rawtypes")
	public Task getByMsgId(String msgId) {
		Criteria criteria = super.getCurrentSession().createCriteria(Task.class,"task");
		criteria.add(Restrictions.eq("task.msgid", msgId));
		List list = criteria.list();
		if(list!=null&&list.size()>0) {
			return (Task) list.get(0);
		}
		return null;
	}
	
	/**
	 * list:(任务统计使用)
	 * @author   meishen
	 * @Date	 2018	2018年10月26日		下午4:08:51 
	 * @return List<Task>    
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List<Task> list(String insertedAt) {
		Criteria criteria = super.getCurrentSession().createCriteria(Task.class);
		if(StringUtils.isBlank(insertedAt)) 
			return null;
	
		criteria.add(Restrictions.ge("inserted_at",DateUtil.getTimeStamp(insertedAt+" 00:00:00")));
	    criteria.add(Restrictions.le("inserted_at",DateUtil.getTimeStamp(insertedAt+" 23:59:59")));
	
		List<Task> list = criteria.list();
		return list;
	}
}
