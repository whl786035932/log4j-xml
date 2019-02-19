/**
 * 
 */
package cn.videoworks.cms.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.videoworks.cms.enumeration.UserType;
import cn.videoworks.cms.vo.MenuVo;
import cn.videoworks.cms.vo.UserVo;

/**
 * 用于页面导航高亮需要的拦截器
 * 
 * @author lge
 * 
 */
public class RequestAnalyzerInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response,Object handler) throws Exception {	
		String[] params = request.getRequestURI().split("/");
		String base = request.getContextPath();
		if (params.length >= 2) {
			request.setAttribute("base", base);
		}
		HttpSession session = request.getSession();
		UserVo user = (UserVo) session.getAttribute("user");
		List<MenuVo> menus = new ArrayList<>();
		String servletPath = request.getServletPath();
		String contentType = request.getContentType();
//		System.out.println(contentType +";serveletPath="+servletPath);
		if(user!=null) {
			Integer type = user.getType()==null?UserType.UNSUPER.getValue():user.getType();
			if(type==UserType.SUPER.getValue()) {
				return true;
			}else if(contentType==null && !servletPath.equals("/index") && !servletPath.equals("/logout")&& !servletPath.equals("/classification/classificationManage") &&!servletPath.equals("/classification/arrangement")) {
					
					 menus = user.getMenus();
					 //获取请求的路径
					 if(servletPath!=null) {
						 if(servletPath.length()>1) {
							 servletPath = servletPath.substring(1);
						 }
						 String[] split = servletPath.split("/");
						 if(split.length>=1) {
							 servletPath = split[0];
						 }
						
					 }
					 boolean allowFlag = allowFlag(menus,servletPath);
					 
					 if(allowFlag) {
						 return  true;
					 }else {
						 response.sendRedirect(base+"/index");
						 return false;
					 }
				}
			
			
		}
		
		return true;
	}
	
	
	private boolean allowFlag(List<MenuVo> menus ,String servletPath) {
		
		 
		boolean allowFlag = false;
		 for (MenuVo menuVo : menus) {
			 String url = menuVo.getUrl();
			 System.out.println("url="+url+","+servletPath);
			 if(!url.equals("list")) {
				 
				 if(url.equals(servletPath) || url.contains(servletPath)) {
					 allowFlag = true;
					 return allowFlag;
				 }
				 List<MenuVo> children = menuVo.getChildren();
				 if(children.size()>0) {
					 MenuVo menuVo_child = menus.get(0);
					 List<MenuVo> children_childs = menuVo.getChildren();
					 allowFlag =  allowFlag(children_childs,servletPath);
				 }
			 }else {
				 allowFlag = true;
			 }
			 
		 }
		 
		 return allowFlag;
 }
	

}
