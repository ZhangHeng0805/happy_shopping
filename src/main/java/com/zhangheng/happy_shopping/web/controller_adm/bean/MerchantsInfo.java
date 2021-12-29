package com.zhangheng.happy_shopping.web.controller_adm.bean;

import lombok.Data;

/*商家列表界面商家信息*/
@Data
public class MerchantsInfo {
    private String store_image;//店铺图片
    private String store_name;//店铺名称
    private int store_id;//店铺id
    private String mer_name;//商家姓名
    private String tel;//商家电话
    private String email;//商家邮箱
    private String store_address;//店铺地址
    private String store_introduce;
    private double turnover;//营业额
    private String time;//注册时间
    private int state;//账号状态[0:正常,1:违规封禁。2:账号异常]
}
