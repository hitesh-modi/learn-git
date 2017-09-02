package com.moditraders.app;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.PropertiesRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Configuration
public class ShiroConfig {
	
	private static final Logger LOGGER = Logger.getLogger(ShiroConfig.class);
	
	@Autowired
	private DataSource datasource;
	
	@Bean
	public Realm realm() {
		// uses 'classpath:shiro-users.properties' by default
		/*PropertiesRealm realm = new PropertiesRealm();
		// Caching isn't needed in this example, but we can still turn it on
		realm.setCachingEnabled(true);
		realm.init();*/
		
		JdbcRealm jdbcRealm = new JdbcRealm();
		jdbcRealm.setDataSource(datasource);
		jdbcRealm.setPermissionsLookupEnabled(true);
		jdbcRealm.init();
		return jdbcRealm;
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter() {
	    ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
	    shiroFilter.setLoginUrl("/login");
	    shiroFilter.setSuccessUrl("/index");
	    shiroFilter.setUnauthorizedUrl("/forbidden");
	    Map<String, String> filterChainDefinitionMapping = new HashMap<String, String>();
	    filterChainDefinitionMapping.put("/services/**", "authcBasic");
	    shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);
	    shiroFilter.setSecurityManager(securityManager());
	    /*Map<String, Filter> filters = new HashMap<String, Filter>();
	    filters.put("authc", new FormAuthenticationFilter());
	    filters.put("logout", new LogoutFilter());
	    filters.put("roles", new RolesAuthorizationFilter());
	    filters.put("user", new UserFilter());
	    shiroFilter.setFilters(filters);*/
	    return shiroFilter;
	}
	
	@Bean(name = "securityManager")
	public SecurityManager securityManager() {
	    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
	    securityManager.setRealm(realm());
	    return securityManager;
	}

	@Bean
	public CacheManager cacheManager() {
		return new MemoryConstrainedCacheManager();
	}

	@ExceptionHandler(UnauthenticatedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public void handleException(UnauthenticatedException e) {
		LOGGER.debug("{} was thrown", e);
	}
	
}
