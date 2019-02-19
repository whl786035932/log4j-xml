package cn.videoworks.cms.util;

import javax.servlet.http.HttpServletRequest;

import cn.videoworks.cms.vo.UserVo;

public class UserUtil {
	
	private static UserUtil userUtil = null;
	
	private String username;
	
	private UserUtil(HttpServletRequest request) {
		try {
			UserVo user = (UserVo)request.getSession().getAttribute("user"); 
			String userName = user != null?user.getUsername():"未知";
			this.username = userName;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private UserUtil(String userName) {
			this.username = userName;
	}
	
	public static synchronized UserUtil getUserUtil(HttpServletRequest request) {
		if(null == userUtil) {
			userUtil = new UserUtil(request);
		}
		return userUtil;
	}
	
	public static synchronized UserUtil getUserUtil(String userName) {
		if(null == userUtil) {
			userUtil = new UserUtil(userName);
		}
		return userUtil;
	}
	
	public static void clearUserUtil() {
		userUtil = null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
}
