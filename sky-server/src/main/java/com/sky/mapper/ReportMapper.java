package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;
@Mapper
public interface ReportMapper {
    /**
     * 根据动态条件统计营业额数据
     * @param map
     * @return
     */
    Double sumByMap(Map map);
    //统计用户数量
    Integer getUsersByTime(Map map);
    //订单统计接口
    Integer getOrderByTime(Map map);
}
