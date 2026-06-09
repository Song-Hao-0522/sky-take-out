package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询当前菜品关联的套餐数量
     * @param id
     * @return
     */
    @Select("select count(1) from setmeal_dish where dish_id = #{id}")
    int countByDishId(Long id);

    void add(List<SetmealDish> setmealDishes);

    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> findSetmealDishById(Long id);

    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId}")
    void deleteBySetmealId(Long setmealId);
}
