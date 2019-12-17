package com.cy.pj.sys.service.impl;

import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.service.SysMenuService;
import com.fasterxml.jackson.core.sym.Name;

import io.micrometer.core.instrument.util.StringUtils;
@Transactional(timeout = 30,rollbackFor = Throwable.class,isolation = Isolation.READ_COMMITTED)//(readonly=false,默认的值,存在并发问题)
@Service//必须读取别人已提交的数据isolation = Isolation.READ_COMMITTED
public class SysMenuServiceImpl implements SysMenuService{
	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@RequiredLog("查询菜单")//日志切面
	@Transactional(readOnly = true)//事务切面
	@Override
	public List<Map<String, Object>> findObjects() {
		//获取线程名称
		String tName = Thread.currentThread().getName();
		System.out.println("method.query.thread.name="+tName);
		List<Map<String,Object>> list=
			sysMenuDao.findObjects();
		if(list==null||list.size()==0)
		throw new ServiceException("没有对应的菜单信息");
		return list;

}
	/**
	 * @Transactional默认是方法出现运行时异常,就要回滚
	 */
	//默认@Transactional事务优先级较高
	@RequiredLog("查询菜单")          //所有操作是串行Isolation.SERIALIZABLE
	@Transactional(readOnly=true,isolation = Isolation.SERIALIZABLE)//(允许并发读, 不能并发写)
	@Override
	public int deleteObject(Integer id) {
		//1.验证数据的合法性
				if(id==null||id<=0)
				throw new IllegalArgumentException("请先选择");
				//2.基于id进行子元素查询
				int count=sysMenuDao.getChildCount(id);
				if(count>0)
				throw new ServiceException("请先删除子菜单");
				//3.删除菜单元素
				int rows=sysMenuDao.deleteObject(id);
				if(rows==0)
				throw new ServiceException("此菜单可能已经不存在");
				//4.删除角色,菜单关系数据
				sysRoleMenuDao.deleteObjectsByMenuId(id);
				//5.返回结果
				return rows;

	}
	@Override
	public List<Node> findZtreeMenuNodes() {
		return sysMenuDao.findZtreeMenuNodes();
	}
	@Override
	public int saveObject(SysMenu entity) {
		        //1.合法验证
				if(entity==null)
				throw new ServiceException("保存对象不能为空");
				if(StringUtils.isEmpty(entity.getName()))
				throw new ServiceException("菜单名不能为空");
				int rows;
				//2.保存数据
				try{
				rows=sysMenuDao.insertObject(entity);
				}catch(Exception e){
				e.printStackTrace();
				throw new ServiceException("保存失败");
				}
				//3.返回数据
				return rows;

	}
	@Override
	public int updateObject(SysMenu entity) {
		        //1.合法验证
				if(entity==null)
				throw new ServiceException("保存对象不能为空");
				if(StringUtils.isEmpty(entity.getName()))
				throw new ServiceException("菜单名不能为空");
				
				//2.更新数据
				int rows=sysMenuDao.updateObject(entity);
				if(rows==0)
				throw new ServiceException("记录可能已经不存在");
				//3.返回数据
				return rows;
		}

		
	}
