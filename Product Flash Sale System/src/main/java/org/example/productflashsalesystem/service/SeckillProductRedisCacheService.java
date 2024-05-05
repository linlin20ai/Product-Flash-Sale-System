package org.example.productflashsalesystem.service;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.jdbc.Null;
import org.example.productflashsalesystem.pojo.SeckillProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeckillProductRedisCacheService {
    @Autowired
    RedisService redisService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void setSeckillProduct(int id) {
        String sql="SELECT id, name, description, start_time as starttime, end_time as endtime, price, quantity, status, orders_count as orderscount FROM Seckill_Products WHERE id =  ?";
        SeckillProduct seckillProduct=jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(SeckillProduct.class),id);
        if(seckillProduct!=null){
            redisService.setSeckillProduct(String.valueOf(id),seckillProduct);
        }
    }

    public SeckillProduct getSeckillProduct(int id){
        SeckillProduct seckillProduct=redisService.getSeckillProduct(String.valueOf(id));
        return redisService.getSeckillProduct(String.valueOf(id));
    }

    public void setSeckillProducts(){
        String sql="SELECT id, name, description, start_time as starttime, end_time as endtime, price, quantity, status, orders_count as orderscount  FROM Seckill_Products";
        List<SeckillProduct> seckillProducts=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(SeckillProduct.class));
        List<SeckillProduct> seckillProducts1=new ArrayList<>();
        if(!seckillProducts.isEmpty()){
            for (int i = 0; i <seckillProducts.size() ; i++) {
                int id=seckillProducts.get(i).getId();
                String sql2="SELECT id, name, description, start_time as starttime, end_time as endtime, price, quantity, status, orders_count as orderscount FROM Seckill_Products WHERE id =  ?";
                SeckillProduct seckillProduct=jdbcTemplate.queryForObject(sql2,new BeanPropertyRowMapper<>(SeckillProduct.class),id);
                seckillProducts1.add(seckillProduct);
            }
            redisService.setSeckillProductsForList(seckillProducts1);
        }
    }

    public List<SeckillProduct> getSeckillProducts(){
        List<SeckillProduct> seckillProducts=new ArrayList<>();
        seckillProducts=redisService.getSckillProducts();
        return seckillProducts;
    }


    public boolean setSeckillProduct(int id,SeckillProduct seckillProduct){
        if(id>10||seckillProduct==null){
            return false;
        }
        redisService.update(seckillProduct, String.valueOf(id));
        return true;
    }

    public void setMd5(String hashkey,String key){
        redisService.setMd5(hashkey,key);
    }

    public String getMd5(String hashkey){
        return redisService.getMd5(hashkey);
    }

}
