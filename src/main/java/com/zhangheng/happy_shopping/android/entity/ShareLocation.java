package com.zhangheng.happy_shopping.android.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*位置共享信息*/
@Entity
@Table(name = "location")
public class ShareLocation {
    @Id
    private String phone;//手机号
    @Column
    private String username;//姓名
    @Column
    private String latitude;//经度
    @Column
    private String longitude;//纬度
    @Column
    private String time;//时间
    @Column
    private String state;//状态

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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ShareLocation{" +
                "phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", time='" + time + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
