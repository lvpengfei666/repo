package com.cy.pj.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@Order(1)//针对多个切面类,数字越小,优先级越高
public class SysTimeAspect {
	 //1.粗粒度的切入点表达式(只能精确到类)
	  //1)bean表达式(掌握)
	  @Pointcut("bean(sysMenuServiceImpl)")
	  //@Pointcut("bean(*ServiceImpl)")
	  //2)within表达式(了解)
	  //@Pointcut("within(com.cy.pj.sys.service.impl.*)")//这个包中所有的类
	  //2.细粒度的切入点表达式(可以精确到具体方法)
	  //1)execution表达式(了解)
	  //@Pointcut("execution(int com.cy.pj.sys.service.impl.SysRoleServiceImpl.saveObject(com.cy.pj.sys.entity.SysRole,..))")
	  //2)@annotation表达式(掌握)
	  //@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredTime)")//和角色role对应
	  public void doTimePointCut() {}
	  
	  @Before("doTimePointCut()")
	  public void beforeAdvice() {
		  log.info("time:beforeAdvice");
	  }
	  @After("doTimePointCut()")
	  public void afterAdvice() {
		  log.info("time:afterAdvice");
	  }
	  @AfterReturning("doTimePointCut()")
	  public void afterReturningAdvice() {
		  log.info("time:AfterReturningAdvice");
	  }
	  @AfterThrowing("doTimePointCut()")
	  public void afterThrowingAdvice() {
		  log.info("time:afterThrowingAdvice");
	  }
	  @Around("doTimePointCut()")
	  public Object aroundAdvice(ProceedingJoinPoint jp)
	  throws Throwable{
		  log.info("time:aroundAdvice.before");
		  Object result=jp.proceed();
		  log.info("time:aroundAdvice.after");
		  return result;
	  }
}
