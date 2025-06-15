package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

  /**
   * 根据菜品id查询对应的套餐id
   *
   * @param ids 菜品id列表
   * @return 套餐id列表
   */
  List<Long> getSetmealIdsByDishIds(List<Long> ids);

  /**
   * 批量插入套餐菜品关联数据
   *
   * @param setmealDishes 套餐菜品关联数据列表
   */
  void insertBatch(List<SetmealDish> setmealDishes);

  /**
   * 根据套餐id删除套餐菜品关联数据
   *
   * @param setmealIds 套餐id列表
   */
  void deleteBatchBySetmealIds(List<Long> setmealIds);
}
