package com.zhangheng.happy_shopping;

import com.zhangheng.happy_shopping.android.repository.GoodsRepository;
import com.zhangheng.happy_shopping.bean.VerificationCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
class HappyShoppingApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    GoodsRepository goodsRepository;
    @Test
    void contextLoads() {
        test1();
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

}
