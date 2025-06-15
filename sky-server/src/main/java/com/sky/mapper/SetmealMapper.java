package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

  /**
   * 根据分类id查询套餐的数量
   *
   * @param id 分类id
   * @return 套餐的数量
   */
  @Select("select count(id) from setmeal where category_id = #{categoryId}")
  Integer countByCategoryId(Long id);

  /**
   * 套餐分页查询
   *
   * @param setmealPageQueryDTO 分页查询条件
   * @return 分页查询结果
   */
  Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

  /**
   * 插入套餐数据
   *
   * @param setmeal 套餐对象
   */
  @AutoFill(value = OperationType.INSERT)
  void insert(Setmeal setmeal);

  /**
   * 根据ID查询套餐
   *
   * @param id 套餐ID
   * @return 套餐对象
   */
  @Select("select * from setmeal where id = #{id}")
  Setmeal getById(Long id);

  /**
   * 根据id批量删除套餐
   *
   * @param ids 套餐ID列表
   */
  void deleteBatch(List<Long> ids);

  /**
   * 更新套餐信息
   *
   * @param setmeal 套餐对象
   */
  @AutoFill(value = OperationType.UPDATE)
  void update(Setmeal setmeal);
}
