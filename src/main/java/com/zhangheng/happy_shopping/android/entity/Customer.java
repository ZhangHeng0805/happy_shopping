package com.zhangheng.happy_shopping.android.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*顾客信息*/
@Table(name = "customer")
@Entity
@Data
public class Customer {
    @Id
    private String phone;//手机号
    @Column
    private String username;//用户名
    @Column
    private String password;//密码
    @Column
    private String sex;//性别
    @Column
    private String icon;//头像图标
    @Column
    private String address;//地址
    @Column
    private String time;//注册时间
    @Column
    private int state;//账号状态[0:正常 1:封禁]

    public static final String[] accont_state={"正常","封禁"};
    //重置的初始密码
    public static final String reset_pwd="123456";

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
