package com.cy.pj.sys.controller;
//访问路径:http://localhost/role/doFindPageObjects?pageCurrent=1
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;

@Controller
@RequestMapping("/role/")
public class SysRoleController {
	 @Autowired
	  private SysRoleService sysRoleService;
	 
	  @RequestMapping("doFindPageObjects")
	  @ResponseBody
	  public JsonResult doFindPageObjects(
			  String name,Integer pageCurrent){
		  PageObject<SysRole> pageObject=
		  sysRoleService.findPageObjects(name,pageCurrent);
		  return new JsonResult(pageObject);
	  }
	  
	  @RequestMapping("doDeleteObject")
	  @ResponseBody
	  public JsonResult doDeleteObject(Integer id){
	   sysRoleService.deleteObject(id);
	  return new JsonResult("delete Ok");
	  }
	  @RequestMapping("doSaveObject")
	  @ResponseBody
	  public JsonResult doSaveObject(
	      		SysRole entity,Integer[] menuIds){
	      	sysRoleService.saveObject(entity,menuIds);
	  return new JsonResult("save ok");    
	  }
	  
	  @RequestMapping("doFindObjectById")
	  @ResponseBody
	   public JsonResult doFindObjectById(Integer id){
	      	return new JsonResult(sysRoleService.findObjectById(id));
	   }

	  @RequestMapping("doUpdateObject")
	  @ResponseBody
	  public JsonResult doUpdateObject(SysRole entity,
	  Integer[] menuIds){
	  		  sysRoleService.updateObject(entity,menuIds);
	   return new JsonResult("update ok");
	  	 
	  }
	  @RequestMapping("doFindRoles")
	  @ResponseBody
	  public JsonResult doFindObjects() {
		  return new JsonResult(sysRoleService.findObjects());
	  }
}
