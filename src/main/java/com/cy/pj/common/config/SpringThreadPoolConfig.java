package com.cy.pj.common.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Setter;

@Configuration
public class SpringThreadPoolConfig {
	@Value("${async-thread-pool.corePoolSize}")
	private int corePoolSize=2;
	@Value("${async-thread-pool.maxPoolSize}")
	private int maxPoolSize=5;
	@Value("${async-thread-pool.keepAliveSeconds}")
	private int keepAliveSeconds=60*5;
	@Value("${async-thread-pool.queueCapacity}")
	private int queueCapacity=5;
	
	/**创建线程工厂对象：目的是创建线程时为线程起个名字*/
	private ThreadFactory threadFactory=new ThreadFactory() {
		private AtomicLong count=new AtomicLong(1);//CAS算法
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r,"db-service-thread-name-"+count.getAndIncrement());
		}
	};
	
	@Bean
	public Executor asyncExecutor() {
		System.out.println("corePoolSize="+corePoolSize);
		//构建阻塞式队列
		BlockingQueue<Runnable> workQueue=
		new LinkedBlockingDeque<>(queueCapacity);
		//构建线程池对象(tomcat中默认用的线程池也是这个类型)
		ThreadPoolExecutor executor=
				new ThreadPoolExecutor(corePoolSize,
						maxPoolSize, 
						keepAliveSeconds, 
						TimeUnit.SECONDS,
						workQueue,
						threadFactory,
						new ThreadPoolExecutor.CallerRunsPolicy());
		return executor;
	}
}
