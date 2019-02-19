package cn.videoworks.cms.service;

import java.util.List;
import java.util.Map;

import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.dto.TaskDto;
import cn.videoworks.cms.entity.Task;

public interface TaskService {

	int delete(List<Integer> ids);

	Map<String, Object> taskList(TaskDto dto);

	void save(Task task);

	List<Task> list(Map<String, Object> q, String string, Page page);
	List<Task> list(String insertedAt);
	List<Task> list();

	Page paging(Map<String, Object> q,Page page);

	Task get(Long taskId);
	void update(Task task);

	Task getByMsgId(String msgId);

	void delete(Long taskId);
}
