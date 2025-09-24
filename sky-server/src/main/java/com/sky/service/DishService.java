package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;

import java.util.List;

public interface DishService {
    //新增菜品方法以及口味
    public void saveWithFlavor(DishDTO dishDTO);


    //菜品分页查询
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    //批量删除菜品 @parm ids
    void deleteBatch(List<Long> ids);

    //根据分类id查询菜品数据
    List<Dish> list(Long categoryId);
}
