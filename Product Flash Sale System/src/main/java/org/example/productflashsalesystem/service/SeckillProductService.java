package org.example.productflashsalesystem.service;

import org.apache.ibatis.annotations.Param;
import org.example.productflashsalesystem.pojo.SeckillProduct;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SeckillProductService  {

      List<SeckillProduct> getSeckillProducts();

      String productOrder( Integer id,  Integer userid, String md5);

      String initializproduct(Integer id,Integer sum);

      String md5(Integer id,Integer userid);

      String setM5(Integer id, Integer userId);
}
