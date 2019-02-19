package cn.videoworks.cms.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.entity.Menu;
import cn.videoworks.cms.entity.Permission;
import cn.videoworks.cms.entity.PermissionMenuMapping;
import cn.videoworks.cms.enumeration.PermissionType;
import cn.videoworks.cms.service.MenuService;
import cn.videoworks.cms.service.PermissionService;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.cms.vo.ConvertDto;
import cn.videoworks.cms.vo.MenuVo;
import cn.videoworks.commons.util.json.JsonConverter;
import cn.videoworks.commons.webdev.constant.ResponseStatusCode;

@Controller
@RequestMapping(value="menus")
public class MenuController {
	
	@Resource
	private MenuService menuServiceImpl;
	
	@Resource
	private PermissionService permissionServiceImpl;
	
	@RequestMapping(value="")
	public String list(ModelMap map) {
		List<Menu> menus = menuServiceImpl.list("sequence");
		
		//根节点
	      List<MenuVo> rootMenu = new ArrayList<MenuVo>();
	      
	      for (Menu menu : menus) {
	          if(null == menu.getParent() || menu.getParent() == 0){//父节点是0的，为根节点。
	            rootMenu.add(ConvertDto.convertMenu(menu));
	          }
	        }
	      
	    //为根菜单设置子菜单，getClild是递归调用的
	      for (MenuVo nav : rootMenu) {
		        /* 获取根节点下的所有子节点 使用getChild方法*/
		        List<MenuVo> childList = ConvertDto.getChild(nav.getId(), menus);
		        nav.setChildren(childList);//给根节点设置子节点
	      }
		
		map.put("menus", JsonConverter.format(rootMenu));
		return  "site.cms.menu.index";
	}
	
	/**
	 * save:(添加菜单)
	 * @author   meishen
	 * @Date	 2018	2018年9月18日		下午3:24:35 
	 * @return String    
	 * @throws
	 */
	@RequestMapping(value="",method = RequestMethod.POST)
	@ResponseBody
	public RestResponse save(MenuVo menuVo) {
		RestResponse res = new RestResponse();
		Menu menu =ConvertDto.convertMenu(menuVo);
		menuServiceImpl.save(menu);
		
		//添加权限表
		Permission permission = new Permission();
		permission.setType(PermissionType.MENU.getValue());
		permissionServiceImpl.save(permission);
		
		//添加权限-菜单关联表
		List<PermissionMenuMapping> pms = new ArrayList<PermissionMenuMapping>();
		PermissionMenuMapping pm = new PermissionMenuMapping(permission,menu);
		pms.add(pm);
		permission.setPermissionMenuMappings(pms);
		permissionServiceImpl.update(permission);
		
		res.setData(JsonConverter.asMap(JsonConverter.format(menu), String.class, Object.class));
		res.setStatusCode(ResponseStatusCode.OK);
		return  res;
	}
	
	/**
	 * update:(修改菜单)
	 * @author   meishen
	 * @Date	 2018	2018年9月19日		下午4:50:09 
	 * @return RestResponse    
	 * @throws
	 */
	@RequestMapping(value="/update/{id}",method = RequestMethod.POST)
	@ResponseBody
	public RestResponse update(MenuVo menuVo,@PathVariable int id) {
		RestResponse res = new RestResponse();
		Menu menu =menuServiceImpl.get(id);
		if(null != menu) {
			ConvertDto.convertMenu(menuVo, menu);
			menuServiceImpl.update(menu);
		}
		res.setStatusCode(ResponseStatusCode.OK);
		return  res;
	}
	
	/**
	 * delete:(删除菜单)
	 * @author   meishen
	 * @Date	 2018	2018年9月20日		下午2:50:02 
	 * @return RestResponse    
	 * @throws
	 */
	@RequestMapping(value="delete/{id}",method = RequestMethod.PUT)
	@ResponseBody
	public RestResponse delete(@PathVariable int id) {
		RestResponse res = new RestResponse();
		Menu menu = menuServiceImpl.get(id);
		List<PermissionMenuMapping> pms = menu.getPermissionMenuMappings();
		for(PermissionMenuMapping pm : pms) {
			Permission permission = pm.getPermisson();
			if(null != permission)
				permissionServiceImpl.delete(permission);
		}
		menu.getPermissionMenuMappings().clear();
		menuServiceImpl.delete(id);
		res.setStatusCode(ResponseStatusCode.OK);
		return  res;
	}
	
	@RequestMapping(value="order/{pId}",method = RequestMethod.PUT)
	@ResponseBody
	public RestResponse order(@RequestBody List<Integer> ids,@PathVariable int pId) {
		RestResponse res = new RestResponse();
		if(null != ids && ids.size() >0) {
			for(int i=0;i < ids.size();i++) {
				Menu menu = menuServiceImpl.get(ids.get(i));
				menu.setSequence(i+1);
				menuServiceImpl.update(menu);
			}
		}
		
		res.setStatusCode(ResponseStatusCode.OK);
		return  res;
	}
	
}
