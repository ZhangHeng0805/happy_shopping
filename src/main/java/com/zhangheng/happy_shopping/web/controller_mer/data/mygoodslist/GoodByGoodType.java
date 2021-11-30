package com.zhangheng.happy_shopping.web.controller_mer.data.mygoodslist;

import lombok.Data;
/*根据商品类型查询商品信息*/
@Data
public class GoodByGoodType {
    private int id;//商品id
    private String goods_name;//商品名称
    private String goods_image;//商品图片
    private String goods_type;//商品类型
    private String goods_introduction;//商品简介
    private int goods_sales;//商品销量
    private int goods_num;//商品库存
    private double goods_price;//商品价格
    private int state;//商品状态
}
