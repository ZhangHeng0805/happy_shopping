package com.zhangheng.happy_shopping.web.controller.data.main;

import lombok.Data;

/*不同商品类型的数量*/
@Data
public class goodsnumBygoodstype {
    private String type;//商品类型
    private int num;//商品数量
}
