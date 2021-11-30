package com.zhangheng.happy_shopping.web.controller_mer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/*商家修改账号控制器*/
@Controller
public class modifyAccountController {

    @GetMapping("/modify_accountPage")
    private String modify_accountPage(){
        return "merchants/modifyAccount_mer";
    }
}
