package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PutMapping("/delivery/{id}")
    @ApiOperation("确认接单")
    public Result delivery(@PathVariable("id") long id){
        orderService.delivery(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("确认完成")
    public Result complete(@PathVariable("id") long id){
        orderService.complete(id);
        return Result.success();
    }
}
