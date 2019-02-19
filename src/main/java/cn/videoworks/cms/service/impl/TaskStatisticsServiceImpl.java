package cn.videoworks.cms.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.TaskStatisticsDao;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.TaskStatistics;
import cn.videoworks.cms.service.TaskStatisticsService;

@Service
public class TaskStatisticsServiceImpl implements TaskStatisticsService {
	
	@Resource
	private TaskStatisticsDao dao;

	@Override
	public void save(TaskStatistics taskStatistics) {
		dao.save(taskStatistics);
	}

	@Override
	public void delete(Long id) {
		dao.delete(id);
	}

	@Override
	public void update(TaskStatistics taskStatistics) {
		dao.update(taskStatistics);
	}

	@Override
	public TaskStatistics get(Long id) {
		return dao.get(id);
	}

	@Override
	public TaskStatistics get(String statisticsAt) {
		return dao.get(statisticsAt);
	}

	@Override
	public List<TaskStatistics> list(Map<String, Object> q, String order, String sort, Page page) {
		return dao.list(q, order, sort, page);
	}

	@Override
	public Page paging(Map<String, Object> q, Page page) {
		page.setRecordCount(dao.count(q));
		return page;
	}

}
