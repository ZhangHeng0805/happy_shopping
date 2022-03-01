package com.zhangheng.happy_shopping.web.controller_mer.data.main;

import lombok.Data;

/*不同商品类型的数量*/
@Data
public class goodsnumBygoodstype {
    private int id;//商品类型id
    private String type;//商品类型
    private int num;//商品数量
}
