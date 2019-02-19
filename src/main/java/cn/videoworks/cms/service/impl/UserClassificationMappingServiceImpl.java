package cn.videoworks.cms.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.UserClassificationMappingDao;
import cn.videoworks.cms.entity.UserClassificationMapping;
import cn.videoworks.cms.service.UserClassificationMappingService;

@Service
public class UserClassificationMappingServiceImpl implements UserClassificationMappingService {
	
	@Resource
	private UserClassificationMappingDao dao;

	@Override
	public void save(UserClassificationMapping userClassificationMapping) {
		dao.save(userClassificationMapping);
	}

}
