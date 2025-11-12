package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.*;
import com.sky.service.SetmealService;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 获取今日营业数据
     * @param begin
     * @param end
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {

        /*
         * 1.查询总订单数
         * 2.查询总营业额
         * 3.查询有效订单数
         * 4.查询有效订单金额
         * 5.查询新增用户数
         * 6.查询总用户数
         * 7.查询总销量
         */
        Map map=new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        //查询总订单数
        Integer totalOrderCount = reportMapper.getOrdersByTime(map);
        map.put("status", Orders.COMPLETED);

        //营业额
        Double turnover = reportMapper.getTurnoverByTime(map);
        turnover = turnover == null ? 0.0 : turnover;

        //有效订单数
        Integer validOrderCount = orderMapper.countByMap(map);

        Double unitPrice=0.0;
        Double orderCompletionRate=0.0;
        if(validOrderCount!=0&&validOrderCount!=0){
            //单品销售总额
            orderCompletionRate=validOrderCount.doubleValue()/totalOrderCount;
            //平均客单价
            unitPrice=turnover/validOrderCount;
        }

        //新增用户数
        Integer newUsers = userMapper.countByMap(map);
        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }


    //订单接口设计

    @Override
    public OrderOverViewVO overViewOrders() {
        Map map=new HashMap();
        //待接单数量
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingOrders=orderMapper.countByMap(map);
        //待派送数量
        map.put("status", Orders.DELIVERY_IN_PROGRESS);
        Integer deliveredOrders=orderMapper.countByMap(map);
        //已完成数量
        map.put("status", Orders.COMPLETED);
        Integer completedOrders=orderMapper.countByMap(map);
        //已取消数量
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders=orderMapper.countByMap(map);
        //全部订单
        map.put("status", null);
        Integer allOrders=orderMapper.countByMap(map);
        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * 菜品总览接口
     * @return
     */
    @Override
    public DishOverViewVO overViewDishes() {
        Integer status=1;
        Integer sold=dishMapper.countStatus(status);
        status=0;
        Integer discontinued=dishMapper.countStatus(status);
        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    @Override
    public SetmealOverViewVO overviewSetmeals() {
        Integer status=1;
        Integer sold=setmealMapper.countStatus(status);
        status=0;
        Integer discontinued=setmealMapper.countStatus(status);
        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
