package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.apache.poi.ss.formula.functions.Address;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    //用户下单

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;


    //用户下单
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        //异常：地址簿为空+购物车为空
        AddressBook addressBook=addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if(addressBook==null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        ShoppingCart shoppingCart=new ShoppingCart();

        //购物车为空
        Long userId=BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList=shoppingCartMapper.list(shoppingCart);
        if(shoppingCartList==null||shoppingCartList.size()==0){
            //抛出业务异常
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //向订单表插入1条数据
        Orders order=new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,order);
        order.setOrderTime(LocalDateTime.now());
        order.setPayStatus(Orders.UN_PAID);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setPhone(addressBook.getPhone());
        order.setConsignee(addressBook.getConsignee());
        order.setUserId(userId);


        orderMapper.insert(order);



        List<OrderDetail> orderDetailList=new ArrayList<>();
        //向订单明细表插入n条数据
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail=new OrderDetail();//订单明细
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(order.getId());//设置当前订单明细关联的订单id
            orderDetailList.add(orderDetail);
        }

        orderDetailMapper.insertBatch(orderDetailList);
        //清空用户购物车
        shoppingCartMapper.deleteById(userId);
        //封装VO返回结果
        OrderSubmitVO orderSubmitVO=OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .build();
        return orderSubmitVO;
    }
}
