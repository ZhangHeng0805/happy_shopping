package com.zhangheng.happy_shopping.web.controller_mer.data.main;

import lombok.Data;

//不同时间和订单类型的商品数
@Data
public class goodsnumBytimeAndtype {
    private String time;//时间
    private int sta0=0;//待处理的商品数
    private int sta1=0;//拒绝发货的商品数
    private int sta2=0;//待收货的商品数
    private int sta3=0;//已收货的商品数
    private int sta4=0;//退货商品数
    private Double cont_price;//当日收益
}
