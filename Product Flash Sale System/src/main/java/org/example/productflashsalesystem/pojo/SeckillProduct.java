package org.example.productflashsalesystem.pojo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
/**
 * 秒杀商品实体类
 * Created by lin on 2024/4/18.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeckillProduct implements Serializable {
    private  int id;

    private String name;

    private String description;

    private Date starttime;

    private Date endtime;

    private double price;

    private int quantity;

    private String status;

    private int orderscount;

    private int version;

    public void setOrderscount(int orderscount) {
        this.orderscount = orderscount;
    }

    @Override
    public String toString() {
        return "SeckillProduct{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", price=" + price +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                ", orderscount=" + orderscount +
                ", version=" + version +
                '}';
    }

}
