package com.zhangheng.happy_shopping.config;

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
                ,"/myGoodsListpage","/findGoodsByType"
                ,"/modify_accountPage");
    }
}
