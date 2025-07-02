package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "C端菜品相关接口")
@Slf4j
@RequestMapping("/user/dish")
@RestController("userDishController")
public class DishController {
  private final DishService dishService;

  public DishController(DishService dishService) {
    this.dishService = dishService;
  }

  /**
   * 根据分类id查询菜品
   *
   * @param categoryId 分类id
   * @return 菜品列表
   */
  @ApiOperation("根据分类id查询菜品")
  @GetMapping("/list")
  public Result<List<DishVO>> listByCategoryId(Long categoryId) {
    log.info("C端根据分类id查询菜品：{}", categoryId);
    Dish dish = Dish.builder()
      .categoryId(categoryId)
      .status(StatusConstant.ENABLE)
      .build();
    List<DishVO> dishVOList = dishService.listWithFlavor(dish);
    return Result.success(dishVOList);
  }
}
