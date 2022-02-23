package com.zhangheng.happy_shopping.android.controller;

import com.zhangheng.happy_shopping.android.entity.ChatConfig;
import com.zhangheng.happy_shopping.android.entity.PhoneInfo;
import com.zhangheng.happy_shopping.android.repository.ChatConfigRep;
import com.zhangheng.happy_shopping.utils.CusAccessObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/*
* Android配置
* */
@RequestMapping("config")
@RestController
public class ConfigController {

    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private ChatConfigRep chatConfigRepository;

    /**
     * 聊天服务器的IP地址和端口号
     * @param port:本地端口号
     * @return ChatConfig对象
     */
    @GetMapping("chatconfig")
    public ChatConfig chatConfig(@RequestParam("port")String port, HttpServletRequest request){
        ChatConfig chatConfig = new ChatConfig();
        Optional<ChatConfig> chatConfigByLocalPort = chatConfigRepository.findChatConfigByLocalPort(port);
        if (chatConfigByLocalPort.isPresent()){
            chatConfig=chatConfigByLocalPort.get();
        }else {
            log.error("chatconfig错误：本地端口"+port+"查询为空");
        }

        String requst = CusAccessObjectUtil.getRequst(request);
        log.info("获取聊天室配置的请求："+ requst);
        return chatConfig;
    }
}
