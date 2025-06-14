package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl implements SetmealService {

  private final SetmealMapper setmealMapper;

  public SetmealServiceImpl(SetmealMapper setmealMapper) {
    this.setmealMapper = setmealMapper;
  }

  /**
   * 套餐分页查询
   * @param setmealPageQueryDTO 分页查询条件
   * @return 分页查询结果
   */
  @Override
  public PageResult<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
    PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
    Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
    return new PageResult<>(page.getTotal(), page.getResult());
  }
}
