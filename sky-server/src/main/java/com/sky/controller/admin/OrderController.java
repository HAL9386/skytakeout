package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
