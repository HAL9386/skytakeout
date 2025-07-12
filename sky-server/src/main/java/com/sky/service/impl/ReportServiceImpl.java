package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
  private final OrderMapper orderMapper;

  public ReportServiceImpl(OrderMapper orderMapper) {
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
    List<LocalDate> dateList = this.makeBetweenDates(begin, end);
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
   * 生成指定日期范围内的日期列表
   *
   * @param startDate 开始日期
   * @param endDate   结束日期
   * @return 日期列表
   */
  private List<LocalDate> makeBetweenDates(LocalDate startDate, LocalDate endDate) {
    List<LocalDate> dates = new ArrayList<>();
    LocalDate currentDate = startDate;
    while (!currentDate.isAfter(endDate)) {
      dates.add(currentDate);
      currentDate = currentDate.plusDays(1);
    }
    return dates;
  }
}
