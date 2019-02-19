package cn.videoworks.cms.service.impl;



import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.ClassificationOldDao;
import cn.videoworks.cms.entity.ClassificationOld;
import cn.videoworks.cms.service.ClassificationOldService;

@Service
public class ClassificationServiceIOldmpl implements ClassificationOldService {
	@Resource
	private ClassificationOldDao dao;

	@Override
	public List<ClassificationOld> getAll() {
		return dao.getAll();
	}



}
