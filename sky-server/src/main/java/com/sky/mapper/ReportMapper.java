package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
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
    //商品销量top10统计
    List<GoodsSalesDTO> getSalesTop10(Map map);

    /**
     * 根据动态条件统计营业额数据
     * @param map
     * @return
     */
    Double getTurnoverByTime(Map map);
    /**
     * 根据动态条件统计订单数据
     * @param map
     * @return
     */
    Integer getOrdersByTime(Map map);
}
