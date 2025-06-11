package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {
  /**
   * 新增菜品，同时保存对应的口味数据
   *
   * @param dishDTO 菜品基本信息及对应的口味数据
   */
  void saveWithFlavor(DishDTO dishDTO);
}
