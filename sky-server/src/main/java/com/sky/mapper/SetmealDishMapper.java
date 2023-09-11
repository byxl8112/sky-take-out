package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据dishId查看setmeal_dish 表中的数据
     * @param dishIds
     * @return
     */
    List<Long> getSetmealDishByDishId(List<Long> dishIds);
}
