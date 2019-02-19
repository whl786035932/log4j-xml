package cn.videoworks.cms.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.dto.OperationDto;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.dto.PermissonRequestDto;
import cn.videoworks.cms.dto.RoleDto;
import cn.videoworks.cms.entity.Menu;
import cn.videoworks.cms.entity.Operation;
import cn.videoworks.cms.entity.Permission;
import cn.videoworks.cms.entity.PermissionMenuMapping;
import cn.videoworks.cms.entity.PermissionOperatoinMapping;
import cn.videoworks.cms.entity.Role;
import cn.videoworks.cms.entity.RolePermissionMapping;
import cn.videoworks.cms.enumeration.PermissionType;
import cn.videoworks.cms.enumeration.Status;
import cn.videoworks.cms.service.MenuService;
import cn.videoworks.cms.service.OperationService;
import cn.videoworks.cms.service.RoleService;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.cms.vo.MenuVo;
import cn.videoworks.commons.util.json.JsonConverter;
import cn.videoworks.commons.webdev.constant.ResponseStatusCode;

@Controller
@RequestMapping(value = "role")
public class RoleController {
	@Resource
	private RoleService roleServiceImpl;

	@Resource
	private OperationService operationService;

	@Resource
	private MenuService menuServiceImpl;

	@RequestMapping(value = "list")
	public String list() {
		return "site.cms.role.index";
	}

	@RequestMapping(value = "allRoles")
	@ResponseBody
	public Map<String, Object> allRoles() {
		HashMap<String, Object> hashMap = new HashMap<>();
		int valid_status = Status.VALID.getValue();

		List<Role> list = roleServiceImpl.list(valid_status);
		hashMap.put("roles", list);
		return hashMap;
	}

	@RequestMapping("/ajax")
	@ResponseBody
	public Map<String, Object> ajax(String data) {
		Map<String, Object> rr = new HashMap<String, Object>();
		Map<String, Object> q = new HashMap<String, Object>();
		Page page = ConvertDto.convertPage(data);
		List<RoleDto> dtos = new ArrayList<RoleDto>();
		List<Role> roles = roleServiceImpl.list(q, "inserted_at", page);
		for (Role role : roles) {
			dtos.add(ConvertDto.convertRole(role));
		}
		page = roleServiceImpl.paging(q, page);

		buildRR(rr, dtos, page);
		return rr;
	}

	private void buildRR(Map<String, Object> rr, List<RoleDto> roleDtos, Page page) {
		rr.put("data", roleDtos);
		rr.put("iTotalRecords", roleDtos.size());
		rr.put("iTotalDisplayRecords", page.getRecordCount());
	}

	// 添加角色
	@RequestMapping(value = "add")
	@ResponseBody
	public RestResponse addRole(RoleDto dto) {
		RestResponse restResponse = new RestResponse();

		Role role = new Role();
		role.setName(dto.getName());
		String description = dto.getDescription();
		role.setDescription(description);
		Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
		role.setInserted_at(nowTimeStamp);
		role.setUpdated_at(nowTimeStamp);
		roleServiceImpl.save(role);
		restResponse.setStatusCode(ResponseStatusCode.OK);
		return restResponse;
	}

	@RequestMapping(value = "update")
	@ResponseBody
	public RestResponse updateRole(RoleDto dto,SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		Integer id = dto.getId();
		Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
		Role role = roleServiceImpl.get(dto.getId());
		if (role != null) {
			role.setName(dto.getName());
			role.setDescription(dto.getDescription());
			role.setUpdated_at(nowTimeStamp);
			roleServiceImpl.update(role);
		}

		RestResponse restResponse = new RestResponse();
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
				Role role = roleServiceImpl.get(id);
				role.getUserRoleMappings().clear();
				roleServiceImpl.delete(role);
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

	@RequestMapping("/info")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam(value = "id") Integer id) {
		Role role = roleServiceImpl.get(id);
		HashMap<String, Object> hashMap = new HashMap<>();

		HashSet<Integer> hashSet = new HashSet<>();
		if (role != null) {
			hashMap.put("id", role.getId());
			hashMap.put("name", role.getName());
			hashMap.put("description", role.getDescription());
		}
		return hashMap;

	}

