package com.zhangheng.happy_shopping.web.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.zhangheng.bean.Const;
import com.zhangheng.happy_shopping.utils.CusAccessObjectUtil;
import com.zhangheng.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

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
        str.append("<p>您的商家账号:"+ DesensitizedUtil.mobilePhone(phone)+"登录错误多次，系统发出警告，如果非本人登录请忽略，系统将进行ip封禁等待处理；如果本人忘记密码可以点击邮箱联系管理员进行重置密码处理</p>");
        str.append("<a target=\"_blank\" href=\"http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=wriqo6ylqqespezy_vL3grOz7KGtrw\" style=\"text-decoration:none;\"><img src=\"http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_02.png\"/></a>");
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
     * @param email
     * @param request
     * @param type
     */
    public void merSendnotice(String email, String request,String phone,String type){
        StringBuffer str=new StringBuffer();
        str.append("<h1>乐在购物网</h1>");
        str.append("<p>尊敬的"+email.split("@")[0]+"，您好!</p>");
        str.append("<p>您的商家账号："+DesensitizedUtil.mobilePhone(phone)+"刚刚进行了<strong>"+type+"</strong>操作，系统在此通知！如果有任何疑问请点击邮箱联系管理员。</p>");
        str.append("<a target=\"_blank\" href=\"http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=wriqo6ylqqespezy_vL3grOz7KGtrw\" style=\"text-decoration:none;\"><img src=\"http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_02.png\"/></a>");
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
     * @param email
     * @param phone
     * @param type
     * @param info
     */
    public void merSendReset(String email,String phone,String type,String info){
        StringBuffer str=new StringBuffer();
        str.append("<h1>乐在购物网</h1>");
        str.append("<p>尊敬的"+email.split("@")[0]+"，您好!</p>");
        str.append("<p>您的商家账号："+DesensitizedUtil.mobilePhone(phone)+"刚刚进行了<strong>"+type+"</strong>操作，系统在此通知！如果有任何疑问请点击邮箱联系管理员。</p>");
        str.append("<a target=\"_blank\" href=\"http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=wriqo6ylqqespezy_vL3grOz7KGtrw\" style=\"text-decoration:none;\"><img src=\"http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_02.png\"/></a>");
        str.append("<p>"+info+"</p>");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MailUtil.send(account,CollUtil.newArrayList(email),"乐在购物系统商家通知-"+type,str.toString(),true);
                log.info("发送邮件：{}，商家通知-{}",email,type);
            }
        }).start();
    }
}
