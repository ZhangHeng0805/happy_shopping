package com.zhangheng.happy_shopping.android.entity;

import javax.persistence.*;

/*
* 聊天服务器配置信息
* */
@Entity
@Table(name = "chatconfig")
public class ChatConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "[ip]")
    private String ip;//服务器IP地址
    @Column(name = "[port]")
    private String port;//服务器端口号
    @Column(name = "[local_port]")
    private String localPort;//本地端口号
    @Column(name = "[explain]")
    private String explain;//配置说明

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLocalPort() {
        return localPort;
    }

    public void setLocalPort(String localPort) {
        this.localPort = localPort;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    @Override
    public String toString() {
        return "ChatConfig{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", localPort='" + localPort + '\'' +
                ", explain='" + explain + '\'' +
                '}';
    }
}
