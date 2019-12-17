package com.cy.pj.sys.service.impl;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysLogDao;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SysLogServiceImpl implements SysLogService{
	 @Autowired
     private SysLogDao sysLogDao;
//	  @Override
//	  public PageObject<SysLog> findPageObjects(
//			  String name, Integer pageCurrent) {
//		  //1.验证参数合法性
//		  //1.1验证pageCurrent的合法性，
//		  //不合法抛出IllegalArgumentException异常
//		  if(pageCurrent==null||pageCurrent<1)
//		  throw new IllegalArgumentException("当前页码不正确");
//		  //2.基于条件查询总记录数
//		  //2.1) 执行查询
//		  int rowCount=sysLogDao.getRowCount(name);
//		  //2.2) 验证查询结果，假如结果为0不再执行如下操作
//		  if(rowCount==0)
//         throw new ServiceException("系统没有查到对应记录");
//		  //3.基于条件查询当前页记录(pageSize定义为5)
//		  //3.1)定义pageSize
//		  int pageSize=5;
//		  //3.2)计算startIndex
//		  int startIndex=(pageCurrent-1)*pageSize;
//		  //3.3)执行当前数据的查询操作
//		  List<SysLog> records=
//		  sysLogDao.findPageObjects(name, startIndex, pageSize);
//		  //4.对分页信息以及当前页记录进行封装
//		  //4.1)构建PageObject对象
//		  PageObject<SysLog> pageObject=new PageObject<>();
//		  //4.2)封装数据
//		  pageObject.setPageCurrent(pageCurrent);
//		  pageObject.setPageSize(pageSize);
//		  pageObject.setRowCount(rowCount);
//		  pageObject.setRecords(records);
//         pageObject.setPageCount((rowCount-1)/pageSize+1);
//		  //5.返回封装结果。
//		  return pageObject;
//		 // return new PageObject<>(pageCurrent, pageSize, rowCount, records);
//	  }
	 @RequiresPermissions("sys:user:valid")//授权标识
	@Override
	public int deleteObjects(Integer... ids) {
		 //1.参数校验
		if (ids==null||ids.length==0) 
			throw new IllegalArgumentException("参数值无效");
		//2.执行删除操作
		int rows=sysLogDao.deleteObjects(ids);
		if (rows==0)
			throw new ServiceException("记录可能已经不存在了");
		//3.返回结果
			return rows;
			


	}
	
	@Override
	public PageObject<SysLog> findPageObjects(
			String username,Integer pageCurrent) {
		//1.对参数进行校验 
		if(pageCurrent==null||pageCurrent<1)
		throw new IllegalArgumentException("当前页码值无效");
		//2.查询总记录数并进行校验
		int rowCount=sysLogDao.getRowCount(username);
		if(rowCount==0)
		throw new ServiceException("没有找到对应记录");
		//3.查询当前页记录
		int pageSize=5;
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysLog> records=
		sysLogDao.findPageObjects(username,
				startIndex, pageSize);
		//4.对查询结果进行封装并返回
		return new PageObject<>(pageCurrent, pageSize, rowCount, records);
	}
	
	//查菜单和写日志是在两个事务Propagation.REQUIRES_NEW,让它运行在一个独立的事务中
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Async//表示用异步方式写日志,默认池对象ThreadPoolTaskExecutor
	@Override
	public void saveObject(SysLog entity) {
		String tName = Thread.currentThread().getName();
		System.out.println("log.save.thread.name:"+tName);
		  sysLogDao.insertObject(entity);
//		try {
//			Thread.sleep(10000);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
	}

}
