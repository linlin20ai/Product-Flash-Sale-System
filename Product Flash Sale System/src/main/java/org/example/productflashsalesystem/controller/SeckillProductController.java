package org.example.productflashsalesystem.controller;


import com.google.common.util.concurrent.RateLimiter;
import org.apache.ibatis.annotations.Param;
import org.example.productflashsalesystem.pojo.SeckillProduct;
import org.example.productflashsalesystem.service.OrderService;
import org.example.productflashsalesystem.service.SeckillProductRedisCacheService;
import org.example.productflashsalesystem.service.SeckillProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author 林圣涛
 */
@Controller
@RequestMapping("/Seckill")
@Cacheable("product")
public class SeckillProductController {

    @Autowired
    private SeckillProductService seckillProductService;

    @Autowired
    private SeckillProductRedisCacheService seckillProductRedisCacheService;

    @Autowired
    private OrderService orderService;

    private Integer ordersum;

    private static final Logger LOGGER= LoggerFactory.getLogger(SeckillProductController.class);

    //查询单个商品
    @RequestMapping(value = "/Product",method = RequestMethod.GET)
    @ResponseBody
    @CachePut(value = "SeckillProuct",key = "seckillProductImpl")
    public SeckillProduct SeckillProduct(@Param("id") Integer id){
        return seckillProductRedisCacheService.getSeckillProduct(id);
    }
    //查询全部商品
    @RequestMapping(value = "/Products",method = RequestMethod.GET)
    @ResponseBody
    public List<SeckillProduct> SeckillProducts(){
        return seckillProductService.getSeckillProducts();
    }

    //每一秒放行10个请求
    RateLimiter rateLimiter=RateLimiter.create(10);
    @RequestMapping(value = "/ProductOrder",method = RequestMethod.GET)
    @ResponseBody
     //下单商品
    public String productOrder(@Param("id") Integer id,@Param("userid") Integer userid,@Param("md5") String md5){
        return seckillProductService.productOrder(id,userid,md5);
    }


    //设置一个接口用于初始化商品（数据库和缓存中的数据）
    @RequestMapping(value = "/Initializeproduct",method = RequestMethod.POST)
    @ResponseBody
    public String initializeproduct(@Param("id")Integer id,@Param("sum")Integer sum){
        return seckillProductService.initializproduct(id,sum);
    }

    //生成md5值的方法
    //md5: key ys
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    @ResponseBody
    public String login(@Param("id") Integer id, @Param("userid") Integer userId){
        return seckillProductService.md5(id,userId);
    }
}
