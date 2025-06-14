package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜品相关接口")
@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {
  private final DishService dishService;

  public DishController(DishService dishService) {
    this.dishService = dishService;
  }

  /**
   * 新增菜品
   *
   * @param dishDTO 菜品信息
   * @return 新增结果
   */
  @ApiOperation("新增菜品")
  @PostMapping
  public Result<Object> save(@RequestBody DishDTO dishDTO) {
    log.info("新增菜品：{}", dishDTO);
    dishService.saveWithFlavor(dishDTO);
    return Result.success();
  }

  /**
   * 菜品分页查询
   *
   * @param dishPageQueryDTO 分页查询条件
   * @return 分页查询结果
   */
  @ApiOperation("菜品分页查询")
  @GetMapping("/page")
  public Result<PageResult<DishVO>> page(DishPageQueryDTO dishPageQueryDTO) {
    log.info("菜品分页查询：{}", dishPageQueryDTO);
    PageResult<DishVO> pageResult = dishService.pageQuery(dishPageQueryDTO);
    return Result.success(pageResult);
  }

  /**
   * 批量删除菜品
   *
   * @param ids 菜品ID列表
   * @return 删除结果
   */
  @ApiOperation("批量删除菜品")
  @DeleteMapping
  public Result<Object> delete(@RequestParam List<Long> ids) {
    log.info("批量删除菜品：{}", ids);
    dishService.deleteBatch(ids);
    return Result.success();
  }

  /**
   * 根据id查询菜品
   *
   * @param id 菜品id
   * @return 菜品对象
   */
  @ApiOperation("根据id查询菜品")
  @GetMapping("/{id}")
  public Result<DishVO> getById(@PathVariable Long id) {
    log.info("根据id查询菜品：{}", id);
    DishVO dishVO = dishService.getByIdWithFlavor(id);
    return Result.success(dishVO);
  }

  /**
   * 修改菜品
   *
   * @param dishDTO 菜品信息
   * @return 修改结果
   */
  @ApiOperation("修改菜品")
  @PutMapping
  public Result<Object> update(@RequestBody DishDTO dishDTO) {
    log.info("修改菜品：{}", dishDTO);
    dishService.updateWithFlavor(dishDTO);
    return Result.success();
  }

  /**
   * 起售停售菜品
   *
   * @param status 状态 0:停售 1:起售
   * @param id 菜品id
   * @return 操作结果
   */
  @ApiOperation("起售停售菜品")
  @PostMapping("/status/{status}")
  public Result<Object> status(@PathVariable Integer status, Long id){
    log.info("修改菜品状态：设置status: {}, 菜品id: {}", status, id);
    dishService.startOrStop(status, id);
    return Result.success();
  }
}
