package org.example.productflashsalesystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
/**
 * @author 林圣涛
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    private int id;

    private Date ordertime;

    private int productid;

    private String productname;

    private String ordernumber;
}
