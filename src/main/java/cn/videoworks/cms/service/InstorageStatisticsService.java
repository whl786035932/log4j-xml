package cn.videoworks.cms.service;

import java.util.List;

import cn.videoworks.cms.entity.InstorageStatistics;

public interface InstorageStatisticsService {

	List<InstorageStatistics> getRoot(String date);
	
	InstorageStatistics get(String name,String statisticsAt,InstorageStatistics parent);
	
	void save(InstorageStatistics instorageStatistics);
	
	void update(InstorageStatistics instorageStatistics);

	List<InstorageStatistics> getRoots(String dateBegin, String dateEnd);

}
