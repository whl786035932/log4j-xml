package cn.videoworks.cms.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.RoleDao;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Role;
import cn.videoworks.cms.service.RoleService;

@Service
public class RoleServiceImpl  implements RoleService{
	
	@Resource
	private RoleDao dao;

	@Override
	public List<Role> list(int valid_status) {
		return dao.list(valid_status);
	}

	@Override
	public Role get(Integer role) {
		// TODO Auto-generated method stub
		return dao.get(role);
	}

	@Override
	public List<Role> list(Map<String, Object> q, String orderField, Page page) {
		return dao.list(q,orderField,page);
	}

	@Override
	public Page paging(Map<String, Object> q, Page page) {
		page.setRecordCount(dao.count());
		return page;
	}

	@Override
	public void save(Role role) {
		dao.save(role);
	}

	@Override
	public void update(Role role) {
		dao.update(role);
	}

	@Override
	public void delete(Role role) {
		dao.delete(role);
	}
}
