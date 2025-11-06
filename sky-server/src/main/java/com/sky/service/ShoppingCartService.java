package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.service.impl.ShoppingCartServiceImpl;

import java.util.List;

public interface ShoppingCartService{
    //添加购物车
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
    //查看购物车
    List<ShoppingCart> showShoppingCart();
    //清空购物车
    void cleanShoppingCart();
    //删除其中一个商品
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
