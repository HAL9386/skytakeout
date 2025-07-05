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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    ShoppingCart cartItem = this.getCartItem(shoppingCartDTO);
    if (cartItem != null) {
      cartItem.setNumber(cartItem.getNumber() + 1);
      shoppingCartMapper.updateNumberById(cartItem);
      return;
    }
    cartItem = ShoppingCart.builder()
      .userId(BaseContext.getCurrentId())
      .dishId(shoppingCartDTO.getDishId())
      .setmealId(shoppingCartDTO.getSetmealId())
      .number(1)
      .createTime(LocalDateTime.now())
      .build();
    if (cartItem.getDishId() != null) {
      Dish dish = dishMapper.getById(cartItem.getDishId());
      cartItem.setName(dish.getName());
      cartItem.setImage(dish.getImage());
      cartItem.setAmount(dish.getPrice());
    } else if (cartItem.getSetmealId() != null) {
      Setmeal setmeal = setmealMapper.getById(cartItem.getSetmealId());
      cartItem.setName(setmeal.getName());
      cartItem.setImage(setmeal.getImage());
      cartItem.setAmount(setmeal.getPrice());
    }
    shoppingCartMapper.insert(cartItem);
  }

  /**
   * 查看购物车
   *
   * @return 购物车列表
   */
  @Override
  public List<ShoppingCart> list() {
    return shoppingCartMapper.list(BaseContext.getCurrentId());
  }

  /**
   * 清空购物车
   */
  @Override
  public void clear() {
    shoppingCartMapper.clearByUserId(BaseContext.getCurrentId());
  }

  /**
   * 减少购物车中的商品数量
   *
   * @param shoppingCartDTO 购物车传输信息
   */
  @Override
  public void sub(ShoppingCartDTO shoppingCartDTO) {
    ShoppingCart cartItem = this.getCartItem(shoppingCartDTO);
    if (cartItem == null) {
      return;
    }
    if (cartItem.getNumber() > 1) {
      cartItem.setNumber(cartItem.getNumber() - 1);
      shoppingCartMapper.updateNumberById(cartItem);
    } else {
      shoppingCartMapper.deleteById(cartItem.getId());
    }
  }

  /**
   * 根据购物车传输信息构造购物车对象
   * 查询购物车中记录并返回
   *
   * @param shoppingCartDTO 购物车传输信息
   * @return 购物车中的商品
   */
  private ShoppingCart getCartItem(ShoppingCartDTO shoppingCartDTO) {
    ShoppingCart shoppingCart = ShoppingCart.builder()
      .userId(BaseContext.getCurrentId())
      .dishId(shoppingCartDTO.getDishId())
      .setmealId(shoppingCartDTO.getSetmealId())
      .build();
    return shoppingCartMapper.getByUserIdAndDishOrSetmealId(shoppingCart);
  }
}
