package com.moditraders.services;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import com.moditraders.entities.State;
import com.moditraders.models.AuthInfo;
import com.moditraders.models.StateModel;
import com.moditraders.models.UserModel;
import com.moditraders.repositories.StateRepository;

@Component("userService")
public class UserService implements IUserService {

	private static final Logger LOGGER = Logger.getLogger(UserService.class);
	
	@Resource(name="stateRepo")
	private StateRepository stateRepo;
	
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
	
	@Override
	public Collection<StateModel> getStates() {
		Iterable<State> states = stateRepo.findAll();
		LOGGER.info("Getting states from database");
		Collection<StateModel> stateList = new ArrayList<>();
		for (State state : states) {
			StateModel stateModel = new StateModel();
			stateModel.setStatecode(state.getStatecode());
			stateModel.setStatename(state.getStatename());
			stateList.add(stateModel);
		}
		LOGGER.info("Got " + stateList.size() + " states from DB.");
		return stateList;
	}

}
