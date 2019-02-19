package cn.videoworks.cms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.InstorageStatisticsDao;
import cn.videoworks.cms.entity.InstorageStatistics;
import cn.videoworks.cms.service.InstorageStatisticsService;

@Service
public class InstorageStatisticsServiceImpl implements InstorageStatisticsService {
	@Resource
	private InstorageStatisticsDao dao;

	@Override
	public List<InstorageStatistics> getRoot(String date) {
		return dao.list(date);
	}

	@Override
	public InstorageStatistics get(String name,String statisticsAt,InstorageStatistics parent) {
		return dao.get(name,statisticsAt,parent);
	}

	@Override
	public void save(InstorageStatistics instorageStatistics) {
		dao.save(instorageStatistics);
	}

	@Override
	public void update(InstorageStatistics instorageStatistics) {
		dao.update(instorageStatistics);
	}

	@Override
	public List<InstorageStatistics> getRoots(String dateBegin, String dateEnd) {
		return dao.getRoots(dateBegin, dateEnd);
	}
}
