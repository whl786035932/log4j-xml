package cn.videoworks.cms.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.dao.UserRoleMappingDao;
import cn.videoworks.cms.service.UserRoleMappingService;

@Service
public class UserRoleMappingServiceImpl implements UserRoleMappingService{
	
	@Resource
	private UserRoleMappingDao dao;
}
