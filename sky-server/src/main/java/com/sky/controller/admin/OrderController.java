package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "订单相关接口")
@RequestMapping("/admin/order")
@RestController("adminOrderController")
public class OrderController {
  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  /**
   * 条件分页查询订单
   *
   * @param ordersPageQueryDTO 分页查询条件
   * @return 分页查询结果VO对象
   */
  @ApiOperation("管理端条件分页查询订单")
  @GetMapping("/conditionSearch")
  public Result<PageResult<OrderVO>> search(OrdersPageQueryDTO ordersPageQueryDTO) {
    log.info("管理端条件分页查询订单：{}", ordersPageQueryDTO);
    PageResult<OrderVO> pageResult = orderService.pageQuery(ordersPageQueryDTO);
    return Result.success(pageResult);
  }

  /**
   * 根据id查询订单详情
   *
   * @param id 订单id
   * @return 订单VO对象
   */
  @ApiOperation("根据订单id查询订单详情")
  @GetMapping("/details/{id}")
  public Result<OrderVO> details(@PathVariable Long id) {
    log.info("管理端查询订单详情：{}", id);
    OrderVO orderVO = orderService.getById(id);
    return Result.success(orderVO);
  }

  /**
   * 查询订单统计信息
   *
   * @return 订单统计信息
   */
  @ApiOperation("查询订单统计信息")
  @GetMapping("/statistics")
  public Result<OrderStatisticsVO> statistics() {
    log.info("管理端查询订单统计信息");
    OrderStatisticsVO orderStatisticsVO = orderService.statistics();
    return Result.success(orderStatisticsVO);
  }

  /**
   * 管理端接单
   *
   * @param id 订单id
   * @return Result
   */
  @ApiOperation("管理端接单")
  @PutMapping("/confirm")
  public Result<Object> confirm(@RequestBody Long id) {
    log.info("管理端接订单：{}", id);
    orderService.confirm(id);
    return Result.success();
  }

  /**
   * 管理端拒单
   *
   * @param ordersRejectionDTO 订单拒单信息
   * @return Result
   */
  @ApiOperation("拒单")
  @PutMapping("/rejection")
  public Result<Object> rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
    log.info("管理端拒单：{}", ordersRejectionDTO);
    orderService.rejection(ordersRejectionDTO);
    return Result.success();
  }

  /**
   * 管理端取消订单
   *
   * @param ordersCancelDTO 订单取消信息
   * @return Result
   */
  @ApiOperation("取消订单")
  @PutMapping("/cancel")
  public Result<Object> cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
    log.info("管理端取消订单：{}", ordersCancelDTO);
    orderService.adminCancel(ordersCancelDTO);
    return Result.success();
  }

  /**
   * 管理端派送订单
   *
   * @param id 订单id
   * @return Result
   */
  @ApiOperation("派送订单")
  @PutMapping("/delivery/{id}")
  public Result<Object> delivery(@PathVariable Long id) {
    log.info("管理端发货：{}", id);
    orderService.delivery(id);
    return Result.success();
  }

  /**
   * 管理端完成订单
   *
   * @param id 订单id
   * @return Result
   */
  @ApiOperation("完成订单")
  @PutMapping("/complete/{id}")
  public Result<Object> complete(@PathVariable Long id) {
    log.info("管理端完成订单：{}", id);
    orderService.complete(id);
    return Result.success();
  }
}
