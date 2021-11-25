package com.zhangheng.happy_shopping.web.controller.data.main;

import lombok.Data;

//不同时间和订单类型的商品数
@Data
public class goodsnumBytimeAndtype {
    private String time;//时间
    private int sta0;//待处理的商品数
    private int sta1;//拒绝发货的商品数
    private int sta2;//待收货的商品数
    private int sta3;//已收货的商品数
    private int sta4;//退货商品数
}
