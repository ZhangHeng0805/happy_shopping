package com.zhangheng.happy_shopping.bean;

/**
 * @author 张恒
 * @program: happy_shopping
 * @email zhangheng.0805@qq.com
 * @date 2022-04-19 14:52
 */

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * 配置文件中的设置
 */
@Component
@ConfigurationProperties(prefix = "setting")
@Validated
@Data
public class Setting {
    @Min(3)
    @Max(10)
    private Integer max_count;//最大重试机会（登录，邮箱验证码）
    @Min(1)
    @Max(1440)
    private Integer wait_time;//间隔等待时间（登录，邮箱验证码）
    @NotNull
    @Length(min = 6,max = 18)
    private String customer_reset_pwd;//顾客重置的初始密码
    @NotNull
    @Length(min = 6,max = 18)
    private String merchants_reset_pwd;//商家重置的初始密码

}
