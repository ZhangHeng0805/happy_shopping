package com.zhangheng.happy_shopping.web.controller_adm.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 管理员账号
 */


@Component
@ConfigurationProperties(prefix = "login-admin")
@Data
public class Login_admin {
    private String account;
    private String password;
}
