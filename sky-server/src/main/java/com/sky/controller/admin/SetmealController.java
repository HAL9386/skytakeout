package com.sky.controller.admin;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
