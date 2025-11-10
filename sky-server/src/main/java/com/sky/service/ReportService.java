package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface ReportService {
    //营业额数据统计
    TurnoverReportVO getTurnover(LocalDate begin, LocalDate end);
    //用户数据统计
    UserReportVO getUser(LocalDate begin, LocalDate end);
    //订单数据统计
    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);
}
