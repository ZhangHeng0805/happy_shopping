package com.zhangheng.happy_shopping.web.controller_mer.data.main;

import lombok.Data;
//本店的订单商品
@Data
public class listgoodsByState {
    private String order_id;//订单号
    private String time;//时间
    private String username;//收货人
    private String tel;//联系电话
    private String address;//收货地址
    private String goods_name;//商品名称
    private int goods_id;//商品id
    private int id_goods;//订单中的商品id
    private double price;//单价
    private int num;//数量
    private Double all_price;//总价
    private int state;//状态
}
