package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
//    @Scheduled(cron = "0 * * * * ?")  //每分钟触发一次
    @Scheduled(cron = "1/5 * * * * ?")
    public void processTimeoutOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        //select * from orders where status = #{status} and order_time < (当前时间 - 15)
        //订单状态为待付款状态，时间为当前时间 - 15min
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);

        //遍历orderList，赋值给order的订单取消信息
        if(ordersList != null && ordersList.size() > 0){
            for(Orders orders : ordersList){
                orders.setStatus(Orders.CANCELLED);  //状态设置为已取消
                orders.setCancelReason("订单付款时间超时，自动取消");  //订单取消原因
                orders.setCancelTime(LocalDateTime.now());  //当前时间
                orderMapper.update(orders);  //更新此订单
            }
        }
    }

    /**
     * 处理一直处于派送中状态的订单
     */
//    @Scheduled(cron = "0 0 1 * * ?") //凌晨一点触发
    @Scheduled(cron = "0/5 * * * * ?")
    public void processDeliveryOrder(){
        log.info("凌晨一点处理前一天还处于派送中状态的订单: {}", LocalDateTime.now());

        //昨天0:00的订单时间
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);

        //获取状态为派送中、时间为前一天的订单
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        //将这些订单状态设置为已完成并且更新orders数据库
        if(ordersList != null && ordersList.size() > 0){
            for(Orders orders : ordersList){
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }



}
