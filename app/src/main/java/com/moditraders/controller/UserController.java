package com.moditraders.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("userController")
public class UserController {
	
	@RequestMapping("/")
	public String home() {
		return "index";
	}
	
	@RequestMapping("/login")
	public String login() {
		return null;
	}
	
	@GetMapping(value="/greeting")
	@RequiresPermissions("dashboard")
	public String hello() {
		return "greeting";
	}
	
}
