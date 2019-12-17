package com.cy.pj.sys.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
//@Mapper
public interface SysMenuService {
	List<Map<String,Object>> findObjects();
	int deleteObject(Integer id);
	List<Node> findZtreeMenuNodes();
	int saveObject(SysMenu entity);
	int updateObject(SysMenu entity);
	
}
