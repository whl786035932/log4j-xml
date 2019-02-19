package cn.videoworks.cms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.videoworks.cms.dao.MenuDao;
import cn.videoworks.cms.entity.Menu;
import cn.videoworks.cms.service.MenuService;

/**
 * @author   meishen
 * @Date	 2018	2018年9月18日		上午9:58:31
 * @Description 方法描述: 菜单控制实体类
 */
@Service
public class MenuServiceImpl  implements MenuService{

	@Resource
	private MenuDao dao;

	@Override
	public void save(Menu menu) {
		dao.save(menu);
	}

	@Override
	public void update(Menu menu) {
		dao.update(menu);
	}

	@Override
	public void delete(int menuId) {
		dao.delete(menuId);
	}

	@Override
	public List<Menu> list() {
		return dao.list();
	}

	@Override
	public List<Menu> list(String order) {
		return dao.list(order);
	}

	@Override
	public List<Menu> listByPid(int pId, String order) {
		return dao.listByPid(pId, order);
	}

	@Override
	public Menu get(int id) {
		return dao.get(id);
	}

}
