package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
  /**
   * C端订单提交
   *
   * @param ordersSubmitDTO 订单提交信息
   * @return orderSubmitVO 订单提交结果
   */
  OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

  /**
   * 订单支付
   *
   * @param ordersPaymentDTO 订单支付信息
   * @return orderPaymentVO 订单支付结果
   */
  OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

  /**
   * 支付成功，修改订单状态
   *
   * @param outTradeNo 支付订单号
   */
  void paySuccess(String outTradeNo);

  /**
   * 订单查询
   *
   * @param ordersPageQueryDTO 订单查询信息
   * @return PageResult<OrderVO> 订单查询结果
   */
  PageResult<OrderVO> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

  /**
   * 根据id查询订单
   *
   * @param id 订单id
   * @return OrderVO 订单信息
   */
  OrderVO getById(Long id);

  /**
   * 取消订单
   *
   * @param id 订单id
   */
  void cancel(Long id);

  /**
   * 再来一单
   *
   * @param id 订单id
   */
  void repetition(Long id);

  /**
   * 查询订单统计信息
   *
   * @return OrderStatisticsVO 订单统计信息
   */
  OrderStatisticsVO statistics();

  /**
   * 订单确认接单
   *
   * @param ordersConfirmDTO 订单确认信息
   */
  void confirm(OrdersConfirmDTO ordersConfirmDTO);

  /**
   * 订单拒单
   *
   * @param ordersRejectionDTO 订单拒单信息
   */
  void rejection(OrdersRejectionDTO ordersRejectionDTO);

  /**
   * 管理端取消订单
   *
   * @param ordersCancelDTO 订单取消信息
   */
  void adminCancel(OrdersCancelDTO ordersCancelDTO);

  /**
   * 管理端订单派送
   *
   * @param id 订单id
   */
  void delivery(Long id);

  /**
   * 管理端完成订单
   *
   * @param id 订单id
   */
  void complete(Long id);

  /**
   * 催单
   *
   * @param id 订单id
   */
  void reminder(Long id);
}
