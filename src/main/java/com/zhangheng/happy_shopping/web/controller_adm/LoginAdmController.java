package com.zhangheng.happy_shopping.web.controller_adm;

import com.zhangheng.happy_shopping.utils.CusAccessObjectUtil;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import com.zhangheng.happy_shopping.web.controller_adm.bean.Login_admin;
import com.zhangheng.happy_shopping.web.entity.OperationLog;
import com.zhangheng.happy_shopping.web.repository.OperaLogRepository;
import com.zhangheng.happy_shopping.web.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

/**
 * 管理员登录
 */
@Controller
public class LoginAdmController {
    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    private Login_admin login_admin;
    @Autowired
    private LoginService loginService;
    @Autowired
    private OperaLogRepository logRepository;

    /**
     * 跳转至管理员登录界面
     * @return
     */
    @GetMapping("/login_admin")
    private String login_adminPage(HttpServletRequest request){
        log.info("管理员登录页："+ CusAccessObjectUtil.getRequst(request));
//        log.info(login_admin.toString());
        return "administrator/login_adm";
    }

    /**
     * 管理员登录表单提交
     * @param loginAdmin
     * @param model
     * @return
     */
    @PostMapping("/login_adm")
    private String login_admin(Login_admin loginAdmin, Model model, HttpServletRequest request){
        Message msg = new Message();
        //操作记录
        OperationLog olog = new OperationLog();
        olog.setRequest(CusAccessObjectUtil.getRequst(request));
        olog.setInfo("管理员登录操作");
        olog.setTime(TimeUtil.time(new Date()));
        olog.setType(1);
        //判断表单是否为空
        if (loginAdmin!=null && loginAdmin.getAccount()!=null){
            olog.setTel(loginAdmin.getAccount());
            boolean b = loginService.login_log(olog);
            //根据请求request和操作类型查询操作记录
            Optional<OperationLog> requestAndType = logRepository.findByRequestAndType(CusAccessObjectUtil.getRequst(request), 1);
            int count = requestAndType.get().getCount();
            //判断是否重复操作
            if (b) {
                //验证账户
                if (loginAdmin.getAccount().equals(login_admin.getAccount())) {
                    //验证密码
                    if (loginAdmin.getPassword().equals(login_admin.getPassword())) {
                        requestAndType.get().setCount(0);
                        requestAndType.get().setTime(TimeUtil.time(new Date()));
                        requestAndType.get().setInfo("管理员登录成功");
                        logRepository.saveAndFlush(requestAndType.get());
                        //将登陆者的ip地址设置到管理员登录信息中
                        loginAdmin.setPassword(CusAccessObjectUtil.getIpAddress(request));
                        request.getSession().setAttribute("admin",loginAdmin);
                        msg.setCode(200);
                        msg.setMessage("登录成功！");
                        return "redirect:/home";
                    } else {
                        msg.setCode(500);
                        if (LoginService.Max_Count-count >0) {
                            msg.setMessage("密码错误！你还有" + (LoginService.Max_Count- count) + "次机会");
                        }else {
                            log.warn("管理员登录频繁:"+requestAndType.get().getRequest());
                            msg.setMessage("对不起,你没有登录机会了，请"+LoginService.Wait_Time+"分钟后在来");
                        }
                    }
                } else {
                    msg.setCode(500);
                    if (LoginService.Max_Count-count >0) {
                        msg.setMessage("账号错误！你还有" + (LoginService.Max_Count- count) + "次机会");
                    }else {
                        msg.setMessage("对不起,你没有登录机会了,请"+LoginService.Wait_Time+"分钟后在来");
                    }

                }
            }else {
                int i = TimeUtil.minutesDifference(requestAndType.get().getTime(), TimeUtil.time(new Date()));
                msg.setCode(500);
                msg.setMessage("错误次数过多,请"+(LoginService.Wait_Time-i)+"分钟后再操作");
            }
        }else {
            msg.setCode(404);
            msg.setMessage("表单信息为空");
        }
        model.addAttribute("msg",msg);
        return "administrator/login_adm";
    }
    @GetMapping("exit_adm")
    private String exit_adm(HttpServletRequest request){
        request.getSession().removeAttribute("admin");
        return "redirect:/login_admin";
    }
}
