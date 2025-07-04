package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
  private final ShoppingCartMapper shoppingCartMapper;
  private final DishMapper dishMapper;
  private final SetmealMapper setmealMapper;

  public ShoppingCartServiceImpl(ShoppingCartMapper shoppingCartMapper, DishMapper dishMapper, SetmealMapper setmealMapper) {
    this.shoppingCartMapper = shoppingCartMapper;
    this.dishMapper = dishMapper;
    this.setmealMapper = setmealMapper;
  }

  /**
   * 向购物车中添加商品，如果购物车中已经存在该商品，则更新数量，否则添加新商品
   *
   * @param shoppingCartDTO 购物车传输信息
   */
  @Override
  public void add(ShoppingCartDTO shoppingCartDTO) {
    // 获取当前用户ID
    Long userId = BaseContext.getCurrentId();
    // 构造查询条件
    ShoppingCart shoppingCart = new ShoppingCart();
    BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
    shoppingCart.setUserId(userId);
    // 查询购物车中是否已经存在该商品
    ShoppingCart cartItem = shoppingCartMapper.getByUserIdAndDishOrSetmealId(shoppingCart);
    if (cartItem != null) {
      // 如果已经存在，则数量加1
      cartItem.setNumber(cartItem.getNumber() + 1);
      shoppingCartMapper.updateNumberById(cartItem);
    } else {
      // 如果不存在，则添加新记录
      // 设置默认数量为1
      shoppingCart.setNumber(1);
      // 设置创建时间
      shoppingCart.setCreateTime(LocalDateTime.now());
      // 判断添加的是菜品还是套餐，并获取其它相关信息
      Long dishId = shoppingCartDTO.getDishId();
      Long setmealId = shoppingCartDTO.getSetmealId();
      if (dishId != null) {
        // 添加的是菜品
        Dish dish = dishMapper.getById(dishId);
        shoppingCart.setName(dish.getName());
        shoppingCart.setImage(dish.getImage());
        shoppingCart.setAmount(dish.getPrice());
      } else if (setmealId != null) {
        // 添加的是套餐
        Setmeal setmeal = setmealMapper.getById(setmealId);
        shoppingCart.setName(setmeal.getName());
        shoppingCart.setImage(setmeal.getImage());
        shoppingCart.setAmount(setmeal.getPrice());
      }
      // 添加到购物车
      shoppingCartMapper.insert(shoppingCart);
    }
  }
}
