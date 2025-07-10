package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

  /**
   * 条件查询
   *
   * @param shoppingCart 购物车对象
   * @return 查询结果
   */
  @Select("select * from shopping_cart where user_id = #{userId} " +
    "and (dish_id = #{dishId} or setmeal_id = #{setmealId}) limit 1")
  ShoppingCart getByUserIdAndDishOrSetmealId(ShoppingCart shoppingCart);

  /**
   * 新增购物车数据
   *
   * @param shoppingCart 购物车对象
   */
  @Insert("insert into shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) " +
    "values (#{name}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{image}, #{createTime})")
  void insert(ShoppingCart shoppingCart);

  /**
   * 更新购物车中的商品数量
   *
   * @param shoppingCart 购物车对象
   */
  @Update("update shopping_cart set number = #{number} where id = #{id}")
  void updateNumberById(ShoppingCart shoppingCart);

  /**
   * 根据id查询购物车数据
   *
   * @param userId 主键
   * @return 购物车对象
   */
  @Select("select * from shopping_cart where user_id = #{userId}")
  List<ShoppingCart> list(Long userId);

  /**
   * 清空购物车
   *
   * @param userId 用户id
   */
  @Delete("delete from shopping_cart where user_id = #{userId}")
  void clearByUserId(Long userId);

  /**
   * 删除购物车中的商品
   *
   * @param id 主键
   */
  @Delete("delete from shopping_cart where id = #{id}")
  void deleteById(Long id);

  /**
   * 批量插入购物车数据
   *
   * @param cartList 购物车数据列表
   */
  void insertBatch(List<ShoppingCart> cartList);
}
