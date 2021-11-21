package com.zhangheng.happy_shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class HappyShoppingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HappyShoppingApplication.class, args);
    }

}
