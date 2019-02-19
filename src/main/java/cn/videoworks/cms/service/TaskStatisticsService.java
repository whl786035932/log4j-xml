package cn.videoworks.cms.service;

import java.util.List;
import java.util.Map;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.TaskStatistics;

public interface TaskStatisticsService {

	void save(TaskStatistics taskStatistics);
	void delete(Long id);
	void update(TaskStatistics taskStatistics);
	TaskStatistics get(Long id);
	TaskStatistics get(String statisticsAt);
	
	/**
	 * 内容管理查询
	 */
	List<TaskStatistics> list(Map<String,Object> q,String order,String sort,Page page);
	Page paging(Map<String,Object> q,Page page);
}
