package com.zhangheng.happy_shopping.web.controller_adm.bean;

import com.zhangheng.happy_shopping.android.entity.Customer;
import lombok.Data;

/**
 * 顾客信息扩增类
 * @author 张恒
 * @program: happy_shopping
 * @email zhangheng.0805@qq.com
 * @date 2022-02-22 10:37
 */
@Data
public class CustomerInfo  extends Customer {

    private int orderNum;//所有订单数
    private double orderPrice;//所有订单金额之和

}
