package com.zhangheng.happy_shopping.android.entity;

/*商品订单信息*/
public class GoodsOrder {

    private String submit_id;//订单号
    private String name;//收货人
    private String phone;//联系电话
    private String address;//收货地址
    private String time;//订单时间
    private String goods_name;//商品名
    private String state;//订单状态
    private Double goods_price;//商品价格
    private int num;//商品数量
    private Integer goods_id;//商品id

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(Double goods_price) {
        this.goods_price = goods_price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }
}
