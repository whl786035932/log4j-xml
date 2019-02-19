package cn.videoworks.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.ApkVersionDao;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.ApkVersion;
import cn.videoworks.cms.service.ApkVersionService;

@Service
public class ApkVersionServiceImpl implements ApkVersionService {

	@Autowired
	private ApkVersionDao dao;

	@Override
	public void add(ApkVersion apkVersion) {
		dao.save(apkVersion);
	}

	@Override
	public ApkVersion get(Integer apkId) {
		return dao.get(apkId);
	}

	@Override
	public void update(ApkVersion apkVersion) {
		dao.update(apkVersion);
	}

	@Override
	public List<ApkVersion> list(Map<String, Object> q, String orderField, Page page) {
		return dao.list(q,orderField,page );
	}

	@Override
	public Page paging(Map<String, Object> q, Page page) {
		page.setRecordCount(dao.count(q));
		return page;
	}

	@Override
	public List<ApkVersion> list(int status,Integer  type){
		return dao.listByStatus(status,type);
	}

	@Override
	public void updateOtherStatus(Integer type, Integer status) {
		dao.updateOtherStatus(type, status);
	}

	@Override
	public ApkVersion get(Integer type, int status) {
		return dao.getByStatusAndType(type,status);
	}

	@Override
	public ApkVersion getByType(Integer type,String mainVersion,Integer childVersion) {
		return dao.getByType(type, mainVersion, childVersion);
	}

	@Override
	public void delete(Integer apkId) {
		dao.delete(apkId);
	}
	
}
