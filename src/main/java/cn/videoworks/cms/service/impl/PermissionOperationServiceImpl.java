package cn.videoworks.cms.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.PermissionOperationDao;
import cn.videoworks.cms.entity.PermissionOperatoinMapping;
import cn.videoworks.cms.service.PermissionOperationService;

@Service
public class PermissionOperationServiceImpl implements PermissionOperationService{

	@Resource
	private PermissionOperationDao dao;
	@Override
	public void deleteAll() {
		dao.deleteAll();
	}
	@Override
	public void save(PermissionOperatoinMapping permissionOperatoinMapping) {
		dao.save(permissionOperatoinMapping);
	}
	

}
