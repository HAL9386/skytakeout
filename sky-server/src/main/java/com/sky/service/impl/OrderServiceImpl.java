package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
  private final OrderMapper orderMapper;
  private final OrderDetailMapper orderDetailMapper;
  private final AddressBookMapper addressBookMapper;
  private final ShoppingCartMapper shoppingCartMapper;

  public OrderServiceImpl(OrderMapper orderMapper, OrderDetailMapper orderDetailMapper, AddressBookMapper addressBookMapper,
                          ShoppingCartMapper shoppingCartMapper) {
    this.orderMapper = orderMapper;
    this.orderDetailMapper = orderDetailMapper;
    this.addressBookMapper = addressBookMapper;
    this.shoppingCartMapper = shoppingCartMapper;
  }

  /**
   * C端订单提交，异常处理：1. 地址信息是否存在；2. 购物车信息是否存在。
   * 如果均存在，则创建订单对象，插入订单表和订单明细表，并清空购物车。
   *
   * @param ordersSubmitDTO 订单提交信息
   * @return orderSubmitVO 订单提交结果
   */
  @Transactional(rollbackFor = {Exception.class})
  @Override
  public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
    AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
    if (addressBook == null) {
      throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
    }
    List<ShoppingCart> cartList = shoppingCartMapper.list(BaseContext.getCurrentId());
    if (cartList == null || cartList.isEmpty()) {
      throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
    }
    Orders order = this.makeOrder(ordersSubmitDTO, addressBook);
    orderMapper.insert(order);
    List<OrderDetail> orderDetailList = new ArrayList<>();
    for (ShoppingCart cart : cartList) {
      OrderDetail orderDetail = new OrderDetail();
      BeanUtils.copyProperties(cart, orderDetail);
      orderDetail.setOrderId(order.getId());
      orderDetailList.add(orderDetail);
    }
    orderDetailMapper.insertBatch(orderDetailList);
    shoppingCartMapper.clearByUserId(BaseContext.getCurrentId());
    return OrderSubmitVO.builder()
      .id(order.getId())
      .orderNumber(order.getNumber())
      .orderAmount(order.getAmount())
      .orderTime(order.getOrderTime())
      .build();
  }

  /**
   * 根据订单提交信息和查询到的地址簿信息创建订单对象
   *
   * @param ordersSubmitDTO 订单提交信息
   * @param addressBook     地址簿信息
   * @return 创建的订单对象
   */
  private Orders makeOrder(OrdersSubmitDTO ordersSubmitDTO, AddressBook addressBook) {
    Orders order = new Orders();
    BeanUtils.copyProperties(ordersSubmitDTO, order);
    order.setUserId(BaseContext.getCurrentId());
    order.setNumber(String.valueOf(System.currentTimeMillis()));
    order.setStatus(Orders.PENDING_PAYMENT);
    order.setOrderTime(LocalDateTime.now());
    order.setPayStatus(Orders.UN_PAID);
    order.setPhone(addressBook.getPhone());
    order.setAddress(addressBook.getDetail());
    order.setConsignee(addressBook.getConsignee());
    return order;
  }
}
