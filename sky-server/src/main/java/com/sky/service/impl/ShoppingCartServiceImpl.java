package com.sky.service.impl;

import com.sky.config.SimpleRedisLock;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.sky.constant.RedisConstants.*;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {


    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SimpleRedisLock lock;
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断是否存在于购物车
        ShoppingCart shoppingCart = new ShoppingCart();

        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        Long userId = BaseContext.getCurrentId();

        shoppingCart.setUserId(userId);
        //查询购物车中数据
        shoppingCartMapper.list(shoppingCart);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //在购物车进行加减商品时，会出现顾客多次点击导致计算错误的问题,所以通过加互斥锁避免购物车加减错误


        try {
            //获取锁
            boolean success = lock.tryLock(LOCK_SHOPPING_PREFIX, String.valueOf(userId), LOCK_SHOPPING_EXPIRE_TIME, TimeUnit.MINUTES);
            //如果已经存在则加一
            if(success){
                if (list != null && list.size() > 0) {
                    ShoppingCart cart = list.get(0);
                    cart.setNumber(cart.getNumber() + 1);//实体实例化了，所以可以直接修改
                    shoppingCartMapper.updateNumberById(cart);//cart和shoppingCartDTO是同一个对象，所以直接修改
                } else {//如果不存在，则插入一条数据到购物车

                    //判断本次是菜品还是套餐
                    Long dishId = shoppingCartDTO.getDishId();

                    if (dishId != null) {
                        //本次添加到购物车的是菜品
                        Dish dish = dishMapper.getById(dishId);
                        shoppingCart.setName(dish.getName());
                        shoppingCart.setImage(dish.getImage());
                        shoppingCart.setAmount(dish.getPrice());

                    } else {
                        //本次添加到购物车是套餐
                        Long setmealId = shoppingCartDTO.getSetmealId();
                        Setmeal setmeal = setmealMapper.getById(setmealId);
                        shoppingCart.setName(setmeal.getName());
                        shoppingCart.setImage(setmeal.getImage());
                        shoppingCart.setAmount(setmeal.getPrice());

                    }
                    //统一添加
                    shoppingCart.setNumber(1);
                    shoppingCart.setCreateTime(LocalDateTime.now());
                    shoppingCartMapper.insert(shoppingCart);
                }
            }
        }finally{
            //释放锁
            lock.unlock(LOCK_SHOPPING_PREFIX);
        }
    }
    //查看购物车
    @Override
    public List<ShoppingCart> showShoppingCart() {
        //获取用户id
        long userId=BaseContext.getCurrentId();
        ShoppingCart shoppingCart=ShoppingCart.builder()
                        .userId(userId)
                        .build();
        List<ShoppingCart> list=shoppingCartMapper.list(shoppingCart);
        return list;
    }

    @Override
    public void cleanShoppingCart() {
        log.info("清空购物车");
        shoppingCartMapper.cleanShoppingCart(BaseContext.getCurrentId());
    }

    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //设置查询条件，查询当前用户登入的购物车数据
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if(list!=null&&list.size()>0){
            shoppingCart=list.get(0);
            Integer number=shoppingCart.getNumber();
            if(number==1){
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }else{
                shoppingCart.setNumber(shoppingCart.getNumber()-1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }
    }
}
