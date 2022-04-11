package com.zhangheng.happy_shopping;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.zhangheng.bean.Const;
import com.zhangheng.bean.Message;
import com.zhangheng.happy_shopping.android.entity.submitgoods.SubmitGoods;
import com.zhangheng.happy_shopping.android.repository.GoodsRepository;
import com.zhangheng.happy_shopping.android.repository.SubmitGoodsRepository;
import com.zhangheng.happy_shopping.bean.VerificationCode;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import com.zhangheng.happy_shopping.web.service.EmailService;
import com.zhangheng.log.printLog.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
class HappyShoppingApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    private SubmitGoodsRepository submitGoodsRepository;
    @Test
    void contextLoads() {
        test6();
    }
    private void test(){
        String sql="select * from verification_code";
        List<VerificationCode> verificationCodes = jdbcTemplate.query(sql, new Object[]{},new BeanPropertyRowMapper<VerificationCode>(VerificationCode.class));
        for (VerificationCode v:verificationCodes) {
            System.out.println(v.toString());
        }
    }
    private void test1(){
        Integer f = goodsRepository.countByStore_idAndGoods_type(1, "其他");
        System.out.println(f);
    }
    private void test2(){
        List<SubmitGoods> byTimeLike = submitGoodsRepository.findByTimeLike("2021年11月20日");
        for (SubmitGoods sb : byTimeLike) {
            System.out.println(sb.toString());
        }
    }
    private void test3(){
        String t="2021年11月21日 22:42:58";
        String s = TimeUtil.fewDaysAgo(t, 2);
        System.out.println(s.substring(0,11));
    }
    int count=0;
    private int a(int i){
        count++;
        if (i==0){
            return 1;
        }else if (i==1){
            return 2;
        }else {
            return a(i-1)+a(i-2);
        }
    }

    private void test4(){
        String s="data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBA";
        String[] split = s.split(",");
        String type=split[0].split("/")[1].split(";")[0];
        System.out.println(type);
    }
    private void test5(){
        Log.Info("123");
    }

    private void test6(){

    }

}
