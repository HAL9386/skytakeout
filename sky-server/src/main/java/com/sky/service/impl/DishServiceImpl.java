package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
  private final DishMapper dishMapper;

  private final DishFlavorMapper dishFlavorMapper;

  private final SetmealDishMapper setmealDishMapper;

  public DishServiceImpl(DishMapper dishMapper, DishFlavorMapper dishFlavorMapper, SetmealDishMapper setmealDishMapper) {
    this.dishMapper = dishMapper;
    this.dishFlavorMapper = dishFlavorMapper;
    this.setmealDishMapper = setmealDishMapper;
  }

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

  /**
   * 菜品分页查询
   *
   * @param dishPageQueryDTO 分页查询条件
   * @return 分页查询结果VO对象
   */
  @Override
  public PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
    PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
    Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
    return new PageResult<>(page.getTotal(), page.getResult());
  }

  /**
   * 批量删除菜品，同时删除对应的口味数据
   * 1. 起售中的菜品不能删除
   * 2. 菜品存在关联套餐，不能删除
   *
   * @param ids 菜品ID列表
   */
  @Transactional(rollbackFor = {Exception.class})
  @Override
  public void deleteBatch(List<Long> ids) {
    for (Long id : ids) {
      Dish dish = dishMapper.getById(id);
      if (dish != null && dish.getStatus().equals(StatusConstant.ENABLE)) {
        throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
      }
    }
    List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
    if (setmealIds != null && !setmealIds.isEmpty()) {
      throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
    }
    for (Long dishId : ids) {
      dishMapper.deleteById(dishId);
      dishFlavorMapper.deleteByDishId(dishId);
    }
  }

  /**
   * 根据ID查询菜品信息和对应的口味信息
   *
   * @param id 菜品ID
   * @return 菜品信息和对应的口味信息
   */
  @Override
  public DishVO getByIdWithFlavor(Long id) {
    Dish dish = dishMapper.getById(id);
    if (dish == null) {
      return null;
    }
    DishVO dishVO = new DishVO();
    BeanUtils.copyProperties(dish, dishVO);
    List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
    dishVO.setFlavors(flavors);
    return dishVO;
  }

  /**
   * 根据ID修改菜品信息，同时更新对应的口味信息
   *
   * @param dishDTO 菜品信息
   */
  @Transactional(rollbackFor = {Exception.class})
  @Override
  public void updateWithFlavor(DishDTO dishDTO) {
    Dish dish = new Dish();
    BeanUtils.copyProperties(dishDTO, dish);
    dishMapper.update(dish);
    dishFlavorMapper.deleteByDishId(dishDTO.getId());
    List<DishFlavor> flavors = dishDTO.getFlavors();
    if (flavors != null && !flavors.isEmpty()) {
      flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishDTO.getId()));
      dishFlavorMapper.insertBatch(flavors);
    }
  }

  /**
   * 起售停售菜品
   *
   * @param status 起售停售状态
   * @param id     菜品id
   */
  @Override
  public void startOrStop(Integer status, Long id) {
    Dish dish = Dish.builder()
      .id(id)
      .status(status)
      .build();
    dishMapper.update(dish);
  }
}
