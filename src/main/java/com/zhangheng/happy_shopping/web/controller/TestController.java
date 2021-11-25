package com.zhangheng.happy_shopping.web.controller;

import com.zhangheng.happy_shopping.bean.VerificationCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {


    @GetMapping("/")
    private String testPage(){
        return "redirect:/login_merchantsPage" ;
    }

    @ResponseBody
    @GetMapping("/1")
    private String test1Get(){
        return "test1的Get请求" ;
    }
    @ResponseBody
    @PostMapping("/1")
    private String test1Post(){
        return "test1的Post请求" ;
    }
    @ResponseBody
    @PutMapping("/1")
    private VerificationCode test1Put() throws InterruptedException {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setId("123");
        verificationCode.setCode("456");
        Thread.sleep(1500);
        return verificationCode ;
    }

    @ResponseBody
    @GetMapping("/2")
    private String test2Get(VerificationCode verificationCode){
        System.out.println(verificationCode.toString());
        return "test2的Get请求:"+verificationCode.getId() ;
    }
    @ResponseBody
    @PostMapping("/2")
    private String test2Post(VerificationCode verificationCode){
        System.out.println(verificationCode.toString());
        return "test2的Post请求:"+verificationCode.getId() ;
    }

    @GetMapping("/0")
    private String testStylePage(){
        return "test/styleTest";
    }
}
