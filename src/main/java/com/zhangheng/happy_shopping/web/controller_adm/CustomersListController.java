package com.zhangheng.happy_shopping.web.controller_adm;

import com.zhangheng.happy_shopping.android.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 顾客管理界面
 * @author 张恒
 * @program: happy_shopping
 * @email zhangheng.0805@qq.com
 * @date 2022-02-21 17:36
 */
@Controller
public class CustomersListController {
    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/customersListPage")
    private String customersListPage(Model model){
        model.addAttribute("active",3);
        return "administrator/customersList";
    }
}
