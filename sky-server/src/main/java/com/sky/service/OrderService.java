package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;

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
   * @param ordersPaymentDTO 订单支付信息
   * @return orderPaymentVO 订单支付结果
   */
  OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

  /**
   * 支付成功，修改订单状态
   * @param outTradeNo 支付订单号
   */
  void paySuccess(String outTradeNo);
}
