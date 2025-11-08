package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;



public interface OrderService {
    //提交订单
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
    //付款订单
   // OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO);
}
