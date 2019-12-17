package com.cy.pj.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserDeptVo;

import io.micrometer.core.instrument.util.StringUtils;
@Service
public class SysUserServiceImpl implements SysUserService{
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	public int updatePassword(String password,
			String newPassword,String cfgPassword) {
		//1.参数检验
		//1.1非空验证
		if (StringUtils.isEmpty(password)) 
		throw new IllegalArgumentException("原密码不能为空");
		if(StringUtils.isEmpty(newPassword))
		throw new IllegalArgumentException("新密码不能为空");
		if(!newPassword.equals(cfgPassword))
		throw new IllegalArgumentException("两次密码输入必须一致");
		//1.2原密码正确性验证
		SysUser user=(SysUser)SecurityUtils.getSubject().getPrincipal();
		SimpleHash sh=
		    	new SimpleHash("MD5",password,user.getSalt(),1);
		if(!user.getPassword().equals(sh.toHex()))
			throw new ServiceException("原密码不正确");	
		//1.3新密码与原密码不能相同
		sh=new SimpleHash("MD5", newPassword, user.getSalt(), 1);
		if (user.getPassword().equals(sh.toHex())) 
		throw new ServiceException("新密码不能与原密码相同");
		//2.更新密码
		String salt= UUID.randomUUID().toString();
		sh=new SimpleHash("MD5", newPassword, salt, 1);
		int rows = sysUserDao.updatePassword(sh.toHex(), salt, user.getId());
		if (rows==0) {
			throw new ServiceException("记录可能已经不存在了");
		}
			
	
		return rows;
	}
	@Override
	public PageObject<SysUserDeptVo> findPageObjects(String username, Integer pageCurrent) {
		//1.数据合法性验证
				if(pageCurrent==null||pageCurrent<=0)
				throw new ServiceException("参数不合法");
		//2.依据条件获取总记录数
				int rowCount=sysUserDao.getRowCount(username);
		        if(rowCount==0)
				throw new ServiceException("记录不存在");
				//3.计算startIndex的值
				int pageSize=3;
				int startIndex=(pageCurrent-1)*pageSize;
				//4.依据条件获取当前页数据
				List<SysUserDeptVo> records=sysUserDao.findPageObjects(
				username, startIndex, pageSize);
				//5.封装数据
				PageObject<SysUserDeptVo> pageObject=new PageObject<>();
				pageObject.setPageCurrent(pageCurrent);
				pageObject.setRowCount(rowCount);
				pageObject.setPageSize(pageSize);
				pageObject.setRecords(records);
		pageObject.setPageCount((rowCount-1)/pageSize+1);
				return pageObject;

	}
	/**
	 *@RequiresPermissions 告诉shiro框架访问方法时需要sys:user:valid权限
	 */
	@RequiresPermissions("sys:user:valid")
	@RequiredLog("禁用启用")//操作名称,如果没有写就是"operation",具体查看@RequiredLog
	@Override
	public int validById(Integer id,
    Integer valid,
    String modifiedUser) {
		//1.合法性验证
		if(id==null||id<=0)
		throw new ServiceException("参数不合法,id="+id);
		if(valid!=1&&valid!=0)
		throw new ServiceException("参数不合法,valie="+valid);
		if(StringUtils.isEmpty(modifiedUser))
		throw new ServiceException("修改用户不能为空");
		//2.执行禁用或启用操作
		int rows=0;
		try{
	    rows=sysUserDao.validById(valid,id, modifiedUser);
	    System.out.println(rows);
		}catch(Throwable e){
		e.printStackTrace();
		//报警,给维护人员发短信
		throw new ServiceException("底层正在维护");
		}
		//3.判定结果,并返回
		if(rows==0)
		throw new ServiceException("此记录可能已经不存在");
		return rows;
	}

	@Override
	public int saveObject(SysUser entity, Integer[] roleIds) {
		 //1.验证数据合法性
		if (entity==null) 
		throw new ServiceException("保存对象不能为空");
		if (StringUtils.isEmpty(entity.getUsername())) {
			throw new ServiceException("用户名不能为空");
		}
		if (StringUtils.isEmpty(entity.getPassword())) {
			throw new ServiceException("密码不能为空");
		}
		if (roleIds==null||roleIds.length==0) {
			throw new ServiceException("至少要为用户分配角色");
		}
		//2.将数据写入数据库
		String salt = UUID.randomUUID().toString();
		entity.setSalt(salt);
		//加密(先了解,讲shiro时再说)
		SimpleHash sHash = 
				new SimpleHash("MD5",//算法
						entity.getPassword(),//输入的密码
						salt, 1);//盐值和加密次数
		entity.setPassword(sHash.toHex());
		int rows = sysUserDao.insertObject(entity);
		sysUserRoleDao.insertObjects(entity.getId(),roleIds);
		//返回结果
		return rows;
	}
/**
 * @cacheable描述业务方法时,表示方法的返回值要存储到cache中,默认key为实际参数的组合,值为方法的返回值.
 * 其属性说明如下:
 * (1)value属性:用于指定cache对象名称
 */
	@Cacheable(value = "userCache")//缓存的名称userCache,底层默认使用的是ConcurrentHashMap
	@Override
	public Map<String, Object> findObjectById(Integer userId) {//key是参数的组合id
		System.out.println("findObjectById");
			//1.合法性验证
			if(userId==null||userId<=0)
			throw new ServiceException(
			"参数数据不合法,userId="+userId);
			//2.业务查询
			SysUserDeptVo user=
			sysUserDao.findObjectById(userId);
			if(user==null)
			throw new ServiceException("此用户已经不存在");
			List<Integer> roleIds=
			sysUserRoleDao.findRoleIdsByUserId(userId);
			//3.数据封装
			Map<String,Object> map=new HashMap<>();
			map.put("user", user);
			map.put("roleIds", roleIds);
			return map;
		}
	/**
	 * @CachePut方法的返回值要和查询时的方法的返回值类型保持一致,此处为map才可以
	 */
	//Spring中的EL表达式#entity.id
	//@CachePut(value = "userCache",key = "#entity.id")//返回类型为int不能用这种方式
	@CacheEvict(value ="userCache",allEntries = true,key = "#entity.id",beforeInvocation = true)
	@RequiredLog("修改用户")
	@Override
	public int updateObject(SysUser entity,Integer[] roleIds) {
		//1.参数有效性验证
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
			throw new IllegalArgumentException("用户名不能为空");
		if(roleIds==null||roleIds.length==0)
			throw new IllegalArgumentException("必须为其指定角色");
		//其它验证自己实现，例如用户名已经存在，密码长度，...
		//2.更新用户自身信息
		int rows=sysUserDao.updateObject(entity);
		//3.保存用户与角色关系数据
		sysUserRoleDao.deleteObjectsByUserId(entity.getId());
		sysUserRoleDao.insertObjects(entity.getId(),
				roleIds);
		//4.返回结果
		return rows;
	}	


}
