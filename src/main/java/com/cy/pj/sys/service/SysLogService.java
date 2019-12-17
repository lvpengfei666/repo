package com.cy.pj.sys.service;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;

public interface SysLogService {
	/**
	 * 写用户行为日志到数据库
	 * @param entity
	 * @return
	 */
	void saveObject(SysLog entity);
	   /**
               * 通过此方法实现分页查询操作
     * @param name 基于条件查询时的参数名
     * @param pageCurrent 当前的页码值
     * @return 当前页记录+分页信息
     */
	 PageObject<SysLog> findPageObjects(
			 String username,
			 Integer pageCurrent);
	 
	 /**
		 * 基于id执行删除操作
		 * @param ids
		 * @return
		 */ 
	 int deleteObjects(Integer...ids);

}
