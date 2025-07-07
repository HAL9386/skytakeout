package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
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
}
