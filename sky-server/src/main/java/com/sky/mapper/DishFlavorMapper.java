package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface DishFlavorMapper {

    /**
     * 插入口味列表
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 删除菜品口味
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteFlavorByDishId(Long dishId);

    /**
     * 通过dishId查看口味
     * @param dishId dishId
     * @return
     */
    @Select("select * from dish_flavor where id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
