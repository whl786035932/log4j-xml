package cn.videoworks.cms.service;

import java.util.List;

import cn.videoworks.cms.entity.Operation;

public interface OperationService {

	List<Operation> list();

	Operation get(Integer id);

	void delete(Integer id);

	void save(Operation operation);

	void update(Operation operation);

	List<Operation> getChildOperationByParentId(Integer parentId);

	List<Operation> list(String string);

	void deleteAll();

}
