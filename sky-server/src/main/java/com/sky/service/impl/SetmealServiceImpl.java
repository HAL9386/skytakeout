package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

  private final SetmealMapper setmealMapper;

  private final SetmealDishMapper setmealDishMapper;

  public SetmealServiceImpl(SetmealMapper setmealMapper, SetmealDishMapper setmealDishMapper) {
    this.setmealMapper = setmealMapper;
    this.setmealDishMapper = setmealDishMapper;
  }

  /**
   * 套餐分页查询
   *
   * @param setmealPageQueryDTO 分页查询条件
   * @return 分页查询结果
   */
  @Override
  public PageResult<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
    PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
    Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
    return new PageResult<>(page.getTotal(), page.getResult());
  }

  /**
   * 新增套餐
   *
   * @param setmealDTO 新增套餐信息
   */
  @Transactional(rollbackFor = {Exception.class})
  @Override
  public void save(SetmealDTO setmealDTO) {
    Setmeal setmeal = new Setmeal();
    BeanUtils.copyProperties(setmealDTO, setmeal);
    setmealMapper.insert(setmeal);
    List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
    if (setmealDishes != null) {
      setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
      setmealDishMapper.insertBatch(setmealDishes);
    }
  }

  /**
   * 批量删除套餐
   *
   * @param ids 套餐id列表
   */
  @Transactional(rollbackFor = {Exception.class})
  @Override
  public void deleteBatch(List<Long> ids) {
    for (Long id : ids) {
      Setmeal setmeal = setmealMapper.getById(id);
      if (setmeal != null && setmeal.getStatus().equals(StatusConstant.ENABLE)) {
        throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
      }
    }
    setmealMapper.deleteBatch(ids);
    setmealDishMapper.deleteBatchBySetmealIds(ids);
  }

  /**
   * 修改套餐
   *
   * @param setmealDTO 套餐数据
   */
  @Transactional(rollbackFor = {Exception.class})
  @Override
  public void updateWithDishes(SetmealDTO setmealDTO) {
    Setmeal setmeal = new Setmeal();
    BeanUtils.copyProperties(setmealDTO, setmeal);
    setmealMapper.update(setmeal);
    setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
    List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
    if (setmealDishes != null && !setmealDishes.isEmpty()) {
      setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealDTO.getId()));
      setmealDishMapper.insertBatch(setmealDishes);
    }
  }

  /**
   * 根据id查询套餐
   *
   * @param id 套餐id
   * @return 套餐信息
   */
  @Override
  public SetmealVO getByIdWithDishes(Long id) {
    Setmeal setmeal = setmealMapper.getById(id);
    if (setmeal == null) {
      return null;
    }
    SetmealVO setmealVO = new SetmealVO();
    BeanUtils.copyProperties(setmeal, setmealVO);
    List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
    setmealVO.setSetmealDishes(setmealDishes);
    return setmealVO;
  }

  /**
   * 起售停售套餐
   *
   * @param status 状态 0:停售 1:起售
   * @param id     套餐id
   */
  @Override
  public void updateStatus(Integer status, Long id) {
    Setmeal setmeal = Setmeal.builder()
      .id(id)
      .status(status)
      .build();
    setmealMapper.update(setmeal);
  }

  /**
   * 根据条件查询套餐
   *
   * @param setmeal 套餐查询条件
   * @return 套餐集合
   */
  public List<Setmeal> list(Setmeal setmeal) {
    return setmealMapper.list(setmeal);
  }

  /**
   * 根据id查询菜品选项
   *
   * @param id 套餐id
   * @return 菜品选项集合
   */
  public List<DishItemVO> getDishItemById(Long id) {
    return setmealMapper.getDishItemBySetmealId(id);
  }
}
