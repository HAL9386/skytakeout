package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "C端套餐相关接口")
@Slf4j
@RequestMapping("/user/setmeal")
@RestController("userSetmealController")
public class SetmealController {
  private final SetmealService setmealService;

  public SetmealController(SetmealService setmealService) {
    this.setmealService = setmealService;
  }

  /**
   * 根据分类id查询套餐
   *
   * @param categoryId 分类id
   * @return 套餐列表
   */
  @ApiOperation("根据分类id查询套餐")
  @GetMapping("/list")
  public Result<List<Setmeal>> list(Long categoryId) {
    log.info("C端根据分类id查询套餐：{}", categoryId);
    Setmeal setmeal = Setmeal.builder()
      .categoryId(categoryId)
      .status(StatusConstant.ENABLE)
      .build();
    List<Setmeal> setmealList = setmealService.list(setmeal);
    return Result.success(setmealList);
  }

  /**
   * 根据套餐id查询菜品
   *
   * @param id 套餐id
   * @return 菜品列表
   */
  @ApiOperation("根据套餐id查询菜品")
  @GetMapping("/dish/{id}")
  public Result<List<DishItemVO>> listBySetmealId(@PathVariable Long id) {
    log.info("C端根据套餐id查询菜品：{}", id);
    List<DishItemVO> dishItemVOList = setmealService.getDishItemById(id);
    return Result.success(dishItemVOList);
  }
}
