package com.cy.pj.common.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
@Setter
@Slf4j
@Configuration
@ConfigurationProperties("async-thread-pool")
public class SpringAsyncConfig implements AsyncConfigurer {
	private int corePoolSize=2;
	private int maxPoolSize=5;
	private int keepAliveSeconds=60*5;//5分钟
	private int queueCapacity=5;
	//使用SimpleAsyncTaskExecutor对象
	//特点:
	//1)每次请求都会创建新的线程
	//2)可以对并发请求进行限流操作(原理：底层进行阻塞)
	//	@Override
	//	public Executor getAsyncExecutor() {
	//		SimpleAsyncTaskExecutor taskExecutor=new SimpleAsyncTaskExecutor();
	//		System.out.println("getAsyncExecutor()");
	//		taskExecutor.setConcurrencyLimit(100);
	//		return taskExecutor;
	//	}
	/**
	 * 核心线程用完,创建5个线程放在队列里,队列满了之后,还能最多创建3线程,最多总共为:2+5+3=10个线程
	 */
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor taskExecutor=
				new ThreadPoolTaskExecutor();
		//设置核心线程数
		taskExecutor.setCorePoolSize(corePoolSize);
		//设置最大线程数
		taskExecutor.setMaxPoolSize(maxPoolSize);
		//设置线程空闲时间
		taskExecutor.setKeepAliveSeconds(keepAliveSeconds);//超出核心线程数的要释放,不能一直存在池中
		//设置队列容量(LinkedBlockingQueue)
		taskExecutor.setQueueCapacity(queueCapacity);
		//设置线程对象名字前缀(非常有必要)
		taskExecutor.setThreadNamePrefix("db-service-thread-");
	    //设置拒绝处理的策略(当池无法处理新的任务时，该执行什么操作)
			//在这里默认选择调用者线程(例如tomcat线程)进行处理。
			taskExecutor.setRejectedExecutionHandler(
			new ThreadPoolExecutor.CallerRunsPolicy());
//			taskExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
//				@Override
//				public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//				    log.warn("没有足够的线程处理当前任务");
//				}
//			});
		//进行池的初始化
		taskExecutor.initialize();
		return taskExecutor;
	}
	
	/**异步任务执行过程中的错误处理*/
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return  new AsyncUncaughtExceptionHandler() {
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				log.error("执行异步任务时出现了未知的错误",ex);
			}
		};
	}
}
