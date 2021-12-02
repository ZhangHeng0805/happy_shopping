package com.zhangheng.happy_shopping.web.controller_mer;

import com.zhangheng.happy_shopping.bean.VerificationCode;
import com.zhangheng.happy_shopping.controller.FileLoadController;
import com.zhangheng.happy_shopping.utils.FiletypeUtil;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 用于测试的
 */
@Controller
public class TestController {


    private Logger log= LoggerFactory.getLogger(getClass());
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
    @GetMapping("/3")
    private String iamge_cutPage(){
        return "test/test3";
    }

    @ResponseBody
    @PostMapping("/3")
    private Message image_upload(String img){
        Message msg = new Message();
        String s = new FileLoadController().base64ToImg(img, TimeUtil.timeTip(new Date()).substring(5, 12) , "测试图片");
        log.info("图片："+s);
        if (s!=null){
            msg.setCode(200);
            msg.setMessage("图片上传成功！"+s);
        }else {
            msg.setCode(500);
            msg.setMessage("图片上传失败！");
        }
        return msg;
    }
}
