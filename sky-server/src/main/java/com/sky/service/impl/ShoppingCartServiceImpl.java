package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    /**
     * 添加到购物车
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前菜品或套餐是否在购物车中
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        //把用户id也传入
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //根据userId查找购物车
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //如果此数据在购物车已经存在，则数量加一
        if(list != null && list.size() > 0){
            //查询到的只有0或一条数据
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            //update shopping_cart set number = ? where id = ?
            shoppingCartMapper.updateNumberById(cart);
        }else {
            //如果不存在，则需要插入一条数据，且需要判断此数据是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null){
                //说明此数据是菜品，添加到购物车
                Dish dish = dishMapper.getById(dishId);
                //添加到购物车
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else{
                //说明此数据是套餐，添加到购物车
                Long setmealId = shoppingCart.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                //添加到购物车
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            //插入数据
            shoppingCartMapper.insert(shoppingCart);

        }

    }
}
