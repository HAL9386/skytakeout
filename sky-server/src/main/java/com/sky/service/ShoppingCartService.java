package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
  /**
   * 向购物车中添加商品
   *
   * @param shoppingCartDTO 购物车传输信息
   */
  void add(ShoppingCartDTO shoppingCartDTO);

  /**
   * 查询购物车列表
   *
   * @return 购物车列表
   */
  List<ShoppingCart> list();

  /**
   * 清空购物车
   */
  void clear();
}
