package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "套餐相关接口")
@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {

  private final SetmealService setmealService;

  public SetmealController(SetmealService setmealService) {
    this.setmealService = setmealService;
  }

  /**
   * 套餐分页查询
   *
   * @param setmealPageQueryDTO 分页查询条件
   * @return 分页查询结果
   */
  @ApiOperation("套餐分页查询")
  @GetMapping("/page")
  public Result<PageResult<SetmealVO>> page(SetmealPageQueryDTO setmealPageQueryDTO){
    log.info("套餐分页查询：{}", setmealPageQueryDTO);
    PageResult<SetmealVO> pageResult = setmealService.pageQuery(setmealPageQueryDTO);
    return Result.success(pageResult);
  }

  /**
   * 新增套餐
   *
   * @param setmealDTO 新增套餐信息
   * @return 新增结果
   */
  @ApiOperation("新增套餐")
  @PostMapping
  public Result<Object> save(@RequestBody SetmealDTO setmealDTO){
    log.info("新增套餐：{}", setmealDTO);
    setmealService.save(setmealDTO);
    return Result.success();
  }

  /**
   * 批量删除套餐
   *
   * @param ids 套餐id列表
   * @return 删除结果
   */
  @ApiOperation("批量删除套餐")
  @DeleteMapping
  public Result<Object> delete(@RequestParam List<Long> ids){
    log.info("批量删除套餐：{}", ids);
    if (ids == null || ids.isEmpty()){
      return Result.error(MessageConstant.SETMEAL_NOT_SELECTED);
    }
    setmealService.deleteBatch(ids);
    return Result.success();
  }

  /**
   * 根据id查询套餐
   *
   * @param id 套餐id
   * @return 套餐对象
   */
  @ApiOperation("根据id查询套餐")
  @GetMapping("/{id}")
  public Result<SetmealVO> getById(@PathVariable Long id){
    log.info("根据id查询套餐：{}", id);
    SetmealVO setmealVO = setmealService.getByIdWithDishes(id);
    return Result.success(setmealVO);
  }

  /**
   * 修改套餐
   *
   * @param setmealDTO 修改套餐信息
   * @return 修改结果
   */
  @ApiOperation("修改套餐")
  @PutMapping
  public Result<Object> update(@RequestBody SetmealDTO setmealDTO){
    log.info("修改套餐：{}", setmealDTO);
    setmealService.updateWithDishes(setmealDTO);
    return Result.success();
  }

  /**
   * 起售/停售套餐
   *
   * @param status 起售/停售状态
   * @param id 套餐id
   * @return 起售/停售结果
   */
  @ApiOperation("起售停售套餐")
  @PostMapping("/status/{status}")
  public Result<Object> status(@PathVariable Integer status, Long id){
    log.info("修改套餐状态：设置status: {}，菜品id: {}", status, id);
    setmealService.updateStatus(status, id);
    return Result.success();
  }
}
