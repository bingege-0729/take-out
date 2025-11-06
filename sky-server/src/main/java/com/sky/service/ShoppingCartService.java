package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.service.impl.ShoppingCartServiceImpl;

import java.util.List;

public interface ShoppingCartService{
    //添加购物车
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
    //展示购物车
    List<ShoppingCart> showShoppingCart();

    void cleanShoppingCart();

    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
