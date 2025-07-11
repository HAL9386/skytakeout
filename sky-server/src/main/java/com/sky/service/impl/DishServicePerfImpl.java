package com.sky.service.impl;

import com.sky.entity.Dish;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServicePerfImpl extends DishServiceImpl implements DishService {

  public DishServicePerfImpl(DishMapper dishMapper, DishFlavorMapper dishFlavorMapper, SetmealDishMapper setmealDishMapper) {
    super(dishMapper, dishFlavorMapper, setmealDishMapper);
  }

  /**
   * 关联查询菜品和口味
   *
   * @param dish 菜品对象
   * @return 菜品列表
   */
  @Override
  public List<DishVO> listWithFlavor(Dish dish) {
    return dishMapper.listWithFlavor(dish);
  }
}
