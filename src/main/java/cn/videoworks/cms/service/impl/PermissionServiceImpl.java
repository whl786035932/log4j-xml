package cn.videoworks.cms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.PermissionDao;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Permission;
import cn.videoworks.cms.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService{
	@Resource
	private PermissionDao dao;

	@Override
	public List<Permission> list(String orderField, Page page) {
		return dao.list(orderField,page);
	}

	@Override
	public Page paging(Page page) {
		page.setRecordCount(dao.count());
		return page;
	}

	@Override
	public void save(Permission permission) {
		dao.save(permission);
	}

	@Override
	public Permission get(Integer id) {
		return dao.get(id);
	}

	@Override
	public void delete(Permission permission) {
		dao.delete(permission);
	}

	@Override
	public List<Permission> list() {
		return dao.list();
	}

	@Override
	public void update(Permission permission) {
		dao.update(permission);
	}

}
