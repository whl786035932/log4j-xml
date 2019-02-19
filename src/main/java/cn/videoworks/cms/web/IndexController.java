package cn.videoworks.cms.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.TaskStatistics;
import cn.videoworks.cms.service.TaskStatisticsService;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.cms.vo.TaskStatisticsVo;
import cn.videoworks.commons.webdev.constant.ResponseStatusCode;

/**
 * @author   meishen
 * @Date	 2018	2018年9月25日		下午6:09:26
 * @Description 方法描述: 首页
 */
@Controller
@RequestMapping("index")
public class IndexController {

	@Resource
	private TaskStatisticsService taskStatisticeServiceImpl;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(ModelMap map) {
		String insertedAt = DateUtil.getNowTimeYMD();
		TaskStatistics taskStatistics =taskStatisticeServiceImpl.get(insertedAt);
		TaskStatisticsVo taskStatisticsVo = ConvertDto.convertTaskStatistics(taskStatistics);
		map.put("taskStatistics", taskStatisticsVo);
		return "site.cms.index.index";
	}
	
	@RequestMapping("/ajax")
	@ResponseBody
	public Map<String, Object> ajax(String data) {
		Map<String, Object> rr = new HashMap<String, Object>();
		Map<String, Object> q = ConvertDto.convertDataTableSearchForTaskStatistics(data);
		Page page = ConvertDto.convertPage(data);
		List<TaskStatisticsVo> dtos = new ArrayList<TaskStatisticsVo>();
		String order = q.containsKey("order") == true ? q.get("order").toString() : "";
		String sort = q.containsKey("sort") == true ? q.get("sort").toString(): "";
		List<TaskStatistics> taskStatisticses = taskStatisticeServiceImpl.list(q, order, sort, page);
		for (TaskStatistics taskStatistics : taskStatisticses) {
			TaskStatisticsVo dto = ConvertDto.convertTaskStatistics(taskStatistics);
			dtos.add(dto);
		}
		page = taskStatisticeServiceImpl.paging(q, page);

		buildRR(rr, dtos, page);
		return rr;
	}
	
	/**
	 * get:(获取统计详情)
	 * @author   meishen
	 * @Date	 2018	2018年11月19日		下午7:07:20 
	 * @return RestResponse    
	 * @throws
	 */
	@RequestMapping("/get/{id}")
	@ResponseBody
	public RestResponse get(@PathVariable Long id) {
		RestResponse response = new RestResponse();
		TaskStatistics taskStatisticse= taskStatisticeServiceImpl.get(id);
		TaskStatisticsVo dto = new TaskStatisticsVo();
		if (null != taskStatisticse) {
			dto = ConvertDto.convertTaskStatistics(taskStatisticse);
		}
		response.setStatusCode(ResponseStatusCode.OK);
		response.setMessage("获取成功");
		response.getData().put("taskStatistics",dto);
		return response;
	}
	
	private void buildRR(Map<String, Object> rr, List<TaskStatisticsVo> taskStatisticsVos,
			Page page) {
		rr.put("data", taskStatisticsVos);
		rr.put("iTotalRecords", taskStatisticsVos.size());
		rr.put("iTotalDisplayRecords", page.getRecordCount());
	}
}
