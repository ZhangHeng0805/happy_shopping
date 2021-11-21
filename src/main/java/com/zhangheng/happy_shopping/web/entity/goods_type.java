package com.zhangheng.happy_shopping.web.entity;

import lombok.Data;

import javax.persistence.*;

/*商品类型*/
@Data
@Entity
@Table
public class goods_type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//类别id
    @Column
    private String type;//类别名称
}
