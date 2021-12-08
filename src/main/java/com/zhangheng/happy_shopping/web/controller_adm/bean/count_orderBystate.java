package com.zhangheng.happy_shopping.web.controller_adm.bean;

import lombok.Data;
/*根据订单商品的状态统计商品数量(系统主页中的环形图数据)*/
@Data
public class count_orderBystate {
    private int value;
    private String label;
    private String formatted;
}
