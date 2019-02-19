package cn.videoworks.cms.schedule;

import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.entity.Task;
import cn.videoworks.cms.entity.TaskStatistics;
import cn.videoworks.cms.enumeration.TaskStatus;
import cn.videoworks.cms.service.TaskService;
import cn.videoworks.cms.service.TaskStatisticsService;
import cn.videoworks.cms.util.DateUtil;

@Component
public class AutoTaskStatisticsSchedule {
	
	@Resource
	private TaskStatisticsService taskStatisticsServiceImpl;
	
	@Resource
	private TaskService taskServiceImpl;
	
	@Resource
	private Properties databaseConfig;
	
	private static final Logger log = LoggerFactory.getLogger(AutoTaskStatisticsSchedule.class);

	/**
	 * taskStatistics:(每10分钟计算一次任务统计数量)
	 * @author   meishen
	 * @Date	 2018	2018年10月26日		下午5:34:17 
	 * @return void    
	 * @throws
	 */
	@Scheduled(cron="0 */10 * * * ?")
//	@Scheduled(cron="0/10 * * * * ?")
	public void taskStatistics() {
		boolean isBooths = databaseConfig.containsKey("is.booths")?Boolean.valueOf(databaseConfig.getProperty("is.booths")):true;
		if(!isBooths) {
			String statisticsAt = DateUtil.getNowTimeYMD();
			List<Task> tasks = taskServiceImpl.list(statisticsAt);
			int totalNumber = 0;
			int successNumber = 0;
			int waitingNumber = 0;
			int failureNumber = 0;
			if(null != tasks && tasks.size() >0) {
				totalNumber = tasks.size();
				for (Task task : tasks) {
					if(task.getStatus() == TaskStatus.SUCCESS.getValue())
						successNumber ++;
					else if(task.getStatus() == TaskStatus.ONGOING.getValue())
						waitingNumber ++;
					else if (task.getStatus() == TaskStatus.ERROR.getValue())
						failureNumber ++;
				}
				
				TaskStatistics taskStatistics = taskStatisticsServiceImpl.get(statisticsAt);
				if(null  != taskStatistics ) {
					ConvertDto.convertTaskStatistics1(taskStatistics, totalNumber, successNumber, waitingNumber, failureNumber);
					taskStatisticsServiceImpl.update(taskStatistics);
				}else {
					TaskStatistics convertTaskStatistics = ConvertDto.convertTaskStatistics(totalNumber, successNumber, waitingNumber, failureNumber,0);
					taskStatisticsServiceImpl.save(convertTaskStatistics);
					log.info("CMS定时任务统计功能，当天任务统计记录不存在，创建成功!");
				}
			}
		}
	}
	
	/**
	 * taskStatisticsBeforOne:(每天凌晨2点重新计算前一天的数据，解决上面10分钟时间间隔产生的数据差距)
	 * @author   meishen
	 * @Date	 2018	2018年10月26日		下午5:32:22 
	 * @return void    
	 * @throws
	 */
	@Scheduled(cron="0 0 2 * * ?")
	public void taskStatisticsBeforOne() {
		boolean isBooths = databaseConfig.containsKey("is.booths")?Boolean.valueOf(databaseConfig.getProperty("is.booths")):true;
		if(!isBooths) {
			String beforeOneAt = DateUtil.getPastDate(1);
			String statisticsAt = DateUtil.getDateStrYMD(beforeOneAt);
			List<Task> tasks = taskServiceImpl.list(statisticsAt);
			int totalNumber = 0;
			int successNumber = 0;
			int waitingNumber = 0;
			int failureNumber = 0;
			if(null != tasks && tasks.size() >0) {
				totalNumber = tasks.size();
				for (Task task : tasks) {
					if(task.getStatus() == TaskStatus.SUCCESS.getValue())
						successNumber ++;
					else if(task.getStatus() == TaskStatus.ONGOING.getValue())
						waitingNumber ++;
					else if (task.getStatus() == TaskStatus.ERROR.getValue())
						failureNumber ++;
				}
				
				TaskStatistics taskStatistics = taskStatisticsServiceImpl.get(statisticsAt);
				if(null  != taskStatistics ) {
					ConvertDto.convertTaskStatistics1(taskStatistics, totalNumber, successNumber, waitingNumber, failureNumber);
					taskStatisticsServiceImpl.update(taskStatistics);
				}else {
					TaskStatistics convertTaskStatistics = ConvertDto.convertTaskStatistics(totalNumber, successNumber, waitingNumber, failureNumber,0);
					convertTaskStatistics.setInsertedAt(DateUtil.getDate(beforeOneAt));
					taskStatisticsServiceImpl.save( convertTaskStatistics);
				}
			}
		}
	}
}
