package com.sky.controller.user;

import com.alibaba.fastjson.JSON;
import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.sky.constant.RedisConstants.*;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "C端-分类接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询分类")
    public Result<List<Category>> list(Integer type) {
        //1.进行redis缓存修改
        String key = CATEGORY_KEY+type;
        //2.先查redis缓存是否有菜品分类
        String json= stringRedisTemplate.opsForValue().get(key);

        //2.1有，直接返回数据
        if(json!=null){
            return Result.success(JSON.parseArray(json,Category.class));
        }

        //2.2没有，查数据库
        List<Category> list = categoryService.list(type);
        List<Category> result = list != null ? list : new ArrayList<>();

        //2.2.1有查到数据，保存到redis缓存中，并返回
        //2.2.2没有查到数据，返回空信息
        stringRedisTemplate.opsForValue().set(
                key,
                JSON.toJSONString(result),
                result.isEmpty()?BLANK_MESSAGE_EXPIRE_TIME:CATEGORY_EXPIRE_TIME,
                TimeUnit.MINUTES
        );

        return Result.success(result);
    }
}
