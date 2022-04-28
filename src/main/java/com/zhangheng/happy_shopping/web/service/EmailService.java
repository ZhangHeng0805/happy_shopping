package com.zhangheng.happy_shopping.web.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.zhangheng.bean.Const;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import com.zhangheng.happy_shopping.web.controller_adm.bean.Login_admin;
import com.zhangheng.util.FormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 张恒
 * @program: happy_shopping
 * @email zhangheng.0805@qq.com
 * @date 2022-04-11 9:48
 */
@Service
public class EmailService {
    private Logger log= LoggerFactory.getLogger(getClass());
    private MailAccount account=Const.getMailAccount();
    @Autowired
    private Login_admin login_admin;
    /**
     * 商家发送验证码
     * @param email 商家邮箱
     * @param code
     */
    public void merSendCode(String email,String code){
        StringBuffer str=new StringBuffer();
        str.append("<h1>乐在购物网</h1>");
        str.append("<p>尊敬的"+email.split("@")[0]+"，您好!</p>");
        str.append("<p>乐在购物系统-商家平台，您正在进行邮箱验证，</p>");
        str.append("<p>验证码：<strong>"+code+"</strong></p>");
        str.append("<p>请注意不要泄露验证码，验证码有效时间"+LoginService.Wait_Time+"分钟。</p>");
        MailUtil.send(account,CollUtil.newArrayList(email),"乐在购物系统商家邮箱验证",str.toString(),true);
        log.info("发送邮件：{}，商家邮箱验证",email);
    }

    /**
     * 商家登录警告
     * @param email 商家邮箱
     * @param request 登录设备信息
     * @param phone 商家手机号
     */
    public void loginsendWarn(String email, String request,String phone){
        StringBuffer str=new StringBuffer();
        str.append("<h1>乐在购物网</h1>");
        str.append("<p>尊敬的"+email.split("@")[0]+"，您好!</p>");
        str.append("<p>您的商家账号:"+ DesensitizedUtil.mobilePhone(phone)+"在"+ TimeUtil.time(new Date()) +"登录错误多次，系统发出警告，如果非本人登录请忽略，系统将进行ip封禁等待处理；如果本人忘记密码可以点击邮箱联系管理员进行重置密码处理</p>");
        if (login_admin.getEmail()!=null&& FormatUtil.isEmail(login_admin.getEmail())){
            str.append("管理员邮箱：<a href=\"mailto:"+login_admin.getEmail()+"\"><strong>"+login_admin.getEmail()+"</strong></a>");
        }else {
            str.append("<a target=\"_blank\" href=\"http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=wriqo6ylqqespezy_vL3grOz7KGtrw\" style=\"text-decoration:none;\"><img src=\"http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_02.png\"/></a>");
        }
        str.append("<p>登录设备信息："+request+"。</p>");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MailUtil.send(account,CollUtil.newArrayList(email),"乐在购物系统商家登录警告",str.toString(),true);
                log.info("发送邮件：{}，商家登录警告",email);
            }
        }).start();
    }

    /**
     * 商家操作通知
     * @param email 收件人
     * @param request 操作请求信息
     * @param type 操作类型
     */
    public void merSendnotice(String email, String request,String phone,String type){
        StringBuffer str=new StringBuffer();
        str.append("<h1>乐在购物网</h1>");
        str.append("<p>尊敬的"+email.split("@")[0]+"，您好!</p>");
        str.append("<p>您的商家账号："+DesensitizedUtil.mobilePhone(phone)+"在"+ TimeUtil.time(new Date()) +"进行了<strong>"+type+"</strong>操作，系统在此通知！如果有任何疑问请点击邮箱联系管理员。</p>");
        if (login_admin.getEmail()!=null&& FormatUtil.isEmail(login_admin.getEmail())){
            str.append("管理员邮箱：<a href=\"mailto:"+login_admin.getEmail()+"\"><strong>"+login_admin.getEmail()+"</strong></a>");
        }else {
            str.append("<a target=\"_blank\" href=\"http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=wriqo6ylqqespezy_vL3grOz7KGtrw\" style=\"text-decoration:none;\"><img src=\"http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_02.png\"/></a>");
        }
        str.append("<p>操作设备信息："+request+"。</p>");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MailUtil.send(account,CollUtil.newArrayList(email),"乐在购物系统商家通知-"+type,str.toString(),true);
                log.info("发送邮件：{}，商家通知-{}",email,type);
            }
        }).start();
    }

    /**
     * 商家密码重置通知
     * @param email 商家收件人
     * @param phone  商家手机号
     * @param type 操作类型
     * @param info 通知信息
     */
    public void merSendReset(String email,String phone,String type,String info){
        StringBuffer str=new StringBuffer();
        str.append("<h1>乐在购物网</h1>");
        str.append("<p>尊敬的"+email.split("@")[0]+"，您好!</p>");
        str.append("<p>您的商家账号："+DesensitizedUtil.mobilePhone(phone)+"在"+ TimeUtil.time(new Date()) +"进行了<strong>"+type+"</strong>操作，系统在此通知！如果有任何疑问请点击邮箱联系管理员。</p>");
        if (login_admin.getEmail()!=null&& FormatUtil.isEmail(login_admin.getEmail())){
            str.append("管理员邮箱：<a href=\"mailto:"+login_admin.getEmail()+"\"><strong>"+login_admin.getEmail()+"</strong></a>");
        }else {
            str.append("<a target=\"_blank\" href=\"http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=wriqo6ylqqespezy_vL3grOz7KGtrw\" style=\"text-decoration:none;\"><img src=\"http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_02.png\"/></a>");
        }
        str.append("<p>"+info+"</p>");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MailUtil.send(account,CollUtil.newArrayList(email),"乐在购物系统商家通知-"+type,str.toString(),true);
                log.info("发送邮件：{}，商家通知-{}",email,type);
            }
        }).start();
    }

    public void admSendNotice(String email,String adm_account,String type,String request){
        StringBuffer str=new StringBuffer();
        str.append("<h1>乐在购物网</h1>");
        str.append("<p>尊敬的"+email.split("@")[0]+"，您好!</p>");
        str.append("<p>您的管理员账号："+adm_account+"在"+ TimeUtil.time(new Date()) +"进行了<strong>"+type+"</strong>操作，系统在此通知！</p>");
        str.append("<p>操作设备信息："+request+"。</p>");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MailUtil.send(account,CollUtil.newArrayList(email),"乐在购物系统管理员通知-"+type,str.toString(),true);
                log.info("发送邮件：{}，管理员通知-{}",email,type);
            }
        }).start();
    }
}
