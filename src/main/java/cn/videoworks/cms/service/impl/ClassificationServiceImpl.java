package cn.videoworks.cms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.ClassificationDao;
import cn.videoworks.cms.dto.ClassificationDto;
import cn.videoworks.cms.entity.Classification;
import cn.videoworks.cms.enumeration.ClassificationDele;
import cn.videoworks.cms.enumeration.ClassificationEdit;
import cn.videoworks.cms.enumeration.ClassificationRecommend;
import cn.videoworks.cms.enumeration.ClassificationStatus;
import cn.videoworks.cms.enumeration.ClassificationType;
import cn.videoworks.cms.service.ClassificationService;
import cn.videoworks.cms.util.DateUtil;

@Service
public class ClassificationServiceImpl implements ClassificationService {
	@Resource
	private ClassificationDao dao;

	// private ClassificationRuleDao ruleDao;

	@Override
	public Map<String, Object> getDetail(String id) {
		Map<String, Object> result = dao.getDetail(id);
		return result;
	}

	@Override
	public int saveClassification(ClassificationDto dto) {
		int i = 0;
		try {
			// 保存节点信息
			Classification cf = dao.get(dto.getId());
			cf.setId(dto.getId());// 设置id
			cf.setName(dto.getName());// 设置名称
			cf.setAlias(dto.getAlias());// 别名
			if (dto.getStatus() == 0) {
				cf.setStatus(ClassificationStatus.FORBIDDEN);
			}
			if (dto.getStatus() == 1) {
				cf.setStatus(ClassificationStatus.STARTUSE);
			}
			if (dto.getType() == 0) {
				cf.setType(ClassificationType.COMMON);
			}
			if (dto.getType() == 1) {
				cf.setType(ClassificationType.SPECIAL);
			}
			if (dto.getType() == 2) {
				cf.setType(ClassificationType.MULTILTVEL);
			}
			if (dto.getEditable() == 1) {
				cf.setEditable(ClassificationEdit.STARTUSE);
			} else {
				cf.setEditable(ClassificationEdit.FORBIDDEN);
			}// 是否可编辑
			if (dto.getDeletable() == 1) {
				cf.setDeletable(ClassificationDele.STARTUSE);
			} else {
				cf.setDeletable(ClassificationDele.FORBIDDEN);
			}// 是否可删除
			if (dto.getRecommend() == 1) {
				cf.setRecommend(ClassificationRecommend.RECOMMEND);
			} else {
				cf.setRecommend(ClassificationRecommend.NOTRECOMMEND);
			}// 是否推荐
			cf.setUpdated_at(new Date());// 更新时间
			cf.setIcon(dto.getIcon());// 图标
			dao.saveOrUpdate(cf);
			i = 1;
		} catch (Exception e) {
			i = 0;
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public List<ClassificationDto> getClassification() {
		List<ClassificationDto> result = dao.getClassification();
		return result;
	}

	@Override
	public int add(ClassificationDto dto) {
		int i = 0;
		try {
			String id = UUID.randomUUID().toString();// id
			Classification cf = dao.get(id);
			boolean exits = cf != null ? true : false;
			if (!exits) {
				cf = new Classification();
				cf.setId(id);
				cf.setInserted_at(DateUtil.getNowTimeStamp());// 创建时间
			}
			cf.setName(dto.getName());// 名称
			if (dto.getStatus() == 1) {
				cf.setStatus(ClassificationStatus.STARTUSE);// 状态
			} else {
				cf.setStatus(ClassificationStatus.FORBIDDEN);// 状态
			}
			if (dto.getType() == 1) {
				cf.setType(ClassificationType.SPECIAL);// 类型
			} else if (dto.getType() == 2) {
				cf.setType(ClassificationType.MULTILTVEL);// 类型
			} else {
				cf.setType(ClassificationType.COMMON);// 类型
			}
			cf.setUpdated_at(DateUtil.getNowTimeStamp());// 更新时间
			cf.setAlias(dto.getAlias());// 别名
			if (dto.getpId() != null && !dto.getpId().equals("")) {
				cf.setParent(dto.getpId());// 父id
			} else {
				cf.setParent("0");// 父id
			}
			if (dto.getEditable() == 1) {
				cf.setEditable(ClassificationEdit.STARTUSE);
			} else {
				cf.setEditable(ClassificationEdit.FORBIDDEN);
			}// 是否可编辑
			if (dto.getDeletable() == 1) {
				cf.setDeletable(ClassificationDele.STARTUSE);
			} else {
				cf.setDeletable(ClassificationDele.FORBIDDEN);
			}// 是否可删除
			if (dto.getRecommend() == 1) {
				cf.setRecommend(ClassificationRecommend.RECOMMEND);
			} else {
				cf.setRecommend(ClassificationRecommend.NOTRECOMMEND);
			}// 是否推荐
			if (dto.getLevel() == null) {
				cf.setLevel(0);
			} else {
				cf.setLevel(dto.getLevel() + 1);
			}// 层级
			if (dto.getSequence() != null) {
				cf.setSequence(dto.getSequence() + 1);
			} else {
				cf.setSequence(1);
			}// 分类排序
			dao.save(cf);
			i = 1;
		} catch (Exception e) {
			i = 0;
			e.printStackTrace();
		}
		return i;
	}

	/*@Override
	public int updateAll(List<ClassificationDto> all) {
		int j = 0;
		String sql = "update Classification c set c.lft=?,c.rgt=?,c.updated_at=? where c.id=?";
		for (ClassificationDto dto : all) {
			int i = dao.update(sql, Long.valueOf(dto.getLft()),
					Long.valueOf(dto.getRgt()), new Date(), dto.getId());
			if (i > 0) {
				j++;
			}
		}
		return j;
	}*/

	@Override
	public int remove(ClassificationDto dto) {
		int i = 0;
		String sql = "DELETE FROM Classification cc WHERE cc.id = ? or cc.parent =?";
		i = dao.deleteClassification(sql, dto.getId(), dto.getId());
		return i;
	}

	@Override
	public List<Classification> getClassificationF(String id) {
		// 获取实体
		List<Classification> list = new ArrayList<>();
		List<Classification> parent = null;
		try {
			Classification c = dao.get(id);
			if (c != null) {
				list.add(c);
				if (c.getParent() != null) {
					Classification topParent = getTopParent(c.getParent());
					parent = getAllParent(topParent, parent);
				}
			}
			if (parent != null) {
				list.addAll(parent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 递归设置层级.
	 */
	private List<Classification> getAllParent(Classification topParent,List<Classification> resultTrees) {
		if (topParent != null) {
			if (resultTrees == null) {
				resultTrees = new ArrayList<>();
			}
			resultTrees.add(topParent);
			if (topParent.getParent() != null) {
				Classification parent = getTopParent(topParent.getParent());
				getAllParent(parent, resultTrees);
			}
		}
		return resultTrees;
	}

	/**
	 * 查询某父节点下的直接子节点(即不包含孙子节点等)
	 */
	public Classification getTopParent(String parentId) {
		return dao.get(parentId);
	}

	@Override
	public List<Classification> getClassificationC(String id) {
		// 获取实体
		List<Classification> list = new ArrayList<>();
		List<Classification> children = new ArrayList<>();
		try {
			Classification c = dao.get(id);

			if (c != null) {
				list.add(c);
				if (c.getParent() != null) {
					List<Classification> topChildren = getTopChildren(c.getParent());
					children = getAllChildren(topChildren, children);
				}
				list.addAll(children);
				// list = dao.getClassificationC(c.getLft(), c.getRgt());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
//	/**
//	 * 查询某父节点下的直接子节点(即不包含孙子节点等)
//	 */
//	public List<Classification> getTopChildren(String parentId) {
//		return dao.getTopChildren(parentId);
//	}
//	
//	/**
//	 * 递归设置层级.
//	 */
//	private List<Classification> getAllChildren(List<Classification> topChildren, List<Classification> resultTrees) {
//		if (topChildren != null && topChildren.size() > 0) {
//			for (Classification tree : topChildren) {
//				resultTrees.add(tree);
//				List<Classification> children = getTopChildren(tree.getId());
//				getAllChildren(children, resultTrees);
//			}
//		}
//		return resultTrees;
//	}
	@Override
	public List<Classification> list() {
		return dao.list();
	}

	@Override
	public void update(Classification c) {
		dao.update(c);
	}

	@Override
	public List<Classification> getByName(String name) {
		return dao.getByName(name);
	}

	@Override
	public List<Classification> get(ClassificationDto dto) {
		// TODO Auto-generated method stub
		return dao.getByName(dto);
	}

	@Override
	public List<Classification> select(ClassificationDto dto) {
		return dao.get(dto);
	}
	
	
	//api
	@Override
	public List<Classification> list(Integer status) {
		return dao.listClassifications( status);
	}

	@Override
	public List<Classification> listChild(String id, Integer status) {
		// 获取实体
		List<Classification> list = new ArrayList<>();
		try {
			Classification c = dao.get(id);
			if (c != null) {
			
			List<Classification> topChildren = getTopChildren(id, status);
			list.addAll(topChildren);
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	/**
	 * 查询某父节点下的直接子节点(即不包含孙子节点等)
	 */
	public List<Classification> getTopChildren(String parentId,Integer status) {
		return dao.getTopChildren(parentId,status);
	}
	
	/**
	 * 递归设置层级.
	 */
	private List<Classification> getAllChildren(List<Classification> topChildren, List<Classification> resultTrees,Integer status) {
		if (topChildren != null && topChildren.size() > 0) {
			for (Classification tree : topChildren) {
				resultTrees.add(tree);
				List<Classification> children = getTopChildren(tree.getId(),status);
				getAllChildren(children, resultTrees);
			}
		}
		return resultTrees;
	}
	/**
	 * 查询某父节点下的直接子节点(即不包含孙子节点等)
	 */
	public List<Classification> getTopChildren(String parentId) {
		return dao.getTopChildren(parentId);
	}
	
	/**
	 * 递归设置层级.
	 */
	private List<Classification> getAllChildren(List<Classification> topChildren, List<Classification> resultTrees) {
		if (topChildren != null && topChildren.size() > 0) {
			for (Classification tree : topChildren) {
				resultTrees.add(tree);
				List<Classification> children = getTopChildren(tree.getId());
				getAllChildren(children, resultTrees);
			}
		}
		return resultTrees;
	}
	@Override
	public Classification get(String classification_id) {
		// TODO Auto-generated method stub
		return dao.get(classification_id);
	}

	@Override
	public void save(Classification classification) {
		dao.save(classification);
	}

	public void delete(String id) {
		dao.delete(id);
	}

	public List<ClassificationDto> getClassificationApi() {
		List<ClassificationDto> result = dao.getClassification();
		return result;
	}

	@Override
	public Classification getByNameApi(String classification_name) {
		Classification classification=dao.getByNameApi(classification_name);
		return classification;
	}

	@Override
	public List<Classification> listStorage(Integer status) {
		return dao.listStorage(status);
	}

	@Override
	public List<Classification> getByPid(String getpId) {
		return  dao.getByPid(getpId);
	}

	@Override
	public List<Classification> selectElevel(String parent, Integer sequence) {
		// TODO Auto-generated method stub
		return dao.selectElevel(parent,sequence);
	}

	@Override
	public void insert(List<Classification> news) {
		dao.save(news);
	}

	@Override
	public List<Classification> getClassificationF(String id, String parentId) {
		// 获取实体
		List<Classification> list = new ArrayList<>();
		List<Classification> parent = null;
		try {
			Classification c = dao.get(id);
			if (c != null) {
				list.add(c);
				if (c.getParent() != null) {
					Classification topParent = getTopParent(c.getParent());
					if(!topParent.getId().equals(parentId)) {
						parent = getAllParentUtil(topParent, parent,parentId);
					}
				}
			}
			if (parent != null) {
				list.addAll(parent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	private List<Classification> getAllParentUtil(Classification topParent,List<Classification> resultTrees,String exceptClass) {
		if (topParent != null) {
			if (resultTrees == null) {
				resultTrees = new ArrayList<>();
			}
			resultTrees.add(topParent);
			String parent2 = topParent.getParent();
			if (parent2 != null && !parent2.equals(exceptClass)) {
				Classification ttParent = getTopParent(topParent.getParent());
				if(ttParent!=null&& !ttParent.getId().equals(exceptClass)) {
					getAllParentUtil(ttParent, resultTrees,exceptClass);
				}
			}
		}
		return resultTrees;
	}


}
