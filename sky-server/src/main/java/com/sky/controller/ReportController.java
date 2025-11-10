package com.sky.controller;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * 报表
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "统计报表相关接口")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 营业额数据统计
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额数据统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end) {
        return Result.success(reportService.getTurnover(begin, end));
    }


    //用户统计
    @GetMapping("userStatistics")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern="yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern="yyyy-MM-dd")
            LocalDate end){
        return Result.success(reportService.getUser(begin,end));
    }

    //订单统计
    @GetMapping("ordersStatistics")
    public Result<OrderReportVO> orderReportVOResult(
            @DateTimeFormat(pattern="yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern="yyyy-MM-dd")
            LocalDate end){
        log.info("订单统计接口:{},{}",begin,end);
        OrderReportVO orderReportVO = reportService.ordersStatistics(begin,end);
            return Result.success(orderReportVO);
    }



}
