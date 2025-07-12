package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
  /**
   * 营业额统计
   *
   * @param begin 开始时间
   * @param end   结束时间
   * @return TurnoverReportVO 营业额统计信息
   */
  TurnoverReportVO getTurnover(LocalDate begin, LocalDate end);

  /**
   * 用户统计
   *
   * @param begin 开始时间
   * @param end   结束时间
   * @return UserReportVO 用户统计信息
   */
  UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

  /**
   * 订单统计
   *
   * @param begin 开始时间
   * @param end   结束时间
   * @return OrderReportVO 订单统计信息
   */
  OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

  /**
   * 获取销售额排名前十的菜品
   *
   * @param begin 开始时间
   * @param end   结束时间
   * @return SalesTop10ReportVO 销售额排名前十的菜品信息
   */
  SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);
}
