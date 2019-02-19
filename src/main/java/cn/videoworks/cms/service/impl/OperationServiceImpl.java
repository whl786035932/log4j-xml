package cn.videoworks.cms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.OperationDao;
import cn.videoworks.cms.entity.Operation;
import cn.videoworks.cms.service.OperationService;

@Service
public class OperationServiceImpl implements OperationService {

	@Resource
	private OperationDao dao;
	@Override
	public List<Operation> list() {
		return dao.list();
	}
	@Override
	public Operation get(Integer id) {
		return dao.get(id);
	}
	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}
	@Override
	public void save(Operation operation) {
		dao.save(operation);
	}
	@Override
	public void update(Operation operation) {
		dao.update(operation);
	}
	@Override
	public List<Operation> getChildOperationByParentId(Integer parentId) {
		
		return dao.getChildOperationByParentId(parentId);
	}
	@Override
	public List<Operation> list(String orderField) {
		return dao.list(orderField);
	}
	@Override
	public void deleteAll() {
		dao.deleteAll();
	}

}
