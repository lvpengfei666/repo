package com.cy.pj.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
@Mapper
public interface SysMenuDao {
	List<Map<String,Object>> findObjects();
	/**
	  * 根据菜单id统计子菜单的个数
	  * @param id
	  * @return
	  */
	 int getChildCount(Integer id);
	 /**
	  * 根据id 删除菜单
	  * @param id
	  * @return
	  */
	 int deleteObject(Integer id);
	 List<Node> findZtreeMenuNodes();
	 int insertObject(SysMenu entity);
	 int updateObject(SysMenu entity);
	 
	 int deleteObjectsByRoleId(Integer roleId);
	 /**
	  * 在SysMenuDao中基于菜单id查找权限标识信息
	  * @param menuIds
	  * @return
	  */
	 List<String> findPermissions(
				@Param("menuIds")
				Integer[] menuIds);


}
