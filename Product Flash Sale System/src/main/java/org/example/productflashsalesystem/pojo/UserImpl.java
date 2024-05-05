package org.example.productflashsalesystem.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author 林圣涛
 */
@Repository
public class UserImpl {
    @Autowired
    JdbcTemplate jdbcTemplate;

    //根据id查用户
    public User findById(int id){
        String sql="select id,name,password from user where id = ?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(User.class),id);
    }
}
