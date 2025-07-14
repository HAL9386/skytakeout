package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.InputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
  private final OrderMapper orderMapper;
  private final UserMapper userMapper;
  private final WorkspaceService workspaceService;

  public ReportServiceImpl(OrderMapper orderMapper, UserMapper userMapper, WorkspaceService workspaceService) {
    this.userMapper = userMapper;
    this.orderMapper = orderMapper;
    this.workspaceService = workspaceService;
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
   * 订单统计
   *
   * @param begin 开始时间
   * @param end   结束时间
   * @return 订单统计
   */
  @Override
  public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
    List<LocalDate> dateList = this.makeBetweenDatesStep1Day(begin, end);
    List<Integer> orderCountList = new ArrayList<>();
    List<Integer> validOrderCountList = new ArrayList<>();
    Map<String, Object> queryCondition = this.makeQueryCondition(begin, end, null);
    Integer totalOrderCount = orderMapper.countByMap(queryCondition);
    queryCondition = this.makeQueryCondition(begin, end, Orders.COMPLETED);
    Integer totalValidOrderCount = orderMapper.countByMap(queryCondition);
    for (LocalDate date : dateList) {
      queryCondition = this.makeQueryCondition(date, date, null);
      Integer orderCount = orderMapper.countByMap(queryCondition);
      queryCondition.put("status", Orders.COMPLETED);
      Integer validOrderCount = orderMapper.countByMap(queryCondition);
      orderCountList.add(orderCount);
      validOrderCountList.add(validOrderCount);
    }
    return OrderReportVO.builder()
      .dateList(StringUtils.join(dateList, ","))
      .orderCountList(StringUtils.join(orderCountList, ","))
      .validOrderCountList(StringUtils.join(validOrderCountList, ","))
      .totalOrderCount(totalOrderCount)
      .validOrderCount(totalValidOrderCount)
      .orderCompletionRate(totalValidOrderCount.doubleValue() / totalOrderCount)
      .build();
  }

  /**
   * 销量排名
   *
   * @param begin 开始时间
   * @param end   结束时间
   * @return 销量排名
   */
  @Override
  public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
    LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
    LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
    List<GoodsSalesDTO> top10List = orderMapper.getSalesTop10(beginTime, endTime);
    String nameList = StringUtils.join(top10List.stream().map(GoodsSalesDTO::getName).toList(), ",");
    String numberList = StringUtils.join(top10List.stream().map(GoodsSalesDTO::getNumber).toList(), ",");
    return SalesTop10ReportVO.builder()
     .nameList(nameList)
     .numberList(numberList)
     .build();
  }

  /**
   * 根据模板导出运营数据报表
   *
   * @param response 响应
   */
  @Override
  public void exportBusinessData(HttpServletResponse response) {
    // 查询概览运营数据，提供给Excel模板文件
    LocalDate begin = LocalDate.now().minusDays(30);
    LocalDate end = LocalDate.now().minusDays(1);
    BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);

    try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/businessDataTemplate.xlsx")) {
      // 检查模板文件是否存在
      Objects.requireNonNull(in, "Excel模板文件未找到");

      // 基于提供好的模板文件创建一个新的Excel表格对象
      XSSFWorkbook excel = new XSSFWorkbook(in);
      // 获得Excel文件中的一个Sheet页
      XSSFSheet sheet = excel.getSheet("Sheet1");

      // 第二行第二列填入时间范围
      sheet.getRow(1).getCell(1).setCellValue(begin + "至" + end);

      // 获得第4行
      XSSFRow row = sheet.getRow(3);
      // 第4行对应概览数据
      row.getCell(2).setCellValue(businessDataVO.getTurnover());
      row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
      row.getCell(6).setCellValue(businessDataVO.getNewUsers());

      // 获得第5行
      row = sheet.getRow(4);
      row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
      row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

      // 明细数据部分，按每天为一个单位，填入对应营业数据
      for (int i = 0; i < 30; i++) {
        LocalDate date = begin.plusDays(i);
        // 准备明细数据，查询指定日期的营业数据
        BusinessDataVO businessData = workspaceService.getBusinessData(date, date);

        // 获得某一行
        row = sheet.getRow(7 + i);
        row.getCell(1).setCellValue(date.toString());
        row.getCell(2).setCellValue(businessData.getTurnover());
        row.getCell(3).setCellValue(businessData.getValidOrderCount());
        row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
        row.getCell(5).setCellValue(businessData.getUnitPrice());
        row.getCell(6).setCellValue(businessData.getNewUsers());
      }

      // 通过输出流将文件下载到客户端浏览器中
      ServletOutputStream out = response.getOutputStream();
      response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("运营数据报表.xlsx", StandardCharsets.UTF_8));

      excel.write(out);
      excel.close();
      out.flush();
      out.close();
    } catch (IOException e) {
      log.error("导出运营数据报表失败", e);
    }
  }

  private Map<String, Object> makeQueryCondition(LocalDate begin, LocalDate end, Integer status) {
    Map<String, Object> queryCondition = new HashMap<>();
    LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
    LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
    queryCondition.put("begin", beginTime);
    queryCondition.put("end", endTime);
    queryCondition.put("status", status);
    return queryCondition;
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
