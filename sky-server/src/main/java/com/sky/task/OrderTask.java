package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class OrderTask {
  private final OrderMapper orderMapper;

  public OrderTask(OrderMapper orderMapper) {
    this.orderMapper = orderMapper;
  }

  /**
   * 定时任务：取消超时未支付的订单
   */
  @Scheduled(cron = "0 * * * * *")
  public void cancelPaymentTimeoutOrder() {
    log.info("定时任务：取消超时未支付的订单 {}", new Date());
    LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
    List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
    if (ordersList == null || ordersList.isEmpty()) {
      return;
    }
    ordersList.forEach(orders -> {
      orders.setStatus(Orders.CANCELLED);
      orders.setCancelTime(LocalDateTime.now());
      orders.setCancelReason("订单超时，自动取消");
      orderMapper.update(orders);
    });
  }

  /**
   * 定时任务：每日凌晨1点，将派送中的订单的订单状态设置为完成
   */
  @Scheduled(cron = "0 0 1 * * *")
  public void completeDeliveryOrder() {
    log.info("定时任务：将派送中的订单的订单状态设置为完成 {}", new Date());
    LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
    List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
    if (ordersList == null || ordersList.isEmpty()) {
      return;
    }
    ordersList.forEach(orders -> {
      orders.setStatus(Orders.COMPLETED);
      orderMapper.update(orders);
    });
  }
}
