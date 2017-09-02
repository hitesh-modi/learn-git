package com.moditraders.controller;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moditraders.models.AuthInfo;
import com.moditraders.models.AuthResponse;
import com.moditraders.models.StateModel;
import com.moditraders.models.UserModel;
import com.moditraders.services.IUserService;

@Controller("userController")
public class UserController {
	
	private static final Logger LOGGER = Logger.getLogger(UserController.class);
	
	@Resource(name="userService")
	private IUserService userService;
	
	@RequestMapping("/")
	public String home() {
		return "index";
	}
	
	@PostMapping("/login")
	public @ResponseBody AuthResponse login(@Valid @RequestBody String data) {
		LOGGER.info("Login method called with data : " + data);
		String returnStatus = "failure";
		try {
			AuthInfo authData = new ObjectMapper().readValue(data, AuthInfo.class);
			boolean loginSuccess = userService.login(authData);
			if(loginSuccess == true) 
				returnStatus = "success";
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new AuthResponse(returnStatus);
	}
	
	@RequestMapping(value="/getUserName")
	@RequiresPermissions("dashboard")
	public @ResponseBody UserModel getUserName() {
		return userService.getUserInfo();
	}
	
	@RequestMapping(value="/getStates")
	public @ResponseBody Collection<StateModel> getStates() {
		return userService.getStates();
	}
	
	@RequestMapping(value="/greeting")
	@RequiresPermissions("dashboard")
	public String hello() {
		return "greeting";
	}
	
}
