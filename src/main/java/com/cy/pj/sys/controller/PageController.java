package com.cy.pj.sys.controller;

//http://localhost/log/doFindPageObjects?pageCurrent=1
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class PageController {
	@RequestMapping("doIndexUI")
	public String doIndexUI() {
		return "starter";
	}

//	@RequestMapping("log/log_list")
//	public String doLogUI() {
//		return "sys/log_list";
//	}

	@RequestMapping("doPageUI")
	public String doPageUI() {
		return "common/page";
	}

	
	 //通用一种形式
	 
//	 @RequestMapping("{moudle}/{moudleUI}") 
//	 public String doMenuUI(@PathVariable String moduleUI) {
//	 
//	 return "sys/"+moduleUI; 
//	 }
	@RequestMapping("{moudle}/{moduleUI}")
	 public String doModuleUI(@PathVariable String moduleUI) {
		 return "sys/"+moduleUI;
	 }
	@RequestMapping("doLoginUI")
	public String doLoginUI(){
			return "login";
	}


}
