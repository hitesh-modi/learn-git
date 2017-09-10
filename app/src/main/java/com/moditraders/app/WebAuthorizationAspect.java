package com.moditraders.app;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class WebAuthorizationAspect {
	private static final Logger LOGGER = Logger.getLogger(WebAuthorizationAspect.class);
	 @Before("(@target(org.springframework.web.bind.annotation.RestController) || @target(org.springframework.stereotype.Controller)) && @annotation(requiresPermission)")
	    public void assertAuthorized(JoinPoint jp, RequiresPermissions requiresPermission) {
		 HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	        try {
	        	Subject currentUser = SecurityUtils.getSubject();
	        	LOGGER.info("Checking permission on method : " + jp.getSignature().getName() + " for user " + currentUser.getPrincipal());
	        	currentUser.checkPermissions(requiresPermission.value());
			} catch (AuthorizationException e) {
				LOGGER.error("User does not permission to view this page.");
				try {
					response.sendRedirect("/");
				} catch (IOException e1) {
					LOGGER.error("Error redirecting to home.");
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
	    }
}
