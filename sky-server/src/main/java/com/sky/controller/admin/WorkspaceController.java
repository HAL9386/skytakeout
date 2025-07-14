package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Api(tags = "工作台相关接口")
@Slf4j
@RequestMapping("/admin/workspace")
@RestController
public class WorkspaceController {
  private final WorkspaceService workspaceService;

  public WorkspaceController(WorkspaceService workspaceService) {
    this.workspaceService = workspaceService;
  }

  /**
   * 获取今日营业数据
   *
   * @return BusinessDataVO
   */
  @ApiOperation("获取今日营业数据")
  @GetMapping("/businessData")
  public Result<BusinessDataVO> getBusinessData() {
    log.info("获取今日营业数据");
    BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDate.now(), LocalDate.now());
    return Result.success(businessDataVO);
  }

  /**
   * 获取订单概览
   *
   * @return OrderOverViewVO
   */
  @ApiOperation("获取订单概览")
  @GetMapping("/overviewOrders")
  public Result<OrderOverViewVO> getOrderOverView() {
    log.info("获取订单概览");
    OrderOverViewVO orderOverViewVO = workspaceService.getOrderOverView();
    return Result.success(orderOverViewVO);
  }

  /**
   * 获取菜品概览
   *
   * @return DishOverViewVO
   */
  @ApiOperation("获取菜品概览")
  @GetMapping("/overviewDishes")
  public Result<DishOverViewVO> getDishOverView() {
    log.info("获取菜品概览");
    DishOverViewVO dishOverViewVO = workspaceService.getDishOverView();
    return Result.success(dishOverViewVO);
  }

  /**
   * 获取套餐概览
   *
   * @return SetmealOverViewVO
   */
  @ApiOperation("获取套餐概览")
  @GetMapping("/overviewSetmeals")
  public Result<SetmealOverViewVO> getSetmealOverView() {
    log.info("获取套餐概览");
    SetmealOverViewVO setmealOverViewVO = workspaceService.getSetmealOverView();
    return Result.success(setmealOverViewVO);
  }
}
