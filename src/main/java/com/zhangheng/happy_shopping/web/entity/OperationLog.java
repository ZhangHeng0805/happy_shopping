package com.zhangheng.happy_shopping.web.entity;

import lombok.Data;

import javax.persistence.*;

//操作日志
@Entity
@Table(name = "operation_log")
@Data
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String tel;//操作者的账号
    @Column
    private String time;//操作时间
    @Column
    private String request;//操作的request信息(ip和User-Agent)
    @Column
    private int count;//操作次数
    @Column
    private int type;//操作类型(0商家登录;1管理员登录;2顾客登录,3邮箱验证)
    @Column
    private String info;//操作信息

}
