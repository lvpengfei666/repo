package com.cy.pj.common.config;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
/**
 * @Configuration 注解描述的类为一个配置对象,
   * 此对象也会交给spring管理
 */
@Configuration //bean
public class SpringShiroConfig {
	//Step01：在SpringShiroConfig添加会话管理器配置
	 public DefaultWebSessionManager newSessionManager() {
		 DefaultWebSessionManager sManager=
				 new DefaultWebSessionManager();
		 sManager.setGlobalSessionTimeout(20*60*1000);
		 return sManager;
	 }
	 


	
	public SimpleCookie newCookie() {
		 SimpleCookie c=new SimpleCookie("rememberMe");
		 c.setMaxAge(10*60);
		 return c;
}
	//@Bean
	public CookieRememberMeManager newRememberMeManager() {
	CookieRememberMeManager cManager=
	    new CookieRememberMeManager();
	    cManager.setCookie(newCookie());
			 return cManager;
		 }


	
	 /**
	     * 配置shiro框架中的缓存管理器，借助Shiro框架中
	     * 内置缓存对象对用户权限信息进行cache操作。
	  * @return
	  */
	 //在SpringShiroConfig中配置缓存Bean对象(Shiro框架提供)
	 @Bean
	 public CacheManager newCacheManager(){
	 	 return new MemoryConstrainedCacheManager();
	 	}



		 @Bean("shiroFilterFactory")//默认key为方法名,现在起名为shiroFilterFactory
		 public ShiroFilterFactoryBean newShiroFilterFactoryBean(
		 			 @Autowired SecurityManager securityManager) {
			    //构建bean对象,通过此对象创建过滤器工厂
		 		 ShiroFilterFactoryBean sfBean=
		 		 new ShiroFilterFactoryBean();
		 		 //注入SecurityManager
		 		 sfBean.setSecurityManager(securityManager);
		 		 //设置登录url
		 		 sfBean.setLoginUrl("/doLoginUI");
		 		 //定义map指定请求过滤规则(哪些资源允许匿名访问,哪些必须认证访问)
		 		 LinkedHashMap<String,String> map=
		 				 new LinkedHashMap<>();
		 		 //静态资源允许匿名访问:"anon"
		 		 map.put("/bower_components/**","anon");
		 		 map.put("/build/**","anon");//**指的是该目录以及子目录下
		 		 map.put("/dist/**","anon");
		 		 map.put("/plugins/**","anon");
		 		map.put("/user/doLogin","anon");
		 		map.put("/doLogout","logout");
		 		 //除了匿名访问的资源,其它都要认证("authc")后访问
		 		//map.put("/**","authc");
		 		map.put("/**","user");
		 		 sfBean.setFilterChainDefinitionMap(map);
		 		 return sfBean;
		 	 }
		 @Bean
		 public SecurityManager newSecurityManager(@Autowired Realm realm,
				 @Autowired CacheManager cacheManager) {
		 		 DefaultWebSecurityManager sManager=
		 		 new DefaultWebSecurityManager();
		 		 sManager.setRealm(realm);
		 		sManager.setCacheManager(cacheManager);
		 		sManager.setRememberMeManager(newRememberMeManager());
		 		sManager.setSessionManager(newSessionManager());
		 		 return sManager;
		 }
		 //=========授权配置===================
		 //配置shiro框架中一些bean对象的生命周期管理器
		 @Bean("lifecycleBeanPostProcessor")//默认bean的名字为方法名
		 public LifecycleBeanPostProcessor
		     newLifecycleBeanPostProcessor() {
			 return new LifecycleBeanPostProcessor();
		 }
		 /**
		  * 此对象会在Spirng容器启动时,扫描所有的Advisor(通知)对象
		  * ,基于Advisor对象中的切入点(pointcut)的描述,进行代理对象的创建
		  * @return
		  */
		 //配置代理对象创建器,通过此对象为目标业务对象创建代理对象
		 @DependsOn("lifecycleBeanPostProcessor")//生命周期由它管理
		 @Bean
		 public DefaultAdvisorAutoProxyCreator newDefaultAdvisorAutoProxyCreator() {
				 return new DefaultAdvisorAutoProxyCreator();
		}
		 /**
		  * 配置Advisor对象，在此对象中定义切入点以及要在此切入点,进行实现功能功能扩展(advice)
		  * @param securityManager
		  * @return
		  */
		 //配置advisor对象,shiro框架底层会通过此对象的matchs方法返回值决定是否创建代理对象,进行权限控制.
		 @Bean
		 public AuthorizationAttributeSourceAdvisor 
		 newAuthorizationAttributeSourceAdvisor(
		 	    		    @Autowired SecurityManager securityManager) {
		 		        AuthorizationAttributeSourceAdvisor advisor=
		 				new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		 		 return advisor;
		 }
		
 

}
