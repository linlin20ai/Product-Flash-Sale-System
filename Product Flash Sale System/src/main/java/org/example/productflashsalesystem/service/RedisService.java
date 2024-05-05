package org.example.productflashsalesystem.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.example.productflashsalesystem.pojo.SeckillProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    void setSeckillProduct(String key, SeckillProduct value){
        redisTemplate.opsForValue().set(key,value);
    }

    SeckillProduct getSeckillProduct(String key){
       Object object= redisTemplate.opsForValue().get(key);
       if (object instanceof SeckillProduct) {
            return (SeckillProduct) object;
       }
       else{
           return null;
       }
    }

    void setSeckillProductsForList(List<SeckillProduct> seckillProducts){
        String jsonstring=JSON.toJSONString(seckillProducts);
        redisTemplate.opsForValue().set("SeckillProductsList",jsonstring);
    }
    List<SeckillProduct> getSckillProducts(){
        Boolean flag = redisTemplate.hasKey("SeckillProductsList");
        List<SeckillProduct> seckillProducts=null;
        if(Boolean.TRUE.equals(flag)){
            String json= (String) redisTemplate.opsForValue().get("SeckillProductsList");
            if(json!=null&&!json.isEmpty()){
                seckillProducts=  JSON.parseObject(json,new TypeReference<List<SeckillProduct>>(){});
            }
        }
        return  seckillProducts;
    }

    // 更新数据
    public void update(SeckillProduct seckillProduct,String id) {
        redisTemplate.opsForValue().set(id,seckillProduct);
    }

    public void setMd5(String hashkey,String key){
        redisTemplate.opsForValue().set(hashkey,key,240, TimeUnit.SECONDS);
    }

    public String getMd5(String hashkey){
        return (String) redisTemplate.opsForValue().get(hashkey);
    }
}
