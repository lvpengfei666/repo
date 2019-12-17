package com.cy.pj.common.vo;

import java.io.Serializable;

import lombok.Data;
@Data
public class Node implements Serializable{
	
	private Integer id;
	private String name;
	private Integer parentId;

}
