package cn.videoworks.cms.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.videoworks.cms.dto.ApiClassificationDto;
import cn.videoworks.cms.dto.ClassificationDto;
import cn.videoworks.cms.entity.Classification;
import cn.videoworks.cms.enumeration.ClassificationRecommend;
import cn.videoworks.cms.enumeration.ClassificationType;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

@Repository
public class ClassificationDao extends AdvancedHibernateDao<Classification> {

	public Map<String, Object> getDetail(String id) {
		Map<String, Object> result = null;
		try {
			// 拼接sql
			String sqls = "";
			StringBuffer sql = new StringBuffer();
			if (id == null || id.equals("")) {
				sql.append("SELECT cc.name as name,cc.status as status,cc.id as id,cc.icon as icon,cc.type as type,cc.alias as alias,cc.editable as editable,cc.deletable as deletable,cc.recommend as recommend FROM cms_classification cc WHERE cc.parent ='0'  ");
			} else {
				sql.append("SELECT cc.name as name,cc.status as status,cc.id as id,cc.icon as icon,cc.type as type,cc.alias as alias,cc.editable as editable,cc.deletable as deletable,cc.recommend as recommend FROM cms_classification cc WHERE cc.id =  ");
				sql.append("'" + id + "'");
			}
			// 拼接条件
			sqls = sql.toString();
			Query querySql = this.getCurrentSession().createSQLQuery(sqls);
			@SuppressWarnings("unchecked")
			List<Object[]> list = querySql.list();
			if (list != null && list.size() > 0) {
				result = new HashMap<>();
				result.put("name", list.get(0)[0]);
				result.put("status", list.get(0)[1]);
				result.put("id", list.get(0)[2]);
				result.put("icon", list.get(0)[3]);
				result.put("type", list.get(0)[4]);
				result.put("alias", list.get(0)[5]);
				result.put("editable", list.get(0)[6]);
				result.put("deletable", list.get(0)[7]);
				result.put("recommend", list.get(0)[8]);
				// result.put("ruleId", list.get(0)[3]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Classification> list() {
		List<Classification> list = null;
		try {
			// 拼接sql
			String sqls = "";
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM cms_classification order by  lft ");
			sqls = sql.toString();
			Query querySql = this.getCurrentSession().createSQLQuery(sqls)
					.addEntity(Classification.class);
			list = querySql.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<ClassificationDto> getClassification() {
		List<ClassificationDto> result = null;
		try {
			// 拼接sql
			String sqls = "";
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT cc.id as id,cc.name as name,cc.status as status,cc.alias as alias,cc.editable as editable,cc.deletable as deletable,cc.parent as pId FROM cms_classification cc order by  cc.level ASC,cc.sequence ASC");
			sqls = sql.toString();
			Query querySql = this.getCurrentSession().createSQLQuery(sqls);
			List<Object[]> list = querySql.list();
			if (list != null) {
				for (Object[] ob : list) {
					ClassificationDto dto = new ClassificationDto();
					dto.setId(ob[0].toString());
					dto.setName(ob[1].toString());
					dto.setStatus(Integer.valueOf(ob[2].toString()));
					if (ob[3] != null) {
						dto.setAlias(ob[3].toString());
					}
					if (ob[4] != null) {
						dto.setEditable(Integer.valueOf(ob[4].toString()));
					}
					if (ob[5] != null) {
						dto.setDeletable(Integer.valueOf(ob[5].toString()));
					}
					if (ob[6] != null) {
						dto.setpId(ob[6].toString());
					}
					dto.setChildren(new ArrayList<ClassificationDto>());
					if (result == null) {
						result = new ArrayList<ClassificationDto>();
					}
					result.add(dto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int update(String sql, Object... params) {
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

	public int deleteClassification(String sql, Object... params) {
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

	public List<Classification> getClassificationF(Long lft, Long rgt) {
		try {
			Criteria criteria = super.getCurrentSession().createCriteria(
					Classification.class);
			criteria.add(Restrictions.le("lft", lft));
			criteria.add(Restrictions.ge("rgt", rgt));
			criteria.addOrder(Order.asc("lft"));
			@SuppressWarnings("unchecked")
			List<Classification> list = criteria.list();
			if (list.size() > 0)
				return list;
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return new ArrayList<Classification>();
	}

	public List<Classification> getClassificationC(Long lft, Long rgt) {
		try {
			Criteria criteria = super.getCurrentSession().createCriteria(
					Classification.class);
			criteria.add(Restrictions.ge("lft", lft));
			criteria.add(Restrictions.le("rgt", rgt));
			criteria.addOrder(Order.asc("lft"));
			@SuppressWarnings("unchecked")
			List<Classification> list = criteria.list();
			if (list.size() > 0)
				return list;
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return new ArrayList<Classification>();
	}

	@SuppressWarnings("unchecked")
	public List<Classification> getByName(String name) {
		Criteria criteria = super.getCurrentSession().createCriteria(
				Classification.class);
		if (StringUtils.isNotBlank(name))
			criteria.add(Restrictions.eq("name", name.trim()));
		return criteria.list();
	}
	@SuppressWarnings("unchecked")
	public List<Classification> getByName(ClassificationDto dto) {
		Criteria criteria = super.getCurrentSession().createCriteria(Classification.class);
		List<Classification> list = null;
		try {
			if (StringUtils.isNotBlank(dto.getName())) {
				criteria.add(Restrictions.eq("alias", dto.getAlias()));
			}
			if (StringUtils.isNotBlank(dto.getId())) {
				criteria.add(Restrictions.ne("id", dto.getId()));
			}
			list = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Classification> get(ClassificationDto dto) {
		Criteria criteria = super.getCurrentSession().createCriteria(Classification.class);
		List<Classification> list = null;
		try {
			if (dto.getId() != null) {
				criteria.add(Restrictions.or(Restrictions.eq("id", dto.getId()), Restrictions.eq("parent", dto.getId())));
			}
			list = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	
	
	
	
	
	//api
	//获取分类的子分类
		public List<Classification> listChild(Long parent_left,Long parent_right,Integer status) {
//			Criteria createCriteria = super.getCurrentSession().createCriteria(Classification.class);
//		    createCriteria.add(Restrictions.eq("status", status));
//		    createCriteria.add(Restrictions.gt("lft", parent_left));
//		    createCriteria.add(Restrictions.lt("rgt", parent_right));
//		    createCriteria.addOrder(Order.asc("lft"));
//			return createCriteria.list();
			
			List<Classification> list = null;
			try {
				// 拼接sql
				String sqls = "";
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT * FROM cms_classification where status="+status+"  and lft>"+parent_left+" and rgt<"+parent_right +" order by  lft asc ");
				sqls = sql.toString();
				System.out.println(sqls);
				Query querySql = this.getCurrentSession().createSQLQuery(sqls)
						.addEntity(Classification.class);
				list = querySql.list();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		//获取全部有效分类
		public List<Classification> listClassifications(Integer status) {
//			Criteria createCriteria = super.getCurrentSession().createCriteria(Classification.class);
//			if(status!=null){
//				createCriteria.add(Restrictions.eq("status", status));
//			}
//			createCriteria.add(Restrictions.gt("lft", 1));
//		    createCriteria.addOrder(Order.asc("lft"));
//			List list = createCriteria.list();
//			return list;
			
			
			List<Classification> list = null;
			try {
				// 拼接sql
				String sqls = "";
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT * FROM cms_classification where status="+status+"    order by  level asc ");
				sqls = sql.toString();
//				System.out.println(sqls);
				Query querySql = this.getCurrentSession().createSQLQuery(sqls)
						.addEntity(Classification.class);
				list = querySql.list();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
			
		}
		public void updateAllStatus() {
			SQLQuery createQuery = super.getCurrentSession().createSQLQuery(" update booths_classification set status=0");
			createQuery.executeUpdate();
			
		}
		public List<ApiClassificationDto> getClassificationApi() {
			List<ApiClassificationDto> result = null;
			try {
				// 拼接sql
				String sqls = "";
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT cc.id as id,cc.name as name,cc.status as status,cc.lft as lft,cc.rgt as rgt FROM booths_classification cc order by  lft ");
				sqls = sql.toString();
				Query querySql = this.getCurrentSession().createSQLQuery(sqls);
				List<Object[]> list = querySql.list();
				if (list != null) {
					for (Object[] ob : list) {
						ApiClassificationDto dto = new ApiClassificationDto();
						dto.setId(ob[0].toString());
						String name=ob[1]==null?"":ob[1].toString();
						dto.setName(name);
						if(ob[2]!=null) {
							
							dto.setStatus(Integer.valueOf(ob[2].toString()));
						}
						if(ob[3]!=null) {
							
							dto.setLft(Long.valueOf(ob[3].toString()));
						}
						if(ob[4]!=null) {
							
							dto.setRgt(Long.valueOf(ob[4].toString()));
						}
						dto.setChildren(new ArrayList<ApiClassificationDto>());
						if (result == null) {
							result = new ArrayList<ApiClassificationDto>();
						}
						result.add(dto);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		public Classification getByNameApi(String classification_name) {
			Criteria createCriteria = super.getCurrentSession().createCriteria(Classification.class);
		    createCriteria.add(Restrictions.eq("name", classification_name));
		    List list = createCriteria.list();
		    if(list!=null && list.size()>0) {
		    	return (Classification) list.get(0);
		    }
		    return null;
		}
		public List<Classification> listStorage(Integer status) {
			return list();
		}

		public List<Classification> getByPid(String pId) {
			try {
				Criteria criteria = super.getCurrentSession().createCriteria(Classification.class);
				criteria.add(Restrictions.eq("parent", pId));
				return criteria.list();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
			return null;
		}

		public List<Classification> selectElevel(String parent, Integer sequence) {
			try {
				Criteria criteria = super.getCurrentSession().createCriteria(Classification.class);
				criteria.add(Restrictions.eq("parent", parent));
				criteria.add(Restrictions.gt("sequence", sequence));
				criteria.addOrder(Order.desc("sequence"));
				return criteria.list();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
			return null;
		}

		public List<Classification> getTopChildren(String parentId) {
			try {
				Criteria criteria = super.getCurrentSession().createCriteria(Classification.class);
				criteria.add(Restrictions.eq("parent", parentId));
				criteria.addOrder(Order.desc("sequence"));
				return criteria.list();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
			return null;
		}

		public List<Classification> getTopChildren(String parentId,Integer status) {
			
			List<Classification> result = new ArrayList<>();
			try {
				// 拼接sql
				String sqls = "";
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT cc.id as id,cc.name as name,cc.icon , cc.type ,cc.level ,cc.sequence  ,cc.parent as pId, cc.alias as alias, cc.status as status, cc.editable as editable,cc.deletable as deletable,cc.recommend as recommend FROM cms_classification cc  where cc.status="+status+"  and cc.parent='"+parentId+"' order by  cc.level ASC,cc.sequence ASC");
				sqls = sql.toString();
//				System.out.println(sqls);
				Query querySql = this.getCurrentSession().createSQLQuery(sqls);
				List<Object[]> list = querySql.list();
				if (list != null) {
					for (Object[] ob : list) {
						Classification dto = new Classification();
						dto.setId(ob[0].toString());
						dto.setName(ob[1].toString());
						dto.setIcon(String.valueOf(ob[2]));
						Integer type = Integer.valueOf( ob[3].toString());
						if(type==ClassificationType.COMMON.getValue()) {
							dto.setType(ClassificationType.COMMON);
						}else if(type==ClassificationType.MULTILTVEL.getValue()) {
							dto.setType(ClassificationType.MULTILTVEL);
						}else  if(type==ClassificationType.SPECIAL.getValue()) {
							dto.setType(ClassificationType.SPECIAL);
						}
						
						if (ob[4] != null) {
							dto.setLevel(Integer.valueOf(ob[4].toString()));
						}
						dto.setSequence(Integer.valueOf(String.valueOf(ob[5])));
						dto.setParent(String.valueOf(ob[6]));
						dto.setAlias(String.valueOf(ob[7]));
						
						if(null != ob[11]) {
							int recommend = Integer.valueOf(String.valueOf(ob[11]));
							if(recommend == ClassificationRecommend.RECOMMEND.getValue())
								dto.setRecommend(ClassificationRecommend.RECOMMEND);
							else
								dto.setRecommend(ClassificationRecommend.NOTRECOMMEND);
						}else
							dto.setRecommend(ClassificationRecommend.NOTRECOMMEND);
						
						result.add(dto);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
}
