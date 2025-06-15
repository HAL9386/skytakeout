package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
  /**
   * 套餐分页查询
   * @param setmealPageQueryDTO 分页查询条件
   * @return 分页查询结果
   */
  PageResult<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

  /**
   * 新增套餐
   * @param setmealDTO 套餐数据
   */
  void save(SetmealDTO setmealDTO);

  /**
   * 批量删除套餐
   * @param ids 套餐ID列表
   */
  void deleteBatch(List<Long> ids);
}
