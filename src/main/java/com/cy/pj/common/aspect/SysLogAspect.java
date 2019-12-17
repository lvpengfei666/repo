package com.cy.pj.common.aspect;

import java.util.Arrays;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.util.IPUtils;
import com.cy.pj.common.util.ShiroUtils;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysLogService;

import io.micrometer.core.ipc.http.HttpSender.Method;
import lombok.extern.slf4j.Slf4j;
/**
 * @Aspect 描述的类为切面类，此类中实现：
 * 1)切入点(Pointcut)的定义
 * 2)通知(advice)的定义(扩展功能)
 */
@Slf4j//添加日志
@Aspect//切面类
@Component//交给spring管理
//@Order(1)//执行顺序
public class SysLogAspect {
	/**
	 * @Pointcut 注解用于描述或定义一个切入点
	 * 切入点的定义需要遵循spring中指定的表达式规范
	 * 例如:("bean(sysMenuServiceImpl)")为切入点表达式
	 * 的一种定义方式。
	 */
	 //bean(bean名称或一个表达式)
	
     //@Pointcut("bean(sysMenuServiceImpl)")//sysMenuServiceImpl是一个类,存储到spring中,首字母小写(注意)
	@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog)")
	 public void logPointCut() {}//此处什么都不写,起标识作用
    
    /**
     * @Around 注解描述的方法为一个环绕通知方法，
     * 在此方法中可以添加扩展业务逻辑，可以调用下一个切面对象或目标方法
     * @param jp 连接点(此连接点只应用@Around描述的方法)
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")//方法必须要加( )
    public Object aroundAdvice(ProceedingJoinPoint jp)
    throws Throwable{
   	 long start=System.currentTimeMillis();
   	 log.info("start:"+start);
   	 Object result=jp.proceed();//调用下一个切面或目标方法
   	 long end=System.currentTimeMillis();
   	 log.info("end:"+end);
   	 //保存日志(记录用户行为日志信息)
   	 saveLog(jp,(end-start));
   	 return result;
    }
    @Autowired
    private SysLogService sysLogService;
    //日志记录
    private  void saveLog(ProceedingJoinPoint jp,long time) throws Throwable {
    	//1.获取用户行为日志(ip,username,operation,method,params,time,createdTime)
    	//获取字节码对象,通过字节码对象获取方法信息
    	 Class<?> targetCls = jp.getTarget().getClass();//获取字节码对象
    	 String targetClsName = targetCls.getName();//获取类全名
    	 //获取方法签名(通过此签名获取目标方法信息)
    	 MethodSignature ms = ( MethodSignature)jp.getSignature();//方法签名(方法名,方法参数,返回值)
    	 //ms.getName();//当是cglib代理时可用这个方法,获得方法
    	 //获取目标方法上注解指定的操作名称
    	 java.lang.reflect.Method targetMethod = //这是通用的方式
    	targetCls.getDeclaredMethod(ms.getName(),ms.getParameterTypes());
    	 RequiredLog requiredLog =
    	 targetMethod.getAnnotation(RequiredLog.class);//拿到注解
    	 String operation = requiredLog.value();//拿到注解里的内容
    	 //System.out.println("mstype:"+ms.getDeclaringType().getClass());
    	 //System.out.println("method:"+ms.getMethod());
    	 //获取目标方法名(目标类型+方法名)
    	String targetObjectMethodName=targetClsName+"."+ms.getName();//类全名+方法名
    	//SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
    	//获取请求参数
    	String targetMjethodParams=Arrays.toString(jp.getArgs());
    	//System.out.println(ms.getClass());
    	//2.封装用户行为日志(SysLog)
    	SysLog entity = new SysLog();
    	entity.setIp(IPUtils.getIpAddr());//通过IPUtils类获得
    	entity.setUsername(ShiroUtils.getUsername());//通过登录用户获取,此处是写死的
    	entity.setOperation(operation);
    	entity.setMethod(targetObjectMethodName);
    	entity.setParams(targetMjethodParams);
    	entity.setTime(time);
    	entity.setCreatedTime(new Date());
    	//3.调用业务层对象方法 (saveObject)将日志写入到数据库
    	sysLogService.saveObject(entity);
//    	new Thread() {
//    		@Override
//    		public void run() {
//    			sysLogService.saveObject(entity);
//    			
//    		};
//    	}.start();//并发大了以后可能会对性能有影响,导致oom
    }
		
	
}
