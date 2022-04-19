package com.zhangheng.happy_shopping.web.controller_adm.bean;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;


/**
 * 管理员账号
 */


@Component
@ConfigurationProperties(prefix = "login-admin")
@Validated
@Data
public class Login_admin {
    @NotNull
    @Length(min = 8,max = 20)
    private String account;//管理员账号
    @NotNull
    @Length(min = 6,max = 18)
    private String password;//管理员密码
    @Email
    private String email;//管理员邮箱
}
