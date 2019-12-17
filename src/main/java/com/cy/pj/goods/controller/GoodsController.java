package com.cy.pj.goods.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods/")
public class GoodsController {
	 @RequestMapping("doFindGoods")
	 public List<Map<String,Object>> doFindGoods(){
		 Map<String,Object> g1=new HashMap<>();
		 g1.put("id", 100);
		 g1.put("name", "spring boot");
		 Map<String,Object> g2=new HashMap<>();
		 g2.put("id", 200);
		 g2.put("name", "spring framework");
		 List<Map<String,Object>> list=new ArrayList<>();
		 list.add(g1);
		 list.add(g2);
		 return list;
	 }
}
