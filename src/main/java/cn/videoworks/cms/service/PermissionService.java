package cn.videoworks.cms.service;

import java.util.List;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.Permission;

public interface PermissionService {

	List<Permission> list(String string, Page page);

	Page paging(Page page);

	void save(Permission permission);

	void update(Permission permission);
	
	Permission get(Integer id);

	void delete(Permission permission);

	List<Permission> list();

}
