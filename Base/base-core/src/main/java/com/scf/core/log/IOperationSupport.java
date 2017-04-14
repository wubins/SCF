package com.scf.core.log;

/**
 * 日志支持接口，需要业务系统去实现
 * @author wubin
 *
 */
public interface IOperationSupport {
 /**
  * 得到当前系统
  * @return
  */
  String getCurrentSystem();
  /**
   * 得到当前操作人员
   * @return
   */
  String getCurrentOperator();
  
  
  /**
   * 得到备注，可以加上一些特殊信息
   * @return
   */
  String getRemark();
}
