package com.sky.mapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    //菜品id查询 @parm dish_id

    //select setmeal_id from setmeal_dish where dish_id in (?,?,?)
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);


}
