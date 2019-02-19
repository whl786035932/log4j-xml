package cn.videoworks.cms.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.UserDao;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.User;
import cn.videoworks.cms.service.UserService;

@Service
public class UserServiceImpl  implements UserService{

	@Resource
	private UserDao dao;

	@Override
	public List<User> list(Map<String, Object> q, String orderField, Page page) {
		return dao.list(q,orderField,page );
	}

	@Override
	public Page paging(Map<String, Object> q, Page page) {
		page.setRecordCount(dao.count(q));
		return page;
	}

	@Override
	public void save(User sysUser) {
		dao.save(sysUser);
	}

	@Override
	public User get(Integer id) {
		// TODO Auto-generated method stub
		return dao.get(id);
	}

	@Override
	public void update(User sysUser) {
		dao.update(sysUser);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public User getByNameAndPassword(String username, String password) {
		
		return dao.getByNameAndPassword(username,password);
	}

	@Override
	public Long hasUsers() {
		return dao.count();
	}

	@Override
	public User getUser(String userName, int type) {
		return dao.getUser(userName, type);
	}
}
