package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderOverViewVO;

public interface WorkspaceService {
  /**
   * 获取营业数据
   *
   * @return BusinessDataVO
   */
  BusinessDataVO getBusinessData();

  /**
   * 获取订单概览
   *
   * @return OrderOverViewVO
   */
  OrderOverViewVO getOrderOverView();
}