	@RequestMapping("permissions/{roleId}")
	@ResponseBody
	public Map<String, Object> permissions(@PathVariable(value="roleId") Integer roleId) {
		
		
		Map<String, List<Integer>> rolesPermissions = getRolesPermissions(roleId);
		List<Integer> operationIds = rolesPermissions.get("operations");  //角色已经被分配的操作权限
		List<Integer> menus_permissions = rolesPermissions.get("menus");  //角色被分配的菜单权限
		
		HashMap<String, Object> modelMap = new HashMap<>();
		List<Operation> all = operationService.list("sequence");
		List<OperationDto> dtos = convertOperationTodtos(all,operationIds);
		OperationDto operationTree = getOperationTree(dtos);
		modelMap.put("operationTreeJson", JsonConverter.format(operationTree));

		// 获取菜单
		List<Menu> menus = menuServiceImpl.list("sequence");

		// 根节点
		List<MenuVo> rootMenu = new ArrayList<MenuVo>();

		for (Menu menu : menus) {
			if (null == menu.getParent() || menu.getParent() == 0) {// 父节点是0的，为根节点。
				rootMenu.add(convertMenu(menu,menus_permissions));
			}
		}

		// 为根菜单设置子菜单，getClild是递归调用的
		for (MenuVo nav : rootMenu) {
			/* 获取根节点下的所有子节点 使用getChild方法 */
			List<MenuVo> childList = getChild(nav.getId(), menus,menus_permissions);
			nav.setChildren(childList);// 给根节点设置子节点
		}

		modelMap.put("menuTreeJson", JsonConverter.format(rootMenu));

		return modelMap;

	}
	
	
	
	public Map<String,List<Integer>> getRolesPermissions(Integer roleId){
		Role role = roleServiceImpl.get(roleId);
		Set<RolePermissionMapping> permissionRoleMappings = role.getPermissionRoleMappings();
		Iterator<RolePermissionMapping> iterator = permissionRoleMappings.iterator();
		
		ArrayList<Integer> operations = new ArrayList<>();
		ArrayList<Integer> menus = new ArrayList<>();
		while(iterator.hasNext()) {
			RolePermissionMapping next = iterator.next();
			Permission permisson = next.getPermisson();
			if(null != permisson) {
				Integer type = permisson.getType();
				if(type==PermissionType.OPERATION.getValue()) {
					List<PermissionOperatoinMapping> permissionOperationMappings = permisson.getPermissionOperationMappings();
					for (PermissionOperatoinMapping permissionOperatoinMapping : permissionOperationMappings) {
						Operation operation = permissionOperatoinMapping.getOperation();
						operations.add(operation.getId());
					}
				}else if(type==PermissionType.MENU.getValue()) {
					 List<PermissionMenuMapping> permissionMenuMappings = permisson.getPermissionMenuMappings();
					for (PermissionMenuMapping permissionMenuMapping : permissionMenuMappings) {
						 Menu menu = permissionMenuMapping.getMenu();
						menus.add(menu.getId());
					}
				}
			}
			
		}
		
		HashMap<String, List<Integer>> hashMap = new HashMap<>();
		hashMap.put("menus", menus);
		hashMap.put("operations", operations);
		
		return hashMap;
	}

	/**
	 * 获取子节点
	 * 
	 * @param id
	 *            父节点id
	 * @param allMenu
	 *            所有菜单列表
	 * @return 每个根节点下，所有子菜单列表
	 */
	public List<MenuVo> getChild(Integer id, List<Menu> allMenu,List<Integer> menu_permisssions) {
		List<MenuVo> childList = new ArrayList<MenuVo>();
		for (Menu nav : allMenu) {// 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
			if (nav.getParent() == id) {// 相等说明：为该根节点的子节点。
				childList.add(convertMenu(nav,menu_permisssions));
			}
		}
		// 递归
		for (MenuVo nav : childList) {
			if (StringUtils.isNotBlank(nav.getParent()))
				nav.setChildren(getChild(nav.getId(), allMenu,menu_permisssions));
		}
		// 如果节点下没有子节点，返回一个空List（递归退出）
		if (childList.size() == 0) {
			return new ArrayList<MenuVo>();
		}
		return childList;
	}

