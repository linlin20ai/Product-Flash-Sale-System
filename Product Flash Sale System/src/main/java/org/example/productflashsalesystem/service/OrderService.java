package org.example.productflashsalesystem.service;

import org.example.productflashsalesystem.pojo.Order;
import org.example.productflashsalesystem.pojo.OrderImpl;
import org.example.productflashsalesystem.pojo.SeckillProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderImpl order;

    //新建订单
    public String addOrder(SeckillProduct seckillProduct){
        return order.addOrder(seckillProduct);
    }

    public Order getOrder(int id){return order.getOrder(id);
    }

}
