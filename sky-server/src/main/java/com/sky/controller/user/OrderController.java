package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = "C端订单相关接口")
@Slf4j
@RequestMapping("/user/order")
@RestController("userOrderController")
public class OrderController {
  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  /**
   * C端订单提交
   *
   * @param ordersSubmitDTO 订单提交信息
   * @return orderSubmitVO 订单提交结果
   */
  @ApiOperation("C端订单提交")
  @PostMapping("/submit")
  public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
    log.info("C端订单提交：{}", ordersSubmitDTO);
    OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
    return Result.success(orderSubmitVO);
  }

  /**
   * 订单支付
   *
   * @param ordersPaymentDTO 订单支付信息
   * @return orderPaymentVO 订单支付结果
   */
  @PutMapping("/payment")
  @ApiOperation("订单支付")
  public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
    log.info("订单支付：{}", ordersPaymentDTO);
    OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
    log.info("生成预支付交易单：{}", orderPaymentVO);
    return Result.success(orderPaymentVO);
  }
}
