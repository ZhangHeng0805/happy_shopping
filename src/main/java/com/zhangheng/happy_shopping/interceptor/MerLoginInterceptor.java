package com.zhangheng.happy_shopping.interceptor;


import com.zhangheng.happy_shopping.android.entity.Merchants;
import com.zhangheng.happy_shopping.android.entity.Store;
import com.zhangheng.happy_shopping.android.repository.MerchantsRepository;
import com.zhangheng.happy_shopping.android.repository.StoreRepository;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Optional;

/*商家登录拦截器*/
public class MerLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //登录检查
        HttpSession session =request.getSession();
        Object merchants = session.getAttribute("merchants");
        Object store = session.getAttribute("store");
        if (merchants!=null && store!=null){
            //放行
            return true;
        }
        Message msg = new Message();
        msg.setTime(TimeUtil.time(new Date()));
        msg.setCode(404);
        msg.setTitle("未登录");
        msg.setMessage("请先登录");
        request.setAttribute("msg",msg);
        request.getRequestDispatcher("/login_merchantsPage").forward(request,response);
        //拦截
        return false;
    }
}