	public List<OperationDto> convertOperationTodtos(List<Operation> operations,List<Integer>operationIds) {

		ArrayList<OperationDto> dtos = new ArrayList<OperationDto>();
		for (Operation operation : operations) {
			OperationDto operationDto = ConvertDto.convertOperationToDto(operation);
			Boolean contains = containsIds(operationIds, operation.getId());
			operationDto.setChecked(contains);
			dtos.add(operationDto);

		}
		return dtos;
	}

	/**
	 * 获取操作树
	 */
	public OperationDto getOperationTree(List<OperationDto> all) {
		// 获取分类根节点
		if (all != null && all.size() > 1) {
			for (int i = 1; i < all.size(); i++) {
				for (int j = i - 1; j >= 0; j--) {
					if (all.get(j).getId().equals(all.get(i).getParent())) {
						all.get(j).getChildren().add(all.get(i));
					}
					if (all.get(i).getId().equals(all.get(j).getParent())) {
						all.get(i).getChildren().add(all.get(j));
					}
				}
			}
		}
		// 设置根节点父id
		if (all != null && all.size() > 0) {
			OperationDto tree = null;
			for (OperationDto d : all) {
				Integer parentTId = d.getParentTId();
				if (parentTId == null) {
					tree = d;
					break;
				}
			}
			if (tree != null) {
				//tree.setNocheck(true);
			}
			return tree;
		}
		return null;
	}
	public Boolean containsIds(List<Integer> allIds,Integer id) {
		if(allIds.contains(id)) {
			return true;
		}
		return false;
	}
	
	public MenuVo convertMenu(Menu menu,List<Integer> menu_operations) {
		MenuVo vo = new MenuVo();
		Boolean containsIds = containsIds(menu_operations, menu.getId());
		
		vo.setChecked(containsIds);
		vo.setId(menu.getId());
		vo.setName(menu.getName());
		if(null != menu.getParent())
			vo.setParent((String.valueOf(menu.getParent())));
		vo.setSequence(menu.getSequence());
//		vo.setUrl(menu.getUrl());  //解决角色分配菜单时，点击链接问题
		return vo;
	}
	
	@RequestMapping(value="updatePermisssions")
	@ResponseBody
	public Map<String,Object> updatePermisssions(@RequestBody PermissonRequestDto data){
		System.out.println(JsonConverter.format(data));
		List<Integer> roles = data.getRoles();
		List<Integer> menus = data.getMenus();
		List<Integer> operations = data.getOperations();
		for (Integer roleId : roles) {
			Role role = roleServiceImpl.get(roleId);
			role.getPermissionRoleMappings().clear();
			
			for (Integer operationId : operations) {
				Operation operation = operationService.get(operationId);
				List<PermissionOperatoinMapping> permissionOperationMappings = operation.getPermissionOperationMappings();
				if(permissionOperationMappings!=null&permissionOperationMappings.size()>0) {
					PermissionOperatoinMapping permissionOperatoinMapping = permissionOperationMappings.get(0);
					Permission permisson = permissionOperatoinMapping.getPermisson();
					
					RolePermissionMapping permissionRoleMapping = new RolePermissionMapping();
					permissionRoleMapping.setPermisson(permisson);
					permissionRoleMapping.setRole(role);
					
					role.getPermissionRoleMappings().add(permissionRoleMapping);
				}
			}
			
			for (Integer menuId : menus) {
				Menu menu = menuServiceImpl.get(menuId);
				 List<PermissionMenuMapping> permissionMenuMappings = menu.getPermissionMenuMappings();
				if(permissionMenuMappings!=null&permissionMenuMappings.size()>0) {
					PermissionMenuMapping permissionOperatoinMapping = permissionMenuMappings.get(0);
					Permission permisson = permissionOperatoinMapping.getPermisson();
					
					RolePermissionMapping permissionRoleMapping = new RolePermissionMapping();
					permissionRoleMapping.setPermisson(permisson);
					permissionRoleMapping.setRole(role);
					role.getPermissionRoleMappings().add(permissionRoleMapping);

					
				}
			}
			roleServiceImpl.update(role);
		}
		
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("statusCode", ResponseStatusCode.OK);
		return hashMap;
	}
}
