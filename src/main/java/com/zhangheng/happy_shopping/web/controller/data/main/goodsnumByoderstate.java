package com.zhangheng.happy_shopping.web.controller.data.main;

import lombok.Data;

/*不同订单状态的商品数*/
@Data
public class goodsnumByoderstate {
    private int state;//订单状态[0:未处理；1:拒绝发货；2:待收货；3:已收货；4:退货]
    private int num;//商品数量
}
