/**
 * 
 */
package cn.videoworks.cms.initalizer;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.videoworks.cms.entity.User;
import cn.videoworks.cms.enumeration.Status;
import cn.videoworks.cms.enumeration.UserType;
import cn.videoworks.cms.service.UserService;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.util.Md5Util;

/**
 * 初始化媒资配置map
 * 
 * @author lge
 */
public class InitializerUserConfig {
	
	@Autowired
	private UserService sysUserService;
	
	
	@Resource
	private Properties databaseConfig;

	private static final Logger log = LoggerFactory.getLogger(InitializerUserConfig.class);

	@PostConstruct
	private void initialize() {
		User user = sysUserService.getUser("admin", 1);
		if(null == user) {
			User sysUser = new  User();
			sysUser.setUsername("admin");
			sysUser.setNickname("超级管理员");
			sysUser.setPassword(Md5Util.getMD5("Videoworks123#@!"));
			sysUser.setStatus(Status.VALID.getValue());
			Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
			sysUser.setInserted_at(nowTimeStamp);
			sysUser.setUpdated_at(nowTimeStamp);
			sysUser.setType(UserType.SUPER.getValue());
			sysUserService.save(sysUser);
			log.info("初始化超级管理员【admin】");
			
		}
	}
	
	
	

}
