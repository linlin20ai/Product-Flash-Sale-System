package org.example.productflashsalesystem.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 林圣涛
 */
@Repository
public class SeckillProductImpl {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public SeckillProduct getSeckillProduct(int id) {
        String sql="SELECT id, name, description, start_time as starttime, end_time as endtime, price, quantity, status, orders_count as orderscount ,version FROM Seckill_Products WHERE id =  ?";
        SeckillProduct seckillProduct=jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(SeckillProduct.class),id);
        return seckillProduct;
    }


    public List<SeckillProduct> getSeckillProduces() {
        String sql="SELECT id, name, description, start_time as starttime, end_time as endtime, price, quantity, status, orders_count as orderscount ,version FROM Seckill_Products";
        List<SeckillProduct> seckillProducts=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(SeckillProduct.class));
        return seckillProducts;
    }


    public int Initializeproduct(int id) {
        String sql="UPDATE Seckill_Products " +
                "SET orders_count = 0 , version=0 " +
                "WHERE id = ?";
        jdbcTemplate.update(sql,id);
        return  1;
    }

    public int checkProduct(int id){
        String sql="select orders_count from Seckill_Products where id=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id);
    }

    public int updateProduct(int id,int sum,int version){
        String sql=" update Seckill_Products set\n" +
                "        orders_count = orders_count+1 , version=version+1 " +
                "        where\n" +
                "          id= ? and version= ?";
       return jdbcTemplate.update(sql,id,version);
    }

    public int checkProductVersion(int id){
        String sql="select version from Seckill_Products where id=?";
        return jdbcTemplate.queryForObject(sql,Integer.class,id);
    }

}
