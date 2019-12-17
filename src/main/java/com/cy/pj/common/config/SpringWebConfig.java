package com.cy.pj.common.config;
//由于技术的更新,这一步的配置不要也可以
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cy.pj.common.web.TimeAccessInterceptor;

@Configuration 
public class SpringWebConfig implements WebMvcConfigurer{//取代web.xml中filter配置
	/**
	 * 注册过滤器对象DelegatingFilterProxy,
	 * 此对象由spring框架提供,核心作用就是通过它基于web请求加载指定的Bean对象
	 * @return
	 */
	//注册filter对象
	@SuppressWarnings({ "rawtypes", "unchecked" })//这个可以不要
	@Bean
	public FilterRegistrationBean  newFilterRegistrationBean() {
		//1.构建过滤器的注册器对象
		FilterRegistrationBean fBean=
	    new FilterRegistrationBean();
		//2.注册过滤器对象
		DelegatingFilterProxy filter=
		new DelegatingFilterProxy("shiroFilterFactory"); 
		fBean.setFilter(filter);
		//3.进行过滤器配置
		//配置过滤器的生命周期管理(可选)由ServletContext对象负责
		//fBean.setEnabled(true);//默认值就是true
		fBean.addUrlPatterns("/*");//意思是拦截所有请求
		//....
		return fBean;
	}
	
	/**
	  * 注册拦截器并制定拦截规则
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//注册拦截器
		registry.addInterceptor(
		new TimeAccessInterceptor())
		//设置要拦截的资源
		.addPathPatterns("/user/doLogin");
	}
}
