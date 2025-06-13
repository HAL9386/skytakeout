package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

  /**
   * 根据分类id查询菜品数量
   *
   * @param categoryId 分类id
   * @return 菜品数量
   */
  @Select("select count(id) from dish where category_id = #{categoryId}")
  Integer countByCategoryId(Long categoryId);


  /**
   * 插入菜品数据
   *
   * @param dish 菜品对象
   */
  @AutoFill(value = OperationType.INSERT)
  void insert(Dish dish);

  /**
   * 菜品分页查询
   *
   * @param dishPageQueryDTO 分页查询条件
   * @return 分页查询结果VO对象
   */
  Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
