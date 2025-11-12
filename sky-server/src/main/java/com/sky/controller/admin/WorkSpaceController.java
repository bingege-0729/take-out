package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.*;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@Slf4j
@Api(tags = "工作台接口")
@RequestMapping("/admin/workspace")
public class WorkSpaceController {

    @Autowired
    private WorkSpaceService workSpaceService;
    @GetMapping("/businessData")
    public Result<BusinessDataVO>businessData(){
        //获得当天开始时间
        LocalDateTime begin=LocalDateTime.now().with(LocalTime.MIN);
        //获得当天结束时间
        LocalDateTime end=LocalDateTime.now().with(LocalTime.MAX);
        BusinessDataVO businessDataVO=workSpaceService.getBusinessData(begin,end);
        return Result.success(businessDataVO);
    }

    //订单管理
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> overViewOrders(){
        OrderOverViewVO orderOverViewVO=workSpaceService.overViewOrders();
        return Result.success(orderOverViewVO);
    }
    //菜品总览接口
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> overViewDishes(){
        DishOverViewVO dishOverViewVO=workSpaceService.overViewDishes();
        return Result.success(dishOverViewVO);
    }

    /**
     * 套餐总览接口
     * @return
     */
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> overviewSetmeals(){
        SetmealOverViewVO setmealOverViewVO=workSpaceService.overviewSetmeals();
        return Result.success(setmealOverViewVO);
    }

}
