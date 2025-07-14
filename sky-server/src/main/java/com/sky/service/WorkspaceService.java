package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDate;

public interface WorkspaceService {
  /**
   * 获取营业数据
   *
   * @return BusinessDataVO
   */
  BusinessDataVO getBusinessData(LocalDate begin, LocalDate end);

  /**
   * 获取订单概览
   *
   * @return OrderOverViewVO
   */
  OrderOverViewVO getOrderOverView();

  /**
   * 获取菜品概览
   *
   * @return DishOverViewVO
   */
  DishOverViewVO getDishOverView();

  /**
   * 获取套餐概览
   *
   * @return SetmealOverViewVO
   */
  SetmealOverViewVO getSetmealOverView();
}
