package cn.videoworks.cms.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.TaskDao;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.dto.TaskDto;
import cn.videoworks.cms.entity.Task;
import cn.videoworks.cms.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {
	@Resource
	private TaskDao dao;

	@Override
	public int delete(List<Integer> ids) {
		int i = 0;
		try {
			for (int id : ids) {
				dao.delete(id);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public Map<String, Object> taskList(TaskDto dto) {
		return dao.list(dto);
	}

	@Override
	public void save(Task task) {
		dao.save(task);
	}

	public List<Task> list(Map<String, Object> q, String orderField, Page page) {
		return dao.list(q,orderField,page);
	}

	@Override
	public Page paging(Map<String, Object> q,Page page) {
		// TODO Auto-generated method stub
		page.setRecordCount(dao.count(q));
		return page;
	}

	@Override
	public Task get(Long taskId) {
		return dao.get(taskId);
	}

	@Override
	public void update(Task task) {
		dao.update(task);
	}

	@Override
	public Task getByMsgId(String msgId) {
		
		return dao.getByMsgId(msgId);
	}

	@Override
	public void delete(Long taskId) {
		dao.delete(taskId);
	}

	@Override
	public List<Task> list(String insertedAt) {
		return dao.list(insertedAt);
	}

	@Override
	public List<Task> list() {
		return dao.list();
	}

}
