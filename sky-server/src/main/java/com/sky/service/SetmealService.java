package com.sky.service;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

public interface SetmealService {
  /**
   * 套餐分页查询
   * @param setmealPageQueryDTO 分页查询条件
   * @return 分页查询结果
   */
  PageResult<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
}
