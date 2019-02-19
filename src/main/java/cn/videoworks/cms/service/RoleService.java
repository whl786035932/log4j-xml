package cn.videoworks.cms.service;

import java.util.List;
import java.util.Map;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Role;

public interface RoleService {

	List<Role> list(int valid_status);

	Role get(Integer role);

	List<Role> list(Map<String, Object> q, String string, Page page);

	Page paging(Map<String, Object> q, Page page);

	void save(Role role);

	void update(Role role);

	void delete(Role role);

}
