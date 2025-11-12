package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

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
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern="yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern="yyyy-MM-dd")
            LocalDate end){
        return Result.success(reportService.getUser(begin,end));
    }

    //订单统计
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> orderReportVOResult(
            @DateTimeFormat(pattern="yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern="yyyy-MM-dd")
            LocalDate end){
        log.info("订单统计接口:{},{}",begin,end);
        OrderReportVO orderReportVO = reportService.ordersStatistics(begin,end);
            return Result.success(orderReportVO);
    }
    //销量排名Top10
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate  end){
        log.info("销量排名top10:{},{}",begin,end);
        SalesTop10ReportVO salesTop10ReportVO = reportService.top10(begin,end);
        return Result.success(salesTop10ReportVO);

    }

    /**
     * 导出Excel报表
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        reportService.exportBusinessData(response);
    }

}
