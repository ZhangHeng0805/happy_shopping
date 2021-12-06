package com.zhangheng.happy_shopping.android.entity;

import lombok.Data;

import javax.persistence.*;

/*
*
* 商品信息
* */
@Data
@Entity
@Table(name = "goods")
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer goods_id;//商品id
    @Column
    private String goods_name;//名称
    @Column
    private String goods_image;//图片地址
    @Column
    private String goods_introduction;//介绍
    @Column
    private String goods_type;//类型
    @Column
    private Integer goods_sales;//销量
    @Column
    private Integer goods_num;//库存
    @Column
    private double goods_price;//价格
    @Column
    private String store_name;//店名
    @Column
    private Integer store_id;//店铺id
    @Column
    private String time;//更改时间
    @Column
    private Integer state;//商品状态(0:上线；1:下线 2:审核中 3:商品违规)

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public String getGoods_introduction() {
        return goods_introduction;
    }

    public void setGoods_introduction(String goods_introduction) {
        this.goods_introduction = goods_introduction;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public Integer getGoods_sales() {


        return goods_sales;
    }

    public void setGoods_sales(Integer goods_sales) {
        this.goods_sales = goods_sales;
    }

    public Integer getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(Integer goods_num) {
        this.goods_num = goods_num;
    }

    public double getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(double goods_price) {
        this.goods_price = goods_price;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public Integer getStore_id() {
        return store_id;
    }

    public void setStore_id(Integer store_id) {
        this.store_id = store_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

}
