package cn.videoworks.cms.service;

import java.util.List;

import cn.videoworks.cms.entity.Menu;

/**
 * @author   meishen
 * @Date	 2018	2018年9月18日		上午9:51:43
 * @Description 方法描述: 菜单service接口类
 */
public interface MenuService {

	void save(Menu menu);
	void update(Menu menu);
	void delete(int menuId);
	Menu get(int id);
	List<Menu> list();
	List<Menu> list(String order);
	List<Menu> listByPid(int pId,String order);
}
