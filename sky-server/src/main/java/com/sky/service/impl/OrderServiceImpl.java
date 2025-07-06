package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
  private final OrderMapper orderMapper;
  private final OrderDetailMapper orderDetailMapper;
  private final AddressBookMapper addressBookMapper;
  private final ShoppingCartMapper shoppingCartMapper;
  private final UserMapper userMapper;
  private final WeChatPayUtil weChatPayUtil;

  public OrderServiceImpl(OrderMapper orderMapper, OrderDetailMapper orderDetailMapper, AddressBookMapper addressBookMapper,
                          ShoppingCartMapper shoppingCartMapper, UserMapper userMapper, WeChatPayUtil weChatPayUtil) {
    this.orderMapper = orderMapper;
    this.orderDetailMapper = orderDetailMapper;
    this.addressBookMapper = addressBookMapper;
    this.shoppingCartMapper = shoppingCartMapper;
    this.userMapper = userMapper;
    this.weChatPayUtil = weChatPayUtil;
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

  /**
   * 订单支付
   *
   * @param ordersPaymentDTO 订单支付信息
   * @return orderPaymentVO 订单支付结果
   */
  public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
    // 当前登录用户id
    Long userId = BaseContext.getCurrentId();
    User user = userMapper.getById(userId);

    //调用微信支付接口，生成预支付交易单
    JSONObject jsonObject = weChatPayUtil.pay(
      ordersPaymentDTO.getOrderNumber(), //商户订单号
      new BigDecimal("0.01"), //支付金额，单位 元
      "苍穹外卖订单", //商品描述
      user.getOpenid() //微信用户的openid
    );

    if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
      throw new OrderBusinessException("该订单已支付");
    }

    OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
    vo.setPackageStr(jsonObject.getString("package"));

    return vo;
  }

  /**
   * 支付成功，修改订单状态
   *
   * @param outTradeNo 支付订单号
   */
  public void paySuccess(String outTradeNo) {

    // 根据订单号查询订单
    Orders ordersDB = orderMapper.getByNumber(outTradeNo);

    // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
    Orders orders = Orders.builder()
      .id(ordersDB.getId())
      .status(Orders.TO_BE_CONFIRMED)
      .payStatus(Orders.PAID)
      .checkoutTime(LocalDateTime.now())
      .build();

    orderMapper.update(orders);
  }
}
