package org.example.productflashsalesystem.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * @author 林圣涛
 */
@Repository
public class OrderImpl {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public String addOrder(SeckillProduct seckillProduct){
        int id=seckillProduct.getId();
        String name=seckillProduct.getName();
        //随机生成订单号
        String ordernumber= UUID.randomUUID().toString().replace("-","").substring(0,8);

        //获取当时时间
        Date date=new Date();
        Timestamp orderTime=new Timestamp(date.getTime());

        String sql="INSERT INTO orders (order_time, product_id, product_name, order_number) VALUES(?,?,?,?)";
        jdbcTemplate.update(sql,orderTime,id,name,ordernumber);
        return ordernumber;
    }

    public Order getOrder(int id){
        String sql="SELECT id,order_time as ordertime, product_id as productid , product_name as productname, order_number as ordernumber FROM orders WHERE id =  ?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Order.class),id);
    }
}
