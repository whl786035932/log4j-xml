/**
 * UserController.java
 * cn.videoworks.shelves.web
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月14日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import cn.videoworks.cms.dto.ClassificationDto;
import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.dto.LogDto;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.dto.UserDto;
import cn.videoworks.cms.dto.UserPasswordDto;
import cn.videoworks.cms.entity.Classification;
import cn.videoworks.cms.entity.Menu;
import cn.videoworks.cms.entity.Role;
import cn.videoworks.cms.entity.User;
import cn.videoworks.cms.entity.UserClassificationMapping;
import cn.videoworks.cms.entity.UserRoleMapping;
import cn.videoworks.cms.enumeration.BusinessType;
import cn.videoworks.cms.enumeration.LogLevel;
import cn.videoworks.cms.enumeration.LogSource;
import cn.videoworks.cms.enumeration.LogType;
import cn.videoworks.cms.enumeration.Status;
import cn.videoworks.cms.enumeration.UserType;
import cn.videoworks.cms.service.ClassificationService;
import cn.videoworks.cms.service.MenuService;
import cn.videoworks.cms.service.RoleService;
import cn.videoworks.cms.service.UserClassificationMappingService;
import cn.videoworks.cms.service.UserRoleMappingService;
import cn.videoworks.cms.service.UserService;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.util.Md5Util;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.cms.util.UserUtil;
import cn.videoworks.cms.vo.MenuVo;
import cn.videoworks.cms.vo.UserVo;
import cn.videoworks.commons.util.json.JsonConverter;
import cn.videoworks.commons.webdev.constant.ResponseStatusCode;

/**
 * ClassName:UserController
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月14日		下午3:59:20
 *
 * @see 	 
 */
@Controller
@SessionAttributes("user")
public class UserController {
	
	@Resource
	private Properties databaseConfig;
	
	@Resource
	private  UserService userService;
	
	@Resource
	private UserRoleMappingService userRoleMappingServiceImpl;
	
	@Resource
	private RoleService roleServiceImpl;
	
	@Resource
	private MenuService menuServiceImpl;
	
	@Resource
	private ClassificationService classificationServiceImpl;
	
