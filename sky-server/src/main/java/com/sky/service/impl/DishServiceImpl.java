package com.sky.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//新增菜品以及口味

@Service
@Slf4j
public class DishServiceImpl implements DishService {


    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Transactional//同时做两张表

    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish= new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);

        //获取insert语句生成的主键值
        Long dishId = dish.getId();
        //插入1条数据
        List<DishFlavor> flavor=dishDTO.getFlavors();
        if(flavor!=null&&flavor.size()>0){
            flavor.forEach(f->f.setDishId(dishId));
//            for (DishFlavor f:flavor) {
//                f.setDishId(dishId);
//            }
            dishFlavorMapper.insertBatch(flavor);
        }
        //向口味表插入n条数据
    }

    //菜品分页查询
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

//    批量删除菜品 @parm ids @return
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断菜品是否能够删除--是否有起售中的
        for (Long id : ids) {
            Dish dish=dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                //不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }

        }
        //当前菜品是否被套餐引用
        List<Long> setmealIds= setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds !=null  && setmealIds.size() > 0 ){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品表中的数据
//        for (Long id : ids) {
//            dishMapper.deleteByid(ids);
//            //删除菜品关联的口味数据
//            dishFlavorMapper.deleteByDishId(id);
//        }
        //优化的批量删除

        //根据菜品id集合批量删除菜品数据
        //sql:delete from dish where id in (?,?,?)
        dishMapper.deleteByIds(ids);


        //根据菜品id集合批量删除菜品口味数据
        // sql:delete from dish_flavor where dish_id in (?,?,?)/\
        dishFlavorMapper.deleteByDishIds(ids);


    }

    //根据id查询菜品和对应的口味数据
    @Override
    public DishVO getByIdWithFlavors(Long id) {
        //根据id查询菜品数据
        Dish dish= dishMapper.getById(id);
        List<DishFlavor> dishFlavors=dishFlavorMapper.getByDishId(id);
        //查询菜品口味数据
        DishVO dishVO= new DishVO();
        //将查询到的结果封装到VO中
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        //返回controller
        return dishVO;
    }

    //修改菜品
    //@parm dishDTO
    //@return
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils .copyProperties(dishDTO,dish);
        //修改菜品表基本信息

        dishMapper.update(dish);

        //删除原有的口味表
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        List<DishFlavor> flavors=dishDTO.getFlavors();
        if(flavors!=null && flavors.size()>0){
            flavors.forEach(dishFlavor->dishFlavor.setDishId(dishDTO.getId()));
        }



        //重新插入口味数据
        dishFlavorMapper.insertBatch(flavors);
    }
    
    /**
     * 菜品起售停售
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }
}
