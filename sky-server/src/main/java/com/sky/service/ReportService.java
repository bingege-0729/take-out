package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@Service
public interface ReportService {
    //营业额数据统计
    TurnoverReportVO getTurnover(LocalDate begin, LocalDate end);
    //用户数据统计
    UserReportVO getUser(LocalDate begin, LocalDate end);
    //订单数据统计
    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);
    //销量top10排名
    SalesTop10ReportVO top10(LocalDate begin, LocalDate end);
    //导出营业数据
    void exportBusinessData(HttpServletResponse response) throws IOException;
}
