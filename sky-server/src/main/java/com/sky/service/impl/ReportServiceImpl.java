package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
  private final OrderMapper orderMapper;
  private final UserMapper userMapper;

  public ReportServiceImpl(OrderMapper orderMapper, UserMapper userMapper) {
    this.userMapper = userMapper;
    this.orderMapper = orderMapper;
  }

  /**
   * 营业额统计
   *
   * @param begin 开始时间
   * @param end   结束时间
   * @return 营业额统计
   */
  @Override
  public TurnoverReportVO getTurnover(LocalDate begin, LocalDate end) {
    List<LocalDate> dateList = this.makeBetweenDatesStep1Day(begin, end);
    List<Double> turnoverList = new ArrayList<>();
    for (LocalDate date : dateList) {
      LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
      LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
      Map<String, Object> queryCondition = Map.of(
        "status", Orders.COMPLETED,
        "begin", beginTime,
        "end", endTime
      );
      Double turnover = orderMapper.sumByMap(queryCondition);
      turnover = turnover == null ? 0.0 : turnover;
      turnoverList.add(turnover);
    }
    return TurnoverReportVO.builder()
      .dateList(StringUtils.join(dateList, ","))
      .turnoverList(StringUtils.join(turnoverList, ","))
      .build();
  }

  /**
   * 用户统计
   *
   * @param begin 开始时间
   * @param end   结束时间
   * @return 用户统计
   */
  @Override
  public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
    List<LocalDate> dateList = this.makeBetweenDatesStep1Day(begin, end);
    List<Integer> totalUserList = new ArrayList<>();
    List<Integer> newUserList = new ArrayList<>();
    for (LocalDate date : dateList) {
      LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
      LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
      Map<String, Object> queryCondition = new HashMap<>();
      queryCondition.put("end", endTime);
      Integer totalUser = userMapper.countByMap(queryCondition);
      queryCondition.put("begin", beginTime);
      Integer newUser = userMapper.countByMap(queryCondition);
      totalUserList.add(totalUser);
      newUserList.add(newUser);
    }
    return UserReportVO.builder()
     .dateList(StringUtils.join(dateList, ","))
     .totalUserList(StringUtils.join(totalUserList, ","))
     .newUserList(StringUtils.join(newUserList, ","))
     .build();
  }

  /**
   * 生成指定日期范围内的日期列表
   *
   * @param startDate 开始日期
   * @param endDate   结束日期
   * @return 日期列表
   */
  private List<LocalDate> makeBetweenDatesStep1Day(LocalDate startDate, LocalDate endDate) {
    List<LocalDate> dates = new ArrayList<>();
    LocalDate currentDate = startDate;
    while (!currentDate.isAfter(endDate)) {
      dates.add(currentDate);
      currentDate = currentDate.plusDays(1);
    }
    return dates;
  }
}
