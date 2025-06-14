package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
  /**
   * 新增菜品，同时保存对应的口味数据
   *
   * @param dishDTO 菜品基本信息及对应的口味数据
   */
  void saveWithFlavor(DishDTO dishDTO);

  /**
   * 菜品分页查询
   *
   * @param dishPageQueryDTO 分页查询条件
   * @return 分页查询结果
   */
  PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

  /**
   * 菜品批量删除
   *
   * @param ids 菜品id列表
   */
  void deleteBatch(List<Long> ids);
}
