package com.moditraders.app;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WebAuthorizationAspect {
	private static final Logger LOGGER = Logger.getLogger(WebAuthorizationAspect.class);
	 @Before("@target(org.springframework.web.bind.annotation.RestController) && @annotation(requiresPermission)")
	    public void assertAuthorized(JoinPoint jp, RequiresPermissions requiresPermission) {
		 LOGGER.info("Checking Permission on method : " + jp.getSignature().getName()); 
	        SecurityUtils.getSubject().checkPermissions(requiresPermission.value());
	    }
}
