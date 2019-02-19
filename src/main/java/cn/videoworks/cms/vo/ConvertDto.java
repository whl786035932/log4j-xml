package cn.videoworks.cms.vo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import cn.videoworks.cms.dto.OperationDto;
import cn.videoworks.cms.entity.Menu;
import cn.videoworks.cms.entity.Operation;
import cn.videoworks.cms.entity.PermissionMenuMapping;
import cn.videoworks.cms.entity.PermissionOperatoinMapping;
import cn.videoworks.cms.entity.Role;
import cn.videoworks.cms.entity.RolePermissionMapping;
import cn.videoworks.cms.entity.User;
import cn.videoworks.cms.entity.UserRoleMapping;
import cn.videoworks.cms.enumeration.UserType;
import cn.videoworks.cms.util.DateUtil;

public class ConvertDto {
	
	public static Menu convertMenu(MenuVo menuVo) {
		Menu menu = new Menu();
		menu.setName(menuVo.getName());
		menu.setSequence(menuVo.getSequence());
		menu.setUrl(menuVo.getUrl());
		if(StringUtils.isNotBlank(menuVo.getParent()))
			menu.setParent(Integer.valueOf(menuVo.getParent()));
		menu.setInserted_at(DateUtil.getNowTime());
		menu.setUpdated_at(DateUtil.getNowTime());
		menu.setMenu_class(menuVo.getMenuClass());
		return menu;
	}
	
	public static MenuVo convertMenu(Menu menu) {
		MenuVo vo = new MenuVo();
		vo.setId(menu.getId());
		vo.setName(menu.getName());
		if(null != menu.getParent())
			vo.setParent((String.valueOf(menu.getParent())));
		vo.setSequence(menu.getSequence());
		vo.setUrl(menu.getUrl());
		vo.setMenuClass(menu.getMenu_class());
		return vo;
	}
	
	public static void convertMenu(MenuVo menuVo,Menu menu) {
		menu.setName(menuVo.getName());
		menu.setUpdated_at(DateUtil.getNowTime());
		menu.setUrl(menuVo.getUrl());
		menu.setMenu_class(menuVo.getMenuClass());
	}
	
	public static UserVo convertUser(User user,List<Menu> list) {
		UserVo userVo = new UserVo();
		userVo.setId(user.getId());
		userVo.setUsername(user.getUsername());
		userVo.setType(user.getType());
		userVo.setLast_login_time(user.getLast_login_time());
		List<Menu> menus = new ArrayList<Menu>();
		 if(null != user.getType() && user.getType() == UserType.SUPER.getValue()) {
			 menus = list;
		 }else {
			 menus = getMenu(user);
		 }
		//根节点
	      List<MenuVo> rootMenu = new ArrayList<MenuVo>();
	      List<Integer> rootId = new ArrayList<Integer>();
	      
	      for (Menu menu : menus) {
	          if(null == menu.getParent() || menu.getParent() == 0){//父节点是0的，为根节点。
	        	  if(!rootId.contains(menu.getId())) {
		            rootMenu.add(ConvertDto.convertMenu(menu));
		            rootId.add(menu.getId());
	        	  }
	          }
	        }
	      
	    //为根菜单设置子菜单，getClild是递归调用的
	      for (MenuVo nav : rootMenu) {
		        /* 获取根节点下的所有子节点 使用getChild方法*/
		        List<MenuVo> childList = getChild(nav.getId(), menus);
		        nav.setChildren(childList);//给根节点设置子节点
	      }
		
		userVo.setMenus(rootMenu);
		
		List<OperationDto> operations = getOperations(user);
		userVo.setOperations(operations);
		return userVo;
	}
	
	/**
	 * getMenu:(获取菜单)
	 * @author   meishen
	 * @Date	 2018	2018年9月25日		下午2:22:44 
	 * @return List<Menu>    
	 * @throws
	 */
	public static List<Menu> getMenu(User user){
		List<Menu> menus = new ArrayList<Menu>();
		Set<UserRoleMapping> userRoleMappings = user.getUserRoleMappings();
		for(UserRoleMapping urm : userRoleMappings) {
			Set<RolePermissionMapping> permissionRoleMappings = urm.getRole().getPermissionRoleMappings();
			for(RolePermissionMapping rpm : permissionRoleMappings) {
				List<PermissionMenuMapping> permissionMenuMappings = rpm.getPermisson().getPermissionMenuMappings();
				for(PermissionMenuMapping pmm : permissionMenuMappings) {
					if(null != pmm.getMenu()) {
						menus.add(pmm.getMenu());
					}
				}
			}
		}
		
		return menus;
	}
	
	public static List<OperationDto> getOperations(User user){
		List<OperationDto> operations = new ArrayList<OperationDto>();
		Set<UserRoleMapping> userRoleMappings = user.getUserRoleMappings();
		Iterator<UserRoleMapping> iterator = userRoleMappings.iterator();
		while (iterator.hasNext()) {
			UserRoleMapping next = iterator.next();
			Role role = next.getRole();
			Set<RolePermissionMapping> permissionRoleMappings = role.getPermissionRoleMappings();
			Iterator<RolePermissionMapping> iterator2 = permissionRoleMappings.iterator();
			while (iterator2.hasNext()) {
				RolePermissionMapping next2 = iterator2.next();
				List<PermissionOperatoinMapping> permissionOperationMappings = next2.getPermisson().getPermissionOperationMappings();
				
				for (PermissionOperatoinMapping permissionOperatoinMapping : permissionOperationMappings) {
					Operation operation = permissionOperatoinMapping.getOperation();
					OperationDto convertOperationToDto = cn.videoworks.cms.dto.ConvertDto.convertOperationToDto(operation);
					operations.add(convertOperationToDto);
				}
			}
		}
		return operations;
		
	}
	/**
	   * 获取子节点
	   * @param id 父节点id
	   * @param allMenu 所有菜单列表
	   * @return 每个根节点下，所有子菜单列表
	   */
	  public static List<MenuVo> getChild(Integer id,List<Menu> allMenu){
		    List<MenuVo> childList = new ArrayList<MenuVo>();
		    List<Integer> childIds = new ArrayList<Integer>();
		    for (Menu nav : allMenu) {// 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
			    if(nav.getParent() == id){//相等说明：为该根节点的子节点。
			    	if(!childIds.contains(nav.getId())) {
				        childList.add(ConvertDto.convertMenu(nav));
				        childIds.add(nav.getId());
			    	}
			      }
		    }
		    //递归
		    for (MenuVo nav : childList) {
		    	if(StringUtils.isNotBlank(nav.getParent()))
		    		nav.setChildren(getChild(nav.getId(), allMenu));
		    }
		    //如果节点下没有子节点，返回一个空List（递归退出）
		    if(childList.size() == 0){
		      return new ArrayList<MenuVo>();
		    }
		    return childList;
	  }
}
