package cn.videoworks.cms.service;

import java.util.List;
import java.util.Map;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.ApkVersion;

public interface ApkVersionService {

	void add(ApkVersion apkVersion);

	ApkVersion get(Integer apkId);
	
	void delete(Integer apkId);

	void update(ApkVersion apkVersion);

	List<ApkVersion> list(Map<String, Object> q, String string, Page page);

	Page paging(Map<String, Object> q, Page page);

	List<ApkVersion> list(int status,Integer type);

	void updateOtherStatus(Integer type, Integer status);

	ApkVersion get(Integer type, int value);

	ApkVersion getByType(Integer type,String mainVersion,Integer childVersion);

}
