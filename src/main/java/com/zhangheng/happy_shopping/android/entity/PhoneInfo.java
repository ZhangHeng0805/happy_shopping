package com.zhangheng.happy_shopping.android.entity;

import com.zhangheng.happy_shopping.utils.TimeUtil;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 手机信息类
 * @author 张恒
 * @program: happy_shopping
 * @email zhangheng.0805@qq.com
 * @date 2022-02-23 15:10
 */
@Entity
@Table(name = "[phone_info]")
@Data
public class PhoneInfo {
    @Id
    @Column(name = "[id]",length = 100)
    private String id;//设备ID
    @Column(name = "[ip]",length = 255)
    private String ip;//设备IP地址
    @Column(name = "[model]",length = 255)
    private String model;//设备型号
    @Column(name = "[sdk]",columnDefinition = "int",length = 5)
    private Integer sdk;//sdk版本
    @Column(name = "[release]",length = 255)
    private String release;//android版本
    @Column(name = "[app_version]",length = 255)
    private String app_version;//app型号
    @Column(name = "[tel]",length = 15)
    private String tel;//手机号
    @Column(name = "[tel_type]",length = 20)
    private String tel_type;//手机运营商
    @Column(name = "[notice]",length = 255)
    private String notice;//提示信息
    @Column(name = "[last_time]",length = 255)
    private String last_time;//最近一次访问时间

    /**
     * 解析requst为手机信息对象
     * @param requst
     * @return
     */
    public static PhoneInfo UserAgentToPhoneInfo(String requst){
        String[] info={"model:","sdk:","release:","Appversion:","tel:","ID:"};
        PhoneInfo pi = new PhoneInfo();
//应用更新查询:127.0.0.1 / (model:M2104K10AC) (sdk:30) (release:Android11) (Appversion:HappyShopping V2-2.23) (tel:[China Mobile]null) (ID:18fdc81fffa6884e)
        String[] split2 = requst.split(" / ");
        String ip=split2[0];
        String Phone=split2[1];
        pi.setIp(ip);
        String model=Phone.substring(Phone.indexOf(info[0])+info[0].length(),Phone.indexOf(") ("+info[1]));
        pi.setModel(model);
        int sdk=Integer.valueOf(Phone.substring(Phone.indexOf(info[1])+info[1].length(),Phone.indexOf(") ("+info[2])));
        pi.setSdk(sdk);
        String release=Phone.substring(Phone.indexOf(info[2])+info[2].length(),Phone.indexOf(") ("+info[3]));
        pi.setRelease(release);
        String Appversion=Phone.substring(Phone.indexOf(info[3])+info[3].length(),Phone.indexOf(") ("+info[4]));
        pi.setApp_version(Appversion);
        String Tel=Phone.substring(Phone.indexOf(info[4])+info[4].length(),Phone.indexOf(") ("+info[5]));
        String[] Telsplit = Tel.split("]");
        if (!Telsplit[1].equals("null")){
            pi.setTel(Telsplit[1]);
        }
        pi.setTel_type(Telsplit[0].substring(1));
        String id=Phone.substring(Phone.indexOf(info[5])+info[5].length(),Phone.lastIndexOf(")"));
        pi.setId(id);
        pi.setLast_time(TimeUtil.time(new Date()));
        return pi;
    }
}
