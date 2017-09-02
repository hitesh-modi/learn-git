package com.moditraders.services;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import com.moditraders.models.AuthInfo;
import com.moditraders.models.UserModel;

@Component("userService")
public class UserService implements IUserService {

	private static final Logger LOGGER = Logger.getLogger(UserService.class);
	
	@Override
	public boolean login(AuthInfo authInfo) {
		boolean loginSuccess = false;
		LOGGER.info("Login service called for user : ");
		try {
			Subject currentUser = SecurityUtils.getSubject();
			
			if(!currentUser.isAuthenticated()) {
				LOGGER.info("User " + authInfo.getUser() + " is not authenticated hence logging the user into the system.");
				UsernamePasswordToken token = new UsernamePasswordToken(authInfo.getUser(), authInfo.getPassword());
				currentUser.login(token);
				loginSuccess = true;
				LOGGER.info("User " + authInfo.getUser() + " logged in successfully.");
			}
		} catch (AuthenticationException e) {
			LOGGER.error("Error while loging in the user " + authInfo.getUser());
			loginSuccess = false;
		}
		
		return loginSuccess;
	}
	
	@Override
	public UserModel getUserInfo() {
		String userid = (String)SecurityUtils.getSubject().getPrincipal();
		UserModel userModel = new UserModel();
		userModel.setUserid(userid);
		userModel.setUserName("Test User");
		return userModel;
	}

}