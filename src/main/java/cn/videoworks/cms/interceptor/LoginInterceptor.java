package cn.videoworks.cms.interceptor;

import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.videoworks.cms.vo.UserVo;

public class LoginInterceptor extends HandlerInterceptorAdapter{
	
	@Resource
	private Properties databaseConfig;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		UserVo user = (UserVo)request.getSession().getAttribute("user"); 
		if(null==user){
			String base = request.getContextPath();
			response.sendRedirect(base+"/login");
			return false;
		}
		String version = databaseConfig.getProperty("cms.version");
		request.setAttribute("version", version);
		return super.preHandle(request, response, handler);
	}
}
