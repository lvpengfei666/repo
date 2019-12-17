package com.cy.pj.sys.service;

import java.util.Map;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

public interface SysUserService {
	PageObject<SysUserDeptVo> findPageObjects(
			String username,
			Integer pageCurrent);
	int validById(Integer id,
			Integer valid,
			String modifiedUser);

	int saveObject(SysUser entity, Integer[] roleIds);
	
	Map<String, Object> findObjectById(Integer userId);
	
	int updateObject(SysUser entity,Integer[] roleIds);
	/**
	 * 修改密码
	 * @param password 原密码
	 * @param newPassword 新密码(还没加密)
	 * @param cfgPassword 确认密码
	 * @return
	 */

	int updatePassword(String password,
			           String newPassword,
			           String cfgPassword);
}
