//package com.cy.pj.common.vo;
////此对象的最核心的作用是为业务层对象德的执行的返回结果添加状态信息
//import java.io.Serializable;
//
//public class JsonResult implements Serializable{
//	
//	private static final long serialVersionUID = 1L;
//	/**状态码*/
//	private int state=1;//1表示SUCCESS,0表示ERROR
//	/**状态信息*/
//	private String message="ok";
//	/**正确数据*/
//	private Object data;
//	public JsonResult() {}
//	public JsonResult(String message){
//		this.message=message;
//	}
//	/**一般查询时调用，封装查询结果*/
//	public JsonResult(Object data) {
//		this.data=data;
//	}
//	/**出现异常时时调用*/
//	public JsonResult(Throwable t){
//		this.state=0;
//		this.message=t.getMessage();
//	}
//	public int getState() {
//		return state;
//	}
//	public void setState(int state) {
//		this.state = state;
//	}
//	public String getMessage() {
//		return message;
//	}
//	public void setMessage(String message) {
//		this.message = message;
//	}
//	public Object getData() {
//		return data;
//	}
//	public void setData(Object data) {
//		this.data = data;
//	}
//
//}
package com.cy.pj.common.vo;
import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
/**通过此对象封装服务端要返回给客户端的数据，
 * 此对象最核心的作用是为业务层对象的执行结果
 * 添加状态信息*/
@Data
@NoArgsConstructor
public class JsonResult implements Serializable {
	private static final long serialVersionUID = -5766977494287555486L;
	/**状态码*/
	private int state=1;//1表示SUCCESS,0表示ERROR
	/**状态信息*/
	private String message="ok";
	/**正确数据*/
	private Object data;
	public JsonResult(String message){
		this.message=message;
	}
	public JsonResult(Object data){
		this.data=data;
	}
	public JsonResult(Throwable e){
		this.message=e.getMessage();
		this.state=0;
	}
}

