package cn.videoworks.cms.service;

import java.util.List;
import java.util.Map;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.User;

public interface UserService {

	List<User> list(Map<String, Object> q, String string, Page page);

	Page paging(Map<String, Object> q, Page page);

	void save(User sysUser);

	User get(Integer id);

	void update(User sysUser);

	void delete(Integer id);

	User getByNameAndPassword(String username, String password);

	Long hasUsers();

	User getUser(String userName,int type);
}