	@Resource
	private UserClassificationMappingService userClassificationMappingServiceImpl;
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value = "",method = RequestMethod.GET)
	public String index() {
		return "raw.cms.user.login";
	}

	@RequestMapping(value = "login",method = RequestMethod.GET)
	public String login() {
		return "raw.cms.user.login";
	}
	
	@RequestMapping(value = "login",method = RequestMethod.POST)
	@ResponseBody
	public RestResponse loging(String username,String password, SessionStatus sessionStatus,ModelMap map,HttpServletRequest request) {
		//记录添加站点-操作日志
		Map<String,Object> detail = new HashMap<String,Object>();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("username", username);
		param.put("password", password);
		detail.put("param", JsonConverter.format(param));
		LogDto logDto = ConvertDto.buildOperationLog("CMS-用户登录", BusinessType.GET.getValue(), "登录管理", UserUtil.getUserUtil(username).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), detail);
		log.info(JsonConverter.format(logDto));
		
		RestResponse restresponse =new RestResponse();
		password = Md5Util.getMD5(password);
		User user = userService.getByNameAndPassword(username, password);
		 if(user!=null) {
			 List<Menu> list =  new ArrayList<Menu>();
			 ClassificationDto classificationsDto = new ClassificationDto();
			 if(null != user.getType() && user.getType() == UserType.SUPER.getValue()) {
				 list = menuServiceImpl.list("sequence");
				 List<ClassificationDto> all = classificationServiceImpl.getClassification();
				 classificationsDto = getClassificationTree(all) ;
			 }else {
				 classificationsDto = getClassificationTree(getClassification(user));
			 }
			 UserVo userVo =  cn.videoworks.cms.vo.ConvertDto.convertUser(user,list);
			 //安装菜单sequence排序
			 List<MenuVo> l = userVo.getMenus();
			 for(MenuVo vo : l ) {
				 Collections.sort(vo.getChildren());
			 }
			 
			 //设置分类
			 userVo.setClassifications(classificationsDto);
			
			map.addAttribute("user",userVo);//类上已经声明了一个session叫user，那么这里把user对应的值放入即可
			user.setLast_login_time(DateUtil.getNowTimeStamp());
			user.setCount(user.getCount()+1);
			userService.update(user);
			restresponse.setStatusCode(ResponseStatusCode.OK);
			restresponse.setMessage("登录成功！");
			
			HttpSession session = request.getSession();
			session.setAttribute("user", userVo);
		 }else {
			 sessionStatus.setComplete();
			restresponse.setStatusCode(ResponseStatusCode.BAD_REQUEST);
			restresponse.setMessage("用户名或者密码错误！");
		 }
		
		return restresponse;
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String logout(SessionStatus sessionStatus,ServletResponse response,HttpServletRequest request){
		//记录添加站点-操作日志
		LogDto logDto = ConvertDto.buildOperationLog("CMS-用户退出", BusinessType.DELETE.getValue(), "登录管理", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), new HashMap<String,Object>());
		log.info(JsonConverter.format(logDto));
		 UserUtil.clearUserUtil();  //清除用户工具类中的用户
		boolean isC = response.isCommitted();
		if (isC == false) {
			sessionStatus.setComplete();
			return "redirect:/";
		}
		return null;
	}
	
	@RequestMapping(value="list")
	public String list() {
		return "site.cms.user.index";
	}
	
	
	@RequestMapping("/ajax")
	@ResponseBody
	public Map<String, Object> ajax(String data) {
		Map<String, Object> rr = new HashMap<String, Object>();
		Map<String, Object> q = ConvertDto.convertDataTableSearchForUser(data);
		Page page = ConvertDto.convertPage(data);
		List<UserDto> dtos = new ArrayList<UserDto>();
		List<User> users = userService.list(q, "inserted_at", page);
		for (User user : users) {
			dtos.add(ConvertDto.convertUser(user));
		}
		page = userService.paging(q, page);

		buildRR(rr, dtos, page);
		return rr;
	}

	/**
	 * buildRR:(构建datatables数据)
	 *
	 * @author meishen
	 * @Date 2018 2018年5月30日 下午6:17:24
	 * @param rr
	 * @param contentsDto
	 * @param page
	 * @return void
	 * @throws @since
	 *             Videoworks Ver 1.1
	 */
	private void buildRR(Map<String, Object> rr, List<UserDto> userDtos, Page page) {
		rr.put("data", userDtos);
		rr.put("iTotalRecords", userDtos.size());
		rr.put("iTotalDisplayRecords", page.getRecordCount());
	}
	
	/**
	 * classifications:(获取分类树)
	 * @author   meishen
	 * @Date	 2018	2018年9月28日		下午5:26:45 
	 * @return RestResponse    
	 * @throws
	 */
	@RequestMapping(value = "classifications", method = RequestMethod.GET)
	@ResponseBody
	public RestResponse classifications() {
		RestResponse response = new RestResponse();
		Map<String, Object> tree = new HashMap<String, Object>();
		List<ClassificationDto> all = classificationServiceImpl.getClassification();
		ClassificationDto cc = getClassificationTree(all);
		if (cc != null) {
			tree.put("tree", JsonConverter.format(cc));
			response = new RestResponse();
			response.setData(tree);
			response.setStatusCode(200);
			return response;
		} else {
			tree.put("tree", null);
			response = new RestResponse();
			response.setData(tree);
			response.setStatusCode(200);
		}
		return response;
	}
	
	/**
	 * 获取栏目树.
	 */
	public ClassificationDto getClassificationTree(List<ClassificationDto> all) {
		// 获取分类根节点
				
				if (all != null && all.size() > 1) {
					for (int i = 1; i < all.size(); i++) {
						for (int j = i - 1; j >= 0; j--) {
							if (all.get(j).getId().equals(all.get(i).getpId())) {
								all.get(j).getChildren().add(all.get(i));
							}
							if (all.get(i).getId().equals(all.get(j).getpId())) {
								all.get(i).getChildren().add(all.get(j));
							}
						}
					}
				}
				// 设置根节点父id
				if (all != null && all.size() > 0) {
					ClassificationDto tree = null;
					for (ClassificationDto d : all) {
						if(d.getpId().equals("0")){
							tree = d;
							break;
						}
					}
					tree.setNocheck(true);
					return tree;
				}
				return null;
	}
	
	public static List<ClassificationDto> getClassification(User user){
		List<ClassificationDto> classificationDto = new ArrayList<ClassificationDto>();
		Set<UserClassificationMapping> userClassificationMappings = user.getUserClassificationMappings();
		for(UserClassificationMapping uc : userClassificationMappings) {
			Classification classification = uc.getClassification();
			if(null != classification)
				classificationDto.add(ConvertDto.convertClassification(classification));
		}
		return classificationDto;
	}
	
	@RequestMapping(value = "add")
	@ResponseBody
	public RestResponse addUser(UserDto dto) {
		RestResponse restResponse = new RestResponse();
		User sysUser = new User();
		sysUser.setUsername(dto.getUsername());
		String nickname = dto.getNickname();
		if(StringUtils.isNotBlank(nickname)) {
			sysUser.setNickname(nickname);
		}
		
		sysUser.setPassword(Md5Util.getMD5(dto.getPassword()));
		Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
		sysUser.setInserted_at(nowTimeStamp);
		sysUser.setUpdated_at(nowTimeStamp);
		sysUser.setLast_login_time(nowTimeStamp);
		sysUser.setStatus(Status.VALID.getValue());
		
		List<Integer> roles = dto.getRole();
		if(null != roles) {
			Set<UserRoleMapping> hashSet = new HashSet<>();
			for (Integer roleId : roles) {
				
				Role role = roleServiceImpl.get(roleId);
				UserRoleMapping userRoleMapping = new UserRoleMapping();
				userRoleMapping.setRole(role);
				userRoleMapping.setUser(sysUser);
				hashSet.add(userRoleMapping);
			}
			sysUser.setUserRoleMappings(hashSet);
		}
		sysUser.setType(UserType.UNSUPER.getValue());
		userService.save(sysUser);
		
		//添加分类信息
		if(StringUtils.isNotBlank(dto.getClassificationStr())) {
			String[] classificationIds = dto.getClassificationStr().split(",");
			List<String> claIds = new ArrayList<String>();
			for(String id : classificationIds) {
				List<Classification> classificationF = classificationServiceImpl.getClassificationF(id);
				for(Classification c : classificationF) {
					if(!claIds.contains(c.getId())) {
						claIds.add(c.getId());
						UserClassificationMapping userClassification = new UserClassificationMapping();
						userClassification.setClassification(new Classification(c.getId()));
						userClassification.setUser(new User(sysUser.getId()));
						userClassificationMappingServiceImpl.save(userClassification);
					}
				}
			}
		}
		
		restResponse.setStatusCode(ResponseStatusCode.OK);
		return restResponse;
	}

	@RequestMapping(value = "update")
	@ResponseBody
	public RestResponse updateUser(UserDto dto) {

		Integer id = dto.getId();
		User sysUser = userService.get(id);
		List<Integer> roles = dto.getRole();
		if (sysUser != null) {
			sysUser.getUserRoleMappings().clear();
			Set<UserRoleMapping> userRoleMappings = sysUser.getUserRoleMappings();
			if(null != roles) {
				for (Integer roleId : roles) {
					Role role = roleServiceImpl.get(roleId);
					UserRoleMapping userRoleMapping = new UserRoleMapping();
					userRoleMapping.setRole(role);
					userRoleMapping.setUser(sysUser);
					
					userRoleMappings.add(userRoleMapping);
				}
			}
			sysUser.setUserRoleMappings(userRoleMappings);
			String nickname = dto.getNickname();
			if(StringUtils.isNotBlank(nickname)) {
				sysUser.setNickname(nickname);
			}
			sysUser.setUsername(dto.getUsername());
			
			Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
			sysUser.setUpdated_at(nowTimeStamp);
			
			if(StringUtils.isNotBlank(dto.getClassificationStr())) {
				sysUser.getUserClassificationMappings().clear();
				String[] cIassificationIds = dto.getClassificationStr().split(",");
				List<String> claIds = new ArrayList<String>();
				for(String classId : cIassificationIds) {
					List<Classification> classificationF = classificationServiceImpl.getClassificationF(classId);
					for(Classification c : classificationF) {
						if(!claIds.contains(c.getId())) {
							claIds.add(c.getId());
							UserClassificationMapping userClassification = new UserClassificationMapping();
							userClassification.setClassification(new Classification(c.getId()));
							userClassification.setUser(new User(sysUser.getId()));
							sysUser.getUserClassificationMappings().add(userClassification);
						}
					}
				}
				
			}else {
				sysUser.getUserClassificationMappings().clear();
			}
			
			userService.update(sysUser);
		}
		RestResponse restResponse = new RestResponse();
		restResponse.setStatusCode(ResponseStatusCode.OK);
		return restResponse;
	}

	@RequestMapping(value = "updateUserStatus/{userId}/{status}")
	@ResponseBody
	public RestResponse disableUser(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "status") Integer status) {
		RestResponse restResponse = new RestResponse();
		restResponse.setStatusCode(ResponseStatusCode.OK);
		User user = userService.get(userId);
		if (user != null) {

			user.setStatus(status);
			userService.update(user);
		}
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
				User user = userService.get(id);
				user.getUserRoleMappings().clear();
				userService.delete(id);
			}
		}
		return buildRestResponse(ResponseStatusCode.OK, null, null);

	}
	
	
	@RequestMapping("/info")
	@ResponseBody
	public Map<String,Object> delete(@RequestParam(value="id") Integer id) {
		// 记录添加站点-操作日志
		User user = userService.get(id);
		HashMap<String, Object> hashMap = new HashMap<>();
		
		HashSet<Integer> hashSet = new HashSet<>();
		if(user!=null) {
			hashMap.put("id", user.getId());
			hashMap.put("userName", user.getUsername());
			hashMap.put("nickName",user.getNickname());
			Set<UserRoleMapping> userRoleMappings = user.getUserRoleMappings();
			if(userRoleMappings!=null&&userRoleMappings.size()>0) {
				Iterator<UserRoleMapping> iterator = userRoleMappings.iterator();
				while(iterator.hasNext()) {
					UserRoleMapping next = iterator.next();
					Role role = next.getRole();
					if(role!=null) {
						hashSet.add(role.getId());
					}
				}
			}
		}
		
		hashMap.put("selectedRoleS", hashSet);
		
		List<Role> roles = roleServiceImpl.list(1);
		hashMap.put("roles", roles);
		
		List<String> classificationIds = new ArrayList<String>();
		List<String> classificationAllIds = new ArrayList<String>();
		if(null != user && null != user.getUserClassificationMappings()) {
			Set<UserClassificationMapping> userClassificationMappings = user.getUserClassificationMappings();
			for(UserClassificationMapping m : userClassificationMappings) {
				if(null != m.getClassification()) {
					List<Classification> classificationC = classificationServiceImpl.getTopChildren(m.getClassification().getId());
					if(classificationC.size() <=0)
						classificationIds.add(m.getClassification().getId());
					classificationAllIds.add(m.getClassification().getId());
				}
			}
		}
		
		hashMap.put("classificationIds", classificationIds);//只显示子节点
		hashMap.put("classificationAllIds", classificationAllIds);//子节点已经其所有的父节点
		
		return hashMap;

	}
	
	public RestResponse buildRestResponse(Integer statusCode, String message, Map<String, Object> data) {
		RestResponse restResponse = new RestResponse();
		restResponse.setStatusCode(statusCode);
		restResponse.setMessage(message);
		restResponse.setData(data);
		return restResponse;
	}
	
	
	
	@RequestMapping(value = "updatePassword")
	@ResponseBody
	public RestResponse updatePassword(UserDto dto) {
		Integer id = dto.getId();
		User sysUser = userService.get(id);
		if (sysUser != null) {
			
			String password = dto.getPassword();
			String md5 = Md5Util.getMD5(password);
			sysUser.setPassword(md5);
			Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
			sysUser.setUpdated_at(nowTimeStamp);

			userService.update(sysUser);
		}
		RestResponse restResponse = new RestResponse();
		restResponse.setStatusCode(ResponseStatusCode.OK);
		return restResponse;
	}
	
	/**
	 * updateCurrentPassword:(修改当前登录用户密码)
	 * @author   meishen
	 * @Date	 2018	2018年10月18日		上午11:03:34 
	 * @return RestResponse    
	 * @throws
	 */
	@RequestMapping(value = "updateCurrentPassword")
	@ResponseBody
	public RestResponse updateCurrentPassword(UserPasswordDto dto) {
		RestResponse restResponse = new RestResponse();
		Integer id = dto.getCurrentId();
		User sysUser = userService.get(id);
		if (sysUser != null) {
			String password = dto.getCurrentPassword();
			String md5 = Md5Util.getMD5(password);
			sysUser.setPassword(md5);
			Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
			sysUser.setUpdated_at(nowTimeStamp);
			userService.update(sysUser);
			restResponse.setStatusCode(ResponseStatusCode.OK);
		}else {
			restResponse.setStatusCode(ResponseStatusCode.NOT_FOUND);
		}
		return restResponse;
	}
	
}

