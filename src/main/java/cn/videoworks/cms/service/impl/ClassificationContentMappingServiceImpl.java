package cn.videoworks.cms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.ClassificationContentMappingDao;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.dto.RecommonedDto;
import cn.videoworks.cms.entity.ClassificationContentMapping;
import cn.videoworks.cms.service.ClassificationContentMappingService;

@Service
public class ClassificationContentMappingServiceImpl implements
		ClassificationContentMappingService {

	@Resource
	private ClassificationContentMappingDao dao;

	@Override
	public List<ClassificationContentMapping> list(Long content_id) {
		return dao.list(content_id);
	}

	@Override
	public void save(ClassificationContentMapping ccm) {
		dao.save(ccm);
	}

	@Override
	public List<ClassificationContentMapping> list(String classification_id,
			String title, int status, String order, Page page) {
		return dao.list(classification_id, title, status, order, page);
	}

	@Override
	public Page paging(String classification_id, String title, int status,
			Page page) {
		page.setRecordCount(dao.count(classification_id, title, status));
		return page;
	}

	@Override
	public int delete(String classid, List listIds) {
		int i = 0;
		String sql = "DELETE FROM ClassificationContentMapping cc WHERE cc.classification.id = ? AND cc.content.id=?";
		for (Object cid : listIds) {
			int j = dao.deletec(sql, classid, Long.valueOf(cid.toString()));
			if (j > 0) {
				i++;
			}
		}
		return i;
	}

	@Override
	public List<ClassificationContentMapping> list(String classification_id,
			Long content_id) {
		return dao.list(classification_id, content_id);
	}

	@Override
	public void delete(String id) {
		int i = 0;
		String sql = "DELETE FROM ClassificationContentMapping cc WHERE cc.classification.id = ?";
		int j = dao.deletec(sql, id);

	}

	@Override
	public List<ClassificationContentMapping> getRecommoned(String id) {
		// TODO Auto-generated method stub
		return dao.getRecommoned(id);
	}

	@Override
	public void updateRecommoned(List<RecommonedDto> dtos) {
		try {
			for (RecommonedDto dto : dtos) {
				if (dto.getOldId() != null) {
					ClassificationContentMapping c = dao.get(dto.getOldId());
					c.setRecommend(0);
					dao.update(c);
				}
				if (dto.getId() != null) {
					ClassificationContentMapping cn = dao.get(dto.getId());
					cn.setRecommend(dto.getRecommoned());
					dao.update(cn);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public ClassificationContentMapping get(Long id) {
		return dao.get(id);
	}

	@Override
	public void updateDrapRecommoned(List<RecommonedDto> dtos) {
		try {
			for (RecommonedDto dto : dtos) {
				if (dto.getId() != null) {
					ClassificationContentMapping cn = dao.get(dto.getId());
					cn.setRecommend(dto.getRecommoned());
					dao.update(cn);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	//api
	@Override
	public List<ClassificationContentMapping> listContentByClassId(
			String classification_id,Integer limit, Integer sequence) {
		return dao.listContentByClassId(classification_id,limit, sequence);
	}
	
	
	@Override
	public List<ClassificationContentMapping> listContentByClassIdOrderByPublishTime(String classification_id,
			Integer limit, Integer page) {
		// TODO Auto-generated method stub
		return dao.listContentByClassIdOrderByPublishTime(classification_id, limit, page);
	}
	
	@Override
	public Page paging(String classification_id, Page page) {
		page.setRecordCount(dao.count(classification_id));
		return page;
	}

	/**
	 * (分类下随机内容)
	 * @see cn.videoworks.cms.service.ClassificationContentMappingService#getContents(java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<ClassificationContentMapping> getContents(String classification_id, Integer limit) {
		return dao.getContents(classification_id, limit);
	}

	@Override
	public List<String> getClassifications(Long contentId) {
		return dao.getClassifications(contentId);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
