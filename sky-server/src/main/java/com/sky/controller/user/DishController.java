package com.sky.controller.user;

import com.alibaba.fastjson.JSON;
import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.sky.constant.RedisConstants.*;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId 分类id
     * @return 菜品列表
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        //1，获取redis中的key
        String key = DISH_KEY+categoryId;


        //2.查询redis看是否缓存有数据
        String json = stringRedisTemplate.opsForValue().get(key);
        if(json!=null&&!json.isEmpty()){
            List<DishVO> dishList= JSON.parseArray(json,DishVO.class);
            return Result.success(dishList);
        }
        //缓存中没有数据
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品
        List<DishVO> list = dishService.listWithFlavor(dish);

        //写入Redis
        String jsonString = JSON.toJSONString(list!=null?list:new ArrayList<>());
        long expireTime = (list==null||list.isEmpty()?BLANK_MESSAGE_EXPIRE_TIME:DISH_EXPIRE_TIME);

        //缓存数据并设置数据的有效期
        stringRedisTemplate.opsForValue().set(key,jsonString,expireTime, TimeUnit.MINUTES);
        //防止list.size()为空
        log.info("菜品数据已缓存，category:{},数量:{},过期时间:{}",categoryId,list!=null?list.size():0,expireTime);

        return Result.success(list);
    }

}
