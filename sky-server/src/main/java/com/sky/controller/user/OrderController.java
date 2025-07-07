package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
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

  /**
   * C端查询订单历史记录
   *
   * @param ordersPageQueryDTO 订单查询信息
   * @return orderVOList 订单历史记录
   */
  @ApiOperation("C端查询订单历史记录")
  @GetMapping("/historyOrders")
  public Result<PageResult<OrderVO>> list(OrdersPageQueryDTO ordersPageQueryDTO) {
    ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
    log.info("查询订单历史记录：{}", ordersPageQueryDTO);
    PageResult<OrderVO> pageResult = orderService.pageQuery(ordersPageQueryDTO);
    return Result.success(pageResult);
  }

  /**
   * C端查询订单详情
   *
   * @param id 订单id
   * @return orderVO 订单详情
   */
  @ApiOperation("C端查询订单详情")
  @GetMapping("orderDetail/{id}")
  public Result<OrderVO> getById(@PathVariable Long id) {
    log.info("根据订单id查询订单详情：{}", id);
    OrderVO orderVO = orderService.getById(id);
    return Result.success(orderVO);
  }

  /**
   * C端取消订单
   *
   * @param id 订单id
   * @return 取消订单结果
   */
  @ApiOperation("C端取消订单")
  @PutMapping("/cancel/{id}")
  public Result<Object> cancel(@PathVariable Long id) {
    log.info("取消订单：{}", id);
    orderService.cancel(id);
    return Result.success();
  }

  /**
   * C端再来一单
   *
   * @param id 订单id
   * @return 再来一单结果
   */
  @ApiOperation("C端再来一单")
  @PostMapping("/repetition/{id}")
  public Result<Object> repetition(@PathVariable Long id) {
    log.info("再来一单：{}", id);
    orderService.repetition(id);
    return Result.success();
  }
}
