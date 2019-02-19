package cn.videoworks.cms.service;

import java.util.List;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.dto.RecommonedDto;
import cn.videoworks.cms.entity.ClassificationContentMapping;

public interface ClassificationContentMappingService {

	List<ClassificationContentMapping> list(Long content_id);
	void save(ClassificationContentMapping ccm);
	
	List<ClassificationContentMapping> list(String classification_id,String title,int status,String order,Page page);
	Page paging(String classification_id,String title,int status,Page page);
	int delete(String classid, List listIds);
	
	 List<ClassificationContentMapping> list(String classification_id,Long content_id);
	void delete(String id);
	List<ClassificationContentMapping> getRecommoned(String id);

	void updateRecommoned(List<RecommonedDto> dtos);
	ClassificationContentMapping get(Long id);
	void updateDrapRecommoned(List<RecommonedDto> dtos);
	
	
	//api
	List<ClassificationContentMapping> listContentByClassId(
			String classification_id,Integer limit, Integer sequence);
//
//	void removeMappingsByClassiId(String id);
//
//	void update(ClassificationContentMapping mapping);
//
//	void removeMappingsByClassIdAndContentId(String mapping_parent_id,
//			Long mapping_element_id);
//
//	ClassificationContentMapping findByClassIdAndContentId(
//			String mapping_parent_id, long contentId);
//
//	void saveOrUpdate(ClassificationContentMapping mapping);
//
//	void deleteByContentId(String contentId);
//
//	void delete(ClassificationContentMapping mapping);
//
	List<ClassificationContentMapping> listContentByClassIdOrderByPublishTime(String classification_id, Integer limit,
			Integer page);
	Page paging(String classification_id,Page page);
	
	List<ClassificationContentMapping> getContents(String classification_id,Integer limit) ;
	
	List<String> getClassifications(Long contentId);
	
}
