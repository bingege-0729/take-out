package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface WorkSpaceService {
    /**
     * 统计今日营业数据
     * @param begin
     * @param end
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);


    /**
     * 订单管理接口设计
     * @return
     */
    OrderOverViewVO overViewOrders();
    /**
     * 菜品总览接口设计
     * @return
     */
    DishOverViewVO overViewDishes();

    /**
     * 套餐总览接口设计
     * @return
     */
    SetmealOverViewVO overviewSetmeals();
}
