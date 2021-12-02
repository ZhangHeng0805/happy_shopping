package com.zhangheng.happy_shopping.android.entity;

import lombok.Data;

import javax.persistence.*;

/*
* 店铺信息
* */
@Data
@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer store_id;//店铺id
    @Column
    private String store_name;//店家名称
    @Column
    private String store_image;//店家图片
    @Column
    private String boss_name;//店长姓名
    @Column
    private String start_time;//开店时间
    @Column
    private String store_introduce;//店铺介绍介绍

    public Integer getStore_id() {
        return store_id;
    }

    public void setStore_id(Integer store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_image() {
        return store_image;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }

    public String getBoss_name() {
        return boss_name;
    }

    public void setBoss_name(String boss_name) {
        this.boss_name = boss_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStore_introduce() {
        return store_introduce;
    }

    public void setStore_introduce(String store_introduce) {
        this.store_introduce = store_introduce;
    }
}
