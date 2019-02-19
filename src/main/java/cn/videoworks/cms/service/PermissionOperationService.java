package cn.videoworks.cms.service;

import cn.videoworks.cms.entity.PermissionOperatoinMapping;

public interface PermissionOperationService {

	void deleteAll();

	void save(PermissionOperatoinMapping permissionOperatoinMapping);

}
