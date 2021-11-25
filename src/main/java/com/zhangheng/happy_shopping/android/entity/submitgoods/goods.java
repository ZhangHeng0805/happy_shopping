package com.zhangheng.happy_shopping.android.entity.submitgoods;

import lombok.Data;

import javax.persistence.*;

/*提交的商品信息*/
@Data
@Entity
@Table(name = "goods_list")
public class goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "[goods_id]")
    private Integer goods_id;
    @Column(name = "[store_id]")
    private Integer store_id;
    @Column(name = "[goods_name]")
    private String goods_name;
    @Column(name = "[goods_price]")
    private Double goods_price;
    @Column(name = "[num]")
    private Integer num;
    @Column(name = "[state]")
    private int state;//订单状态[0:未处理；1:拒绝发货；2:待收货；3:已收货；4:退货]
    @Column(name = "list_id")
    private String list_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStore_id() {
        return store_id;
    }

    public void setStore_id(Integer store_id) {
        this.store_id = store_id;
    }

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

    public Double getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(Double goods_price) {
        this.goods_price = goods_price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    @Override
    public String toString() {
        return "goods{" +
                "id=" + id +
                ", goods_id=" + goods_id +
                ", store_id=" + store_id +
                ", goods_name='" + goods_name + '\'' +
                ", goods_price=" + goods_price +
                ", num=" + num +
                ", list_id='" + list_id + '\'' +
                '}';
    }
}
