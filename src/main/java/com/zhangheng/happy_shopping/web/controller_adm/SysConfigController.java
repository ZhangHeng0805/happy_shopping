package com.zhangheng.happy_shopping.web.controller_adm;

import com.zhangheng.happy_shopping.android.entity.ChatConfig;
import com.zhangheng.happy_shopping.android.entity.Store;
import com.zhangheng.happy_shopping.android.repository.ChatConfigRep;
import com.zhangheng.happy_shopping.android.repository.StoreRepository;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import com.zhangheng.util.FormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author 张恒
 * @program: happy_shopping
 * @email zhangheng.0805@qq.com
 * @date 2022-02-25 9:29
 */
@Controller
public class SysConfigController {
    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    private ChatConfigRep chatConfigRep;

    /**
     * 跳转至页面
     * @param model
     * @return
     */
    @GetMapping("/sysConfigPage")
    private String customersListPage(Model model){
        model.addAttribute("active",7);
        return "administrator/sysConfig";
    }

    /**
     * 获取聊天配置数据
     * @return
     */
    @ResponseBody
    @PostMapping("/getChatConfig")
    private List<ChatConfig> getChatConfig(){
        List<ChatConfig> list = new ArrayList<>();
        List<ChatConfig> all = chatConfigRep.findAll();
        list=all;
        return list;
    }

    /**
     * 修改聊天服务配置
     * @param id
     * @param ip
     * @param port
     * @param localPort
     * @param explain
     * @return
     */
    @ResponseBody
    @PostMapping("/set_ChatConfig")
    private Message set_ChatConfig(Integer id,@Nullable String ip
            ,@Nullable String port,@Nullable String localPort,@Nullable String explain){
        Message msg = new Message();
        msg.setTime(TimeUtil.time(new Date()));
        if (ip!=null&&ip.length()>0||
                port!=null&&port.length()>0||
                localPort!=null&&localPort.length()>0||
                explain!=null&&explain.length()>0){
            Optional<ChatConfig> byId = chatConfigRep.findById(id);
            if (byId.isPresent()){
                if (ip!=null&&ip.length()>0){
                    if (FormatUtil.isIP(ip)) {
                        byId.get().setIp(ip);
                    }
                }else if (port!=null&&port.length()>0){
                    if (FormatUtil.isPort(port)) {
                        byId.get().setPort(port);
                    }
                }else if (localPort!=null&&localPort.length()>0){
                    if (FormatUtil.isPort(localPort)) {
                        byId.get().setLocalPort(localPort);
                    }
                }else if (explain!=null&&explain.length()>0){
                    byId.get().setExplain(explain);
                }
                ChatConfig chatConfig = chatConfigRep.saveAndFlush(byId.get());
                if (chatConfig!=null){
                    msg.setCode(200);
                    msg.setMessage("聊天服务配置信息更新成功");
                }else {
                    msg.setCode(500);
                    msg.setMessage("聊天服务配置信息更新失败");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("聊天服务配置信息不存在");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("接收数据不能为空");
        }
        return msg;
    }

    @ResponseBody
    @PostMapping("/add_ChatConfig")
    private Message add_ChatConfig(@Nullable String ip
            ,@Nullable String port,@Nullable String localPort,@Nullable String explain){
        Message msg = new Message();
        msg.setTime(TimeUtil.time(new Date()));
        if (ip!=null&&port!=null&&localPort!=null&&explain!=null){
            if (FormatUtil.isIP(ip)){
                if (FormatUtil.isPort(port)){
                    if (FormatUtil.isPort(localPort)){
                        ChatConfig chatConfig = new ChatConfig();
                        chatConfig.setIp(ip);
                        chatConfig.setPort(port);
                        chatConfig.setLocalPort(localPort);
                        chatConfig.setExplain(explain);
                        ChatConfig chatConfig1 = chatConfigRep.saveAndFlush(chatConfig);
                        if (chatConfig1!=null&&chatConfig1.getId()>0){
                            msg.setCode(200);
                            msg.setMessage("新增聊天服务配置成功");
                        }else {
                            msg.setCode(500);
                            msg.setMessage("新增聊天服务配置失败");
                        }
                    }else {
                        msg.setCode(500);
                        msg.setMessage("本地端口号格式错误");
                    }
                }else {
                    msg.setCode(500);
                    msg.setMessage("服务器端口号格式错误");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("ip地址格式错误");
            }
        }else {
         msg.setCode(500);
         msg.setMessage("新增信息不能有空值");
        }
        return msg;
    }

}
