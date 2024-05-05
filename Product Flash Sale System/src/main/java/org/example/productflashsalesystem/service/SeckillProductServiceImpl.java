package org.example.productflashsalesystem.service;

import com.google.common.util.concurrent.RateLimiter;
import org.example.productflashsalesystem.pojo.SeckillProduct;
import org.example.productflashsalesystem.pojo.SeckillProductImpl;
import org.example.productflashsalesystem.pojo.User;
import org.example.productflashsalesystem.pojo.UserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 林圣涛
 */
@Service
public class SeckillProductServiceImpl implements SeckillProductService{
   @Autowired
   private SeckillProductImpl seckillProductImpl;

   @Autowired
   private SeckillProductRedisCacheService seckillProductRedisCacheService;

   @Autowired
   private UserImpl userImpl;

   @Autowired
   private OrderService orderService;


   private Integer ordersum=60;



    @Override
    public List<SeckillProduct> getSeckillProducts() {
        List<SeckillProduct> seckillProducts=seckillProductRedisCacheService.getSeckillProducts();
        if(seckillProducts!=null){
            return seckillProducts;
        }
        seckillProductRedisCacheService.setSeckillProducts();
        return seckillProductImpl.getSeckillProduces();
    }

    //用户秒杀下单接口
    //每一秒放行10个接口
    RateLimiter rateLimiter=RateLimiter.create(10);
    private static final Logger LOGGER= LoggerFactory.getLogger(SeckillProductServiceImpl.class);
    @Override
    public String productOrder(Integer id, Integer userid, String md5) {
        if(!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)){
            LOGGER.warn("秒杀失败！！");
            return "购买失败！！请刷新后重试！！";
        }
        try{
            System.out.println("秒杀商品的id为："+id);
            //md5验证
            String hashkey="KEY_"+userid+id;
            //检验userid 用户是否存在
            User user=userImpl.findById(userid);
            if(user==null){
                throw new RuntimeException("用户信息不存在");
            }
            //校验商品id
            SeckillProduct seckillProduct1=seckillProductImpl.getSeckillProduct(id);
            if(seckillProduct1==null){
                throw new RuntimeException("商品信息不存在");
            }
            String tureM5=seckillProductRedisCacheService.getMd5(hashkey);
            if(tureM5==null){
                return "用户信息有问题";
            }
            if(!md5.equals(tureM5)){
                return "用户信息问题";
            }
            //根据订单id查询库存和版本
            int sum=seckillProductImpl.checkProduct(id);
            int version=seckillProductImpl.checkProductVersion(id);
            if(sum<ordersum&&version==sum){
                //更新数据库
                int updaterows=seckillProductImpl.updateProduct(id,sum,version);
                if(updaterows==0){
                    throw new RuntimeException("抢购失败，请重试！！");
                }
                //更新数据库
                SeckillProduct killproduct=seckillProductImpl.getSeckillProduct(id);
                seckillProductRedisCacheService.setSeckillProduct(id,killproduct);
                //生成订单
                return "秒杀成功，订单号为："+orderService.addOrder(killproduct);
            }
            return "秒杀失败，谢谢参与";
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    //给管理员用于初始化秒杀商品
    @Override
    public String initializproduct(Integer id, Integer sum) {
        //校验数据库商品是否存在
        SeckillProduct seckillProduct=seckillProductImpl.getSeckillProduct(id);
        if(seckillProduct==null&&sum<0){
            return "您初始化的信息有问题，请检查后再尝试";
        }
        seckillProductRedisCacheService.setSeckillProduct(id);
        //改变订单数
        ordersum=sum;
        int flag=seckillProductImpl.Initializeproduct(id);
        if(flag!=1){
            return "系统有点小出错！";
        }
        //格式化缓存
        seckillProductRedisCacheService.setSeckillProduct(id,seckillProductImpl.getSeckillProduct(id));
        return "初始化完成，秒杀活动将按照约定时间结束！";
    }

    @Override
    public String md5(Integer id, Integer userid) {
        String md5;
        try{
            md5=setM5(id,userid);
        }catch (Exception e){
            e.printStackTrace();
            return "系统出了点小差错！稍后再试！";
        }
        return "验证成功！！请快去秒杀吧";
    }


    //生成MD5签名
    @Override
    public String setM5(Integer id, Integer userId) {
        //检验userid 用户是否存在
        User user=userImpl.findById(userId);
        if(user==null){
            throw new RuntimeException("用户信息不存在");
        }
        //校验商品id
        SeckillProduct seckillProduct1=seckillProductImpl.getSeckillProduct(id);
        if(seckillProduct1==null){
            throw new RuntimeException("商品信息不存在");
        }
        String hashKey="KEY_"+userId+id;
        String key= DigestUtils.md5DigestAsHex((userId+id+"linshengtao").getBytes());
        seckillProductRedisCacheService.setMd5(hashKey,key);
        return key;
    }
}
