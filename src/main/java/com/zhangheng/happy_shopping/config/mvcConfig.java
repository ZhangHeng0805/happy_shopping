package com.zhangheng.happy_shopping.config;

import com.zhangheng.happy_shopping.interceptor.AdminLoginInterceptor;
import com.zhangheng.happy_shopping.interceptor.MerLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class mvcConfig implements WebMvcConfigurer {

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MerLoginInterceptor())
                .addPathPatterns("/main","/findGoodsNumByType","/findGoodsNumByOrderState",
                        "/count_price","/findGoodnumBytimeAndtype","/un_goodsOrder","/Ok_Order","/No_Order"
                ,"/OderListPage","/OrderGoodsByState"
                        ,"/addGoodsPage","/addGoods"
                ,"/myGoodsListpage","/findGoodsByType","/updata_GoodsState","/delete_Goods","/update_goods"
                ,"/modify_account","/modify_accountPage","/modify_password");

        registry.addInterceptor(new AdminLoginInterceptor())
                .addPathPatterns("/home","/get_count","/get_order","/getGoodsNum_ByState"
                        , "/handle_goodsPage","/get_sta2Goods","/setState_sta2Goods"
                ,"/merchantsListPage","/getMerchantsList","/set_merchantsState"
                ,"/customersListPage");
    }
}
