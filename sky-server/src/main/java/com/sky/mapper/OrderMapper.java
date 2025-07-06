package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {
  /**
   * 插入订单数据
   *
   * @param order 订单对象
   */
  void insert(Orders order);

  /**
   * 根据订单号查询订单
   *
   * @param orderNumber 订单号
   * @return 订单对象
   */
  @Select("select * from orders where number = #{orderNumber}")
  Orders getByNumber(String orderNumber);

  /**
   * 修改订单信息
   *
   * @param orders 订单对象
   */
  void update(Orders orders);
}
