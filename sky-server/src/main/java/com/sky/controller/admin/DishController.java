package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.exception.ParamException;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@RestController
@Slf4j
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result add(@RequestBody DishDTO dishDTO) {
        if (dishDTO == null){
            throw new ParamException(MessageConstant.PARAM_ERROR);
        }
        log.info("添加菜品：{}", dishDTO);
        dishService.add(dishDTO);
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        if (dishPageQueryDTO == null){
            throw new ParamException(MessageConstant.PARAM_ERROR);
        }
        log.info("菜品分页查询：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @ApiOperation("批量删除菜品")
    @DeleteMapping
    public Result delete(@RequestParam("ids") List<Long> ids) {
        log.info("批量删除菜品：{}", ids);
        if(ids== null|| ids.isEmpty()){
            throw new ParamException(MessageConstant.PARAM_ERROR);
        }
        dishService.delete(ids);
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 根据id查询菜品和对应的口味数据
     * @param id
     * @return
     */
    @ApiOperation("根据id查询菜品和对应的口味数据")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品信息，id为：{}", id);
        if (id == null){
            throw new ParamException(MessageConstant.PARAM_ERROR);
        }
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @ApiOperation("修改菜品")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        if (dishDTO == null){
            throw new ParamException(MessageConstant.PARAM_ERROR);
        }
        dishService.update(dishDTO);
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 起售、停售菜品
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("起售、停售菜品")
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("起售、停售菜品：{},{}",status,id);
        if (id == null || status == null){
            throw new ParamException(MessageConstant.PARAM_ERROR);
        }
        dishService.startOrStop(status, id);
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Dish>> list(@RequestParam Long categoryId) {
        log.info("根据id查询分类：{}", categoryId);
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 清理缓存数据
     * @param pattern
     */
    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
