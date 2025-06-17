package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
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

  /**
   * 修改套餐
   *
   * @param setmealDTO 套餐数据
   */
  void updateWithDishes(SetmealDTO setmealDTO);

  /**
   * 根据id查询套餐
   *
   * @param id 套餐id
   * @return 套餐信息
   */
  SetmealVO getByIdWithDishes(Long id);

  /**
   * 起售停售
   *
   * @param status 状态 0:停售 1:起售
   * @param id 套餐id
   */
  void updateStatus(Integer status, Long id);

  /**
   * 根据条件查询套餐
   *
   * @param setmeal 套餐查询条件
   * @return 套餐集合
   */
  List<Setmeal> list(Setmeal setmeal);

  /**
   * 根据id查询菜品选项
   *
   * @param id 套餐id
   * @return 菜品选项集合
   */
  List<DishItemVO> getDishItemById(Long id);
}
