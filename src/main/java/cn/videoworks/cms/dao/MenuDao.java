package cn.videoworks.cms.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.entity.Menu;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

/**
 * @author   meishen
 * @Date	 2018	2018年9月18日		上午9:56:46
 * @Description 方法描述: 菜单数据库处理类
 */
@Repository
public class MenuDao  extends AdvancedHibernateDao<Menu>{

	/**
	 * list:(按照指定参数排序)
	 * @author   meishen
	 * @Date	 2018	2018年9月18日		上午9:57:09 
	 * @return List<Menu>    
	 * @throws
	 */
	public List<Menu> list(String order){
		Criteria criteria = super.getCurrentSession().createCriteria(Menu.class);
		if (StringUtils.isNotBlank(order))
			criteria.addOrder(Order.asc(order));
		@SuppressWarnings("unchecked")
		List<Menu> list = criteria.list();
		return list;
	}
	
	public List<Menu> listByPid(int pId,String order){
		Criteria criteria = super.getCurrentSession().createCriteria(Menu.class);
		if (StringUtils.isNotBlank(order))
			criteria.addOrder(Order.asc(order));
		
		criteria.add(Restrictions.eq("parent",pId));
		
		@SuppressWarnings("unchecked")
		List<Menu> list = criteria.list();
		return list;
	}
	
}
