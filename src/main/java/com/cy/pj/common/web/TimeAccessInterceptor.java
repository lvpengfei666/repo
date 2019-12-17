package com.cy.pj.common.web;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import com.cy.pj.common.exception.ServiceException;
/**
 * 自定义拦截器功能
  @author Administrator
 */
public class TimeAccessInterceptor 
       implements HandlerInterceptor {//用之前先配置一下
    /**
          *  此方法在handler对象方法执行之前执行，
             方法的返回值决定了目标handler的方法是否执行。
     */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("preHandle");
		//获取日历对象
		Calendar c=Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY,6);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		//获取允许访问的开始时间
		long start=c.getTimeInMillis();
		//获取允许访问的结束时间
		c.set(Calendar.HOUR_OF_DAY,24);
		long end=c.getTimeInMillis();
		//设置访问规则
		long cTime=System.currentTimeMillis();
		if(cTime<start||cTime>end)
			throw new ServiceException("此时间段不可访问");
		//业务实现
		return true;//false表示拦截,true表示放行
	}
}




