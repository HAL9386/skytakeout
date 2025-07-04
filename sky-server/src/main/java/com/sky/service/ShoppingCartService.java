package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

public interface ShoppingCartService {
  /**
   * 向购物车中添加商品
   *
   * @param shoppingCartDTO 购物车传输信息
   */
  void add(ShoppingCartDTO shoppingCartDTO);
}
