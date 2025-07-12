package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Api(tags = "统计报表相关接口")
@Slf4j
@RequestMapping("/admin/report")
@RestController
public class ReportController {
  private final ReportService reportService;

  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  /**
   * 营业额数据统计
   *
   * @param begin 开始时间
   * @param end   结束时间
   * @return 营业额数据统计
   */
  @ApiOperation("营业额数据统计")
  @GetMapping("/turnoverStatistics")
  public Result<TurnoverReportVO> turnoverStatistics(
    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
    log.info("营业额数据统计，参数：开始时间：{}，结束时间：{}", begin, end);
    TurnoverReportVO turnoverReportVO = reportService.getTurnover(begin, end);
    return Result.success(turnoverReportVO);
  }

  /**
   * 用户数据统计
   *
   * @param begin 开始时间
   * @param end   结束时间
   * @return 用户数据统计
   */
  @ApiOperation("用户数据统计")
  @GetMapping("/userStatistics")
  public Result<UserReportVO> userStatistics(
    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
    log.info("用户数据统计，参数：开始时间：{}，结束时间：{}", begin, end);
    UserReportVO userReportVO = reportService.getUserStatistics(begin, end);
    return Result.success(userReportVO);
  }
}
