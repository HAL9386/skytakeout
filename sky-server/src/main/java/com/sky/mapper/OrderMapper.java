package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

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

  /**
   * 订单分页查询
   *
   * @param ordersPageQueryDTO 分页查询条件
   * @return 分页查询结果VO对象
   */
  Page<OrderVO> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

  /**
   * 根据id查询订单详情
   *
   * @param id 订单id
   * @return 订单VO对象
   */
  OrderVO getById(Long id);

  /**
   * 根据订单状态统计订单数量
   *
   * @param status 订单状态
   * @return 订单数量
   */
  @Select("select count(*) from orders where status = #{status}")
  Integer countByStatus(Integer status);

  /**
   * 根据订单状态和订单时间查询订单
   *
   * @param deliveryInProgress 订单状态
   * @param time 订单时间
   * @return 订单列表
   */
  @Select("select * from orders where status = #{deliveryInProgress} and order_time < #{time}")
  List<Orders> getByStatusAndOrderTimeLT(Integer deliveryInProgress, LocalDateTime time);
}
