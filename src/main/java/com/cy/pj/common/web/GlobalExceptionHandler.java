package com.cy.pj.common.web;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.pj.common.vo.JsonResult;
/**
 * 当控制层对象出现异常后：
 * 1)检测控制层对象内部有没有@ExceptionHandler描述异常处理方法
 * 2)检测Spring容器中是否有对象使用了@ControllerAdvice注解修饰
 * 假如有，则使用类中的@ExceptionHandler描述的异常处理方法，对
 * 特定异常进行处理。
 * @author Administrator
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	/**
	 * @ExceptionHandler 描述的方法为异常处理方法
	 * 其中注解内部指定的异常类型，为此方法可处理的异常
	 * @param e
	 * @return
	 */
	//JDK中的自带的日志API
		@ExceptionHandler(RuntimeException.class)//异常的标识
	    @ResponseBody
		public JsonResult doHandleRuntimeException(
				RuntimeException e){
	    	e.printStackTrace();//也可以写日志
			return new JsonResult(e);//封装异常信息
		}

		
		@ExceptionHandler(ShiroException.class)
		@ResponseBody
		public JsonResult doHandleShiroException(
				ShiroException e) {
			JsonResult r=new JsonResult();
			r.setState(0);//进入这里说明出异常了,设置为0
			if(e instanceof UnknownAccountException) {
				r.setMessage("账户不存在");
			}else if(e instanceof LockedAccountException) {
				r.setMessage("账户已被禁用");
			}else if(e instanceof IncorrectCredentialsException) {
				r.setMessage("密码不正确");
			}else if(e instanceof AuthorizationException) {
				r.setMessage("没有此操作权限");
			}else {
				r.setMessage("系统维护中");
			}
			e.printStackTrace();
			return r;
		}


}

