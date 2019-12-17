package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserRoleDao {
	
	  int deleteObjectsByRoleId(Integer roleId);
	  /**
	   * 负责将用户与角色的关系数据写入到数据库
	   * @param uerId 用户id
	   * @param roleIds 多个角色id
	   * @return
	   */
	 int insertObjects(@Param("userId")Integer uerId,
			          @Param("roleIds")Integer[] roleIds);
	 /**
	      *  根据用户id查询角色ID方法
	  * @param id
	  * @return
	  */
	 List<Integer> findRoleIdsByUserId(Integer id);
	 /**
	  * 根据userId删除关系数据
	  * @param userId
	  * @return
	  */
	 int deleteObjectsByUserId(Integer userId);
	 

}
