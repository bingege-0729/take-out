package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    //新增菜品方法以及口味
    public void saveWithFlavor(DishDTO dishDTO);


    //菜品分页查询
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    //批量删除菜品 @parm ids
    void deleteBatch(List<Long> ids);

    //根据id查询菜品 @parm id @return
    DishVO getByIdWithFlavors(Long id);
    //根据id修改菜品基本信息和口味信息
    void updateWithFlavor(DishDTO dishDTO);
    
    /**
     * 菜品起售停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}