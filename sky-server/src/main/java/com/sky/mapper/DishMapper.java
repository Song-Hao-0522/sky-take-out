package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param id
     * @return
     */
    @Select("select count(1) from dish where category_id = #{id}")
    int countByCategoryId(Integer id);

    /**
     * 插入菜品数据
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void add(Dish dish);
}
