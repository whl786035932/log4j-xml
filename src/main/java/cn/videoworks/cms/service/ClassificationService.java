package cn.videoworks.cms.service;

import java.util.List;
import java.util.Map;

import cn.videoworks.cms.dto.ClassificationDto;
import cn.videoworks.cms.entity.Classification;

public interface ClassificationService {

	Map<String, Object> getDetail(String id);

	int saveClassification(ClassificationDto dto);

	List<ClassificationDto> getClassification();

	int add(ClassificationDto dto);

	//int updateAll(List<ClassificationDto> all);

	int remove(ClassificationDto dto);

	List<Classification> getClassificationF(String id);

	List<Classification> getClassificationC(String classid);

	List<Classification> list();
	void update(Classification c);
	
	List<Classification> getByName(String name);

	List<Classification> get(ClassificationDto dto);

	List<Classification> select(ClassificationDto dto);
	
	//api
	List<Classification> list(Integer status);

	List<Classification> listChild(String id,Integer status);

	Classification get(String classification_id);

	void save(Classification classification);


	List<ClassificationDto> getClassificationApi();

	Classification getByNameApi(String classification_name);

	List<Classification> listStorage(Integer status);

	List<Classification> getByPid(String getpId);

	List<Classification> selectElevel(String parent, Integer sequence);

	List<Classification> getTopChildren(String parentId);

	void insert(List<Classification> news);

	List<Classification> getClassificationF(String id, String parentId);


}
