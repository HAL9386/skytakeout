package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
  private final UserMapper userMapper;
  private final OrderMapper orderMapper;

  public WorkspaceServiceImpl(UserMapper userMapper, OrderMapper orderMapper) {
    this.userMapper = userMapper;
    this.orderMapper = orderMapper;
  }

  /**
   * 获取营业数据
   *
   * @return BusinessDataVO
   */
  @Override
  public BusinessDataVO getBusinessData() {
    Map<String, Object> queryCondition = new HashMap<>();
    queryCondition.put("begin", LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
    queryCondition.put("end", LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
    Integer newUsers = userMapper.countByMap(queryCondition);
    newUsers = newUsers == null ? 0 : newUsers;
    Integer totalOrders = orderMapper.countByMap(queryCondition);
    totalOrders = totalOrders == null ? 0 : totalOrders;
    queryCondition.put("status", Orders.COMPLETED);
    Integer validOrderCount = orderMapper.countByMap(queryCondition);
    validOrderCount = validOrderCount == null ? 0 : validOrderCount;
    Double orderCompletionRate = validOrderCount == 0 ? 0.0 : (double) validOrderCount / totalOrders;
    Double turnover = orderMapper.sumByMap(queryCondition);
    turnover = turnover == null ? 0.0 : turnover;
    Double unitPrice = turnover == 0 ? 0.0 : turnover / validOrderCount;
    return BusinessDataVO.builder()
      .newUsers(newUsers)
      .validOrderCount(validOrderCount)
      .orderCompletionRate(orderCompletionRate)
      .turnover(turnover)
      .unitPrice(unitPrice)
      .build();
  }
}
