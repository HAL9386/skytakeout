package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
  @Autowired
  private DishMapper dishMapper;

  @Autowired
  private DishFlavorMapper dishFlavorMapper;

  /**
   * 新增菜品，同时保存对应的口味数据
   *
   * @param dishDTO 菜品基本信息及对应的口味数据
   */
  @Transactional(rollbackFor = {Exception.class})
  @Override
  public void saveWithFlavor(DishDTO dishDTO) {
    Dish dish = new Dish();
    BeanUtils.copyProperties(dishDTO, dish);
    // 保存菜品基本信息到菜品表dish
    // 保存菜品对应的口味数据到菜品口味表dish_flavor
    dishMapper.insert(dish);
    Long dishId = dish.getId();
    List<DishFlavor> flavors = dishDTO.getFlavors();
    if (flavors != null && !flavors.isEmpty()) {
      flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
      dishFlavorMapper.insertBatch(flavors);
    }
  }
}
