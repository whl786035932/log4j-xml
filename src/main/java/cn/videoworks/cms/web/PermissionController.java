package cn.videoworks.cms.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.dto.PermissionDto;
import cn.videoworks.cms.entity.Permission;
import cn.videoworks.cms.service.PermissionService;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.commons.webdev.constant.ResponseStatusCode;

@Controller
@RequestMapping(value="permission")
public class PermissionController {
	@Resource
	private PermissionService permissionServiceImpl;
	

	/**
	 * 获取分类树.
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String classificationTree(Model model) {
		// 获取跟结点
		return "site.cms.permission.index";
	}
	
	
	@RequestMapping("/ajax")
	@ResponseBody
	public Map<String, Object> ajax(String data) {
		Map<String, Object> rr = new HashMap<String, Object>();
		Map<String, Object> q = new HashMap();
		Page page = ConvertDto.convertPage(data);
		List<Permission> roles = permissionServiceImpl.list("inserted_at", page);
		ArrayList<PermissionDto> dtos = new ArrayList<PermissionDto>();
		for (Permission permission : roles) {
			PermissionDto dto = ConvertDto.convertPermission(permission);
			dtos.add(dto);
		}
		page = permissionServiceImpl.paging(page);

		buildRR(rr, dtos, page);
		return rr;
	}

	private void buildRR(Map<String, Object> rr, List<PermissionDto> permissions, Page page) {
		rr.put("data", permissions);
		rr.put("iTotalRecords", permissions.size());
		rr.put("iTotalDisplayRecords", page.getRecordCount());
	}
	
	//添加角色
	@RequestMapping(value = "add")
	@ResponseBody
	public RestResponse addRole(PermissionDto dto) {
		RestResponse restResponse = new RestResponse();
		Permission permission = new Permission();
		permission.setType(dto.getType());
		Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
		permissionServiceImpl.save(permission);
		restResponse.setStatusCode(ResponseStatusCode.OK);
		return restResponse;
	}

	
	
	/**
	 * 批量删除用户
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RestResponse delete(@RequestParam List<Integer> ids) {
		// 记录添加站点-操作日志
		if (null != ids && ids.size() > 0) {
			for (Integer id : ids) {
				Permission permission = permissionServiceImpl.get(id);
				permissionServiceImpl.delete(permission);
			}
		}
		return buildRestResponse(ResponseStatusCode.OK, null, null);

	}
	
	
	public RestResponse buildRestResponse(Integer statusCode, String message, Map<String, Object> data) {
		RestResponse restResponse = new RestResponse();
		restResponse.setStatusCode(statusCode);
		restResponse.setMessage(message);
		restResponse.setData(data);
		return restResponse;
	}	
	

	
}
