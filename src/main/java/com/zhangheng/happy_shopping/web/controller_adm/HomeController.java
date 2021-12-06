package com.zhangheng.happy_shopping.web.controller_adm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*系统主页*/
@Controller
public class HomeController {
    private Logger log= LoggerFactory.getLogger(getClass());

    @GetMapping("/home")
    private String homePage(Model model){
        model.addAttribute("active",0);
        return "administrator/home_adm";
    }
}
