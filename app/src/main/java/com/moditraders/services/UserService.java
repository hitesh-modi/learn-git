package com.moditraders.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import com.moditraders.entities.State;
import com.moditraders.entities.User;
import com.moditraders.entities.UserRole;
import com.moditraders.exceptions.UserWithEmailExistsException;
import com.moditraders.exceptions.UserWithMobileExistsException;
import com.moditraders.models.AuthInfo;
import com.moditraders.models.StateModel;
import com.moditraders.models.UserModel;
import com.moditraders.repositories.StateRepository;
import com.moditraders.repositories.UserRepository;
import com.moditraders.repositories.UserRoleRepository;
import com.moditraders.utility.Util;

@Component("userService")
public class UserService implements IUserService {

	private static final Logger LOGGER = Logger.getLogger(UserService.class);
	
	@Resource(name="stateRepo")
	private StateRepository stateRepo;
	
	@Resource(name="userRepo")
	private UserRepository userRepo;
	
	@Resource(name="userRoleRepo")
	private UserRoleRepository userRoleRepo;
	
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

	@Override
	public void registerUser(UserModel userToRegister) throws UserWithEmailExistsException, UserWithMobileExistsException{
		
		LOGGER.info("Checking if user with email id or phone number already exists.");
		User existingUserByEmail = userRepo.checkUserExistenceByEmail(userToRegister.getEmail());
		if(existingUserByEmail != null) {
			throw new UserWithEmailExistsException("User already exists with email id");
		}
		
		User existingUserByContact = userRepo.checkUserExistenceByContactNumber(userToRegister.getContactNumber());
		if(existingUserByContact != null) {
			throw new UserWithMobileExistsException("User already exists with contact");
		}
		
		LOGGER.info("Registering user : "+ userToRegister);
		User user = new User();
		user.setAddress(userToRegister.getAddress());
		user.setBusinessname(userToRegister.getFirmName());
		user.setFirstname(userToRegister.getFname());
		user.setMiddlename(userToRegister.getMname());
		user.setLastname(userToRegister.getLname());
		user.setGstin(userToRegister.getGstin());
		user.setContactnumber(userToRegister.getContactNumber());
		user.setState(userToRegister.getState().getStatename());
		user.setUsername(userToRegister.getEmail());
		user.setPassword(Util.getDecodedPassword(userToRegister.getPassword()));
		userRepo.save(user);
		
		List<UserRole> roles = new ArrayList<>();
		UserRole role = new UserRole();
		role.setRoleName("normalUser");
		role.setUsername(userToRegister.getEmail());
		roles.add(role);
		user.setUserRoles(roles);
		
		userRoleRepo.save(role);
		
		LOGGER.info("User registered successfully");
	}
	
}
