package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service//创建一个服务类,意思是创建对象，然后注入到这个类中，最后才能被Spring管理
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired //意思是创建对象，并注入到这个类中
    private SetmealMapper setmealMapper;

    //@parm setmealDTO，dto是数据传输对象，vo是视图对象 @return
    @Transactional //transactional 是一个事务注解, 表示这个方法需要开启一个事务
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        //向套餐表插入数据
        setmealMapper.insert(setmeal);

        //获取插入套餐的id
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes=setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish->{
            setmealDish.setSetmealId(setmealId);
        });
        //保存套餐和菜品的关联关系
        setmealMapper.insertBatch(setmealDishes);
    }

    //套餐分页查询
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        int pageNum=setmealPageQueryDTO.getPage();//调用setmealPageQueryDTO的getPage方法
        int pageSize=setmealPageQueryDTO.getPageSize();//调用setmealPageQueryDTO的getPageSize方法
        PageHelper.startPage(pageNum,pageSize);//设置分页参数,PageHelper意思是分页助手，设置分页参数

        Page<SetmealVO> page=setmealMapper.pageQuery(setmealPageQueryDTO);
        //setmealMapper是创建的，是SetmealMapper接口的实现类，setmealVO是创建的，是视图对象
        //实现类需要调用创建的视图对象，所以需要创建一个视图对象

        return new PageResult(page.getTotal(),page.getResult());
        //getTotal是获取总记录数，getResult是获取结果,是此前创建的视图对象
    }
}