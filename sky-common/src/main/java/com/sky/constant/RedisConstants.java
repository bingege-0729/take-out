package com.sky.constant;

public class RedisConstants {
    public static final String DISH_KEY = "dish:";
    public static final String CATEGORY_KEY = "category:";
    public static final String CUSTOMER_ADDR = "customer:addressBook";
    public static final String LOCK_SHOPPING_PREFIX = "lock:shoppingCart:";
    public static final String LOCK_PAY_PREFIX = "lock:pay:";


    //时间
    public static final long CATEGORY_EXPIRE_TIME = 30L;
    public static final long DISH_EXPIRE_TIME = 30L;
    public static final long BLANK_MESSAGE_EXPIRE_TIME = 5L;
    public static final long CUSTOMER_ADDR_EXPIRE_TIME = 30L;
    public static final long LOCK_SHOPPING_EXPIRE_TIME = 10L;
    public static final long LOCK_PAY_EXPIRE_TIME = 30L;
}
