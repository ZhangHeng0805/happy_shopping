package com.zhangheng.happy_shopping.android.entity.submitgoods;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/*顾客提交订单信息*/
@Data
@Entity
@Table(name = "submit_goodslist")
public class SubmitGoods {
    @Id
    private String submit_id;//
    @Column(name = "[name]")
    private String name;
    @Column(name = "[phone]")
    private String phone;
    @Column(name = "[address]")
    private String address;
    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name = "list_id",referencedColumnName = "[submit_id]")
    private List<goods> goods_list;
    @Column(name = "[count_price]")
    private double count_price;
    @Column(name = "[time]")
    private String time;


    public String getSubmit_id() {
        return submit_id;
    }

    public void setSubmit_id(String submit_id) {
        this.submit_id = submit_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<goods> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<goods> goods_list) {
        this.goods_list = goods_list;
    }

    public double getCount_price() {
        return count_price;
    }

    public void setCount_price(double count_price) {
        this.count_price = count_price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SubmitGoods{" +
                "submit_id='" + submit_id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", goods_list=" + goods_list +
                ", count_price=" + count_price +
                ", time='" + time + '\'' +
                '}';
    }
}
