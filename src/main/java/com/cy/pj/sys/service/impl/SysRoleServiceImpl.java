package com.cy.pj.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.common.annotation.RequiredTime;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.vo.SysRoleMenuVo;

import io.micrometer.core.instrument.util.StringUtils;
@Service
public class SysRoleServiceImpl implements SysRoleService{
	 @Autowired
     private SysRoleDao sysRoleDao;
	 @Autowired
	 private SysRoleMenuDao sysRoleMenuDao;
	 @Autowired
	 private SysUserRoleDao sysUserRoleDao;

	@Override
	public PageObject<SysRole> findPageObjects(String name, Integer pageCurrent) {
		//1.验证参数合法性
		  //1.1验证pageCurrent的合法性，
		  //不合法抛出IllegalArgumentException异常
		  if(pageCurrent==null||pageCurrent<1)
		  throw new IllegalArgumentException("当前页码不正确");
		  //2.基于条件查询总记录数
		  //2.1) 执行查询
		  int rowCount=sysRoleDao.getRowCount(name);
		  //2.2) 验证查询结果，假如结果为0不再执行如下操作
		  if(rowCount==0)
          throw new ServiceException("记录不存在");
		  //3.基于条件查询当前页记录(pageSize定义为2)
		  //3.1)定义pageSize
		  int pageSize=2;
		  //3.2)计算startIndex
		  int startIndex=(pageCurrent-1)*pageSize;
		  //3.3)执行当前数据的查询操作
		  List<SysRole> records=
		  sysRoleDao.findPageObjects(name, startIndex, pageSize);
		  //4.对分页信息以及当前页记录进行封装
		  //4.1)构建PageObject对象
		  PageObject<SysRole> pageObject=new PageObject<>();
		  //4.2)封装数据
		  pageObject.setPageCurrent(pageCurrent);
		  pageObject.setPageSize(pageSize);
		  pageObject.setRowCount(rowCount);
		  pageObject.setRecords(records);
        pageObject.setPageCount((rowCount-1)/pageSize+1);
		  //5.返回封装结果。
		  return pageObject;

	}

	@Override
	public int deleteObject(Integer id) {
		//1.验证参数的合法性
				if(id==null||id<1)
				throw new ServiceException("id的值不正确,id="+id);
				//2.执行dao操作
		sysRoleMenuDao.deleteObjectsByMenuId(id);
		sysUserRoleDao.deleteObjectsByRoleId(id);
				int rows=sysRoleDao.deleteObject(id);
				if(rows==0)
				throw new ServiceException("数据可能已经不存在");
				//3.返回结果
				return rows;

	}
	@RequiredTime
	@Override
	 public int saveObject(SysRole entity,Integer[] menuIds) {
	    	//1.合法性验证
	    	if(entity==null)
	    throw new ServiceException("保存数据不能为空");
	    	if(StringUtils.isEmpty(entity.getName()))
	    	throw new ServiceException("角色名不能为空");
	   	if(menuIds==null||menuIds.length==0)
	    	throw new ServiceException("必须为角色赋予权限");
	    	//2.保存数据
	    	int rows=sysRoleDao.insertObject(entity);
	    	sysRoleMenuDao.insertObjects(
	    			entity.getId(),menuIds);
	    	//3.返回结果
	    	return rows;
	 }
	  @Override
	    public SysRoleMenuVo findObjectById(Integer id) {
	    	//1.合法性验证
	    	if(id==null||id<=0)
	    	throw new ServiceException("id的值不合法");
	    	//2.执行查询
	    	SysRoleMenuVo result=sysRoleDao.findObjectById(id);
	  	//3.验证结果并返回
	    	if(result==null)
	    	throw new ServiceException("此记录已经不存在");
	    	return result;
	    }

	  @Override
	    public int updateObject(SysRole entity,Integer[] menuIds) {
	    	//1.合法性验证
	    	if(entity==null)
	    throw new ServiceException("更新的对象不能为空");
	    	if(entity.getId()==null)
	    	throw new ServiceException("id的值不能为空");
	    	
	    	if(StringUtils.isEmpty(entity.getName()))
	    	throw new ServiceException("角色名不能为空");
	    	if(menuIds==null||menuIds.length==0)
	    	throw new ServiceException("必须为角色指定一个权限");
	    	
	    	//2.更新数据
	    	int rows=sysRoleDao.updateObject(entity);
	    	if(rows==0)
	    throw new ServiceException("对象可能已经不存在");
	    	sysRoleMenuDao.deleteObjectsByRoleId(entity.getId());
	    	sysRoleMenuDao.insertObjects(entity.getId(),menuIds);
	 
	    	//3.返回结果
	    	return rows;
	    }

	@Override
	public List<CheckBox> findObjects() {
		 
		return sysRoleDao.findObjects();
	}


}
