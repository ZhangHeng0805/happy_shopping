package com.zhangheng.happy_shopping.web.controller_mer;

import com.zhangheng.happy_shopping.android.entity.Merchants;
import com.zhangheng.happy_shopping.android.entity.Store;
import com.zhangheng.happy_shopping.android.repository.MerchantsRepository;
import com.zhangheng.happy_shopping.android.repository.StoreRepository;
import com.zhangheng.happy_shopping.bean.VerificationCode;
import com.zhangheng.happy_shopping.controller.FileLoadController;
import com.zhangheng.happy_shopping.repository.CodeRepository;
import com.zhangheng.happy_shopping.utils.CusAccessObjectUtil;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.PhoneNumUtil;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import com.zhangheng.happy_shopping.web.controller_adm.bean.Login_admin;
import com.zhangheng.happy_shopping.web.entity.OperationLog;
import com.zhangheng.happy_shopping.web.repository.OperaLogRepository;
import com.zhangheng.happy_shopping.web.service.EmailService;
import com.zhangheng.happy_shopping.web.service.LoginService;
import com.zhangheng.util.FormatUtil;
import com.zhangheng.util.RandomrUtil;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.zhangheng.happy_shopping.web.service.LoginService.Max_Count;

/*
* 商家登录注册控制器
* */
@Controller
public class LoginController {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private MerchantsRepository merchantsRepository;
    @Autowired
    private LoginService loginService;
    @Autowired
    private OperaLogRepository logRepository;
    @Autowired
    private CodeRepository codeRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private Login_admin login_admin;
    /**
     * 商家登录页跳转
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/login_merchantsPage")
    private String login_merchantsPage(Model model, HttpServletRequest request){
        Message msg = new Message();
        msg.setCode(100);
        msg.setMessage("请登录，注意错误机会只有5次");
        log.info("商家登录页："+ CusAccessObjectUtil.getRequst(request));
        String email_html="";
        if (login_admin.getEmail()!=null&& FormatUtil.isEmail(login_admin.getEmail())){
            email_html="管理邮箱：<a href=\"mailto:"+login_admin.getEmail()+"\"><strong>"+login_admin.getEmail()+"</strong></a>";
        }else {
            email_html="<a target=\"_blank\" href=\"http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=wriqo6ylqqespezy_vL3grOz7KGtrw\" style=\"text-decoration:none;\"><img src=\"http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_02.png\"/></a>";
        }
        model.addAttribute("admin_email",email_html);
//        model.addAttribute("msg",msg);
        return "merchants/login_mer";
    }

    /**
     * 商家登录表单提交
     * @param merchants 商家信息(手机号，密码)
     * @param model
     * @param request
     * @return
     */
    @PostMapping("/login_merchants")
    private String login_merchants(@Nullable Merchants merchants, Model model, HttpServletRequest request){
        Message msg = new Message();
        OperationLog olog = new OperationLog();
        olog.setRequest(CusAccessObjectUtil.getRequst(request));
        olog.setInfo("商家登录操作");
        olog.setTime(TimeUtil.time(new Date()));
        olog.setType(0);
        //判断数据是否为空
        if (merchants!=null){
            //判断手机号格式是否正确
            if (PhoneNumUtil.isMobile(merchants.getPhonenum())){
                olog.setTel(merchants.getPhonenum());
                boolean b = loginService.login_log(olog);
                //根据请求request和操作类型查询操作记录
                Optional<OperationLog> requestAndType = logRepository.findByRequestAndType(CusAccessObjectUtil.getRequst(request), 0);
                //判断是否重复操作
                if (b) {
                    Optional<Merchants> byId = merchantsRepository.findById(merchants.getPhonenum());
                    //判断账号是否存在
                    if (byId.isPresent()) {
                        //验证密码
                        if (byId.get().getPassword().equals(merchants.getPassword())) {
                            //判断日志操作
                            if (requestAndType.isPresent()) {
                                if (byId.get().getState().equals(0)) {
                                    requestAndType.get().setCount(0);
                                    requestAndType.get().setTel(byId.get().getPhonenum());
                                    requestAndType.get().setTime(TimeUtil.time(new Date()));
                                    requestAndType.get().setInfo("商家["+byId.get().getName()+"]登录成功");
                                    logRepository.saveAndFlush(requestAndType.get());

                                    request.getSession().setAttribute("merchants", byId.get());
                                    Optional<Store> storeOptional = storeRepository.findById(byId.get().getStore_id());
                                    request.getSession().setAttribute("store", storeOptional.get());

                                    log.info("商家登录:" + byId.get().getPhonenum() + "\t 姓名:" + byId.get().getName());
                                    return "redirect:/main";
                                }else {
                                    msg.setCode(404);
                                    msg.setMessage("对不起，你的账号"+Merchants.checkState(byId.get().getState())+",暂时无法登录，<a href='http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=wriqo6ylqqespezy_vL3grOz7KGtrw'>申述反馈</a>");
                                }
                            }else {
                                msg.setCode(500);
                                msg.setMessage("操作日志错误");
                            }
                        } else {
                            msg.setCode(500);
                            if (Max_Count-requestAndType.get().getCount()>0) {
                                msg.setMessage("密码错误,你还有" + (Max_Count-requestAndType.get().getCount()) + "次机会");
                            }else {
                                log.warn("商家登录频繁:"+requestAndType.get().getRequest());
                                msg.setMessage("对不起,你没有登录机会了，请"+LoginService.Wait_Time+"分钟后在来");
                            }
                        }
                    } else {
                        msg.setCode(500);
                        if (Max_Count-requestAndType.get().getCount()>0) {
                            msg.setMessage("该手机号未注册,你还有" + (Max_Count-requestAndType.get().getCount()) + "次机会");
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
                msg.setCode(500);
                msg.setMessage("手机号格式错误");
            }
        }else {
            msg.setCode(404);
            msg.setMessage("登录提交为空");

        }
        model.addAttribute("msg",msg);
        return "merchants/login_mer";
    }

    /**
     * 商家注册页面跳转
     * @param model
     * @return
     */
    @GetMapping("/regist_merchantsPage")
    private String regist_merchantsPage(Model model){
        List<VerificationCode> all = codeRepository.findAll();
        if (!all.isEmpty()){
            int i = Message.RandomNum(0, all.size() - 1);
            model.addAttribute("codeId",all.get(i).getId());
        }
        Message msg = new Message();
        msg.setCode(100);
        msg.setMessage("欢迎加入我们！");
        model.addAttribute("msg",msg);
        return "merchants/registration_mer";
    }

    /**
     * 商家注册表单提交
     * @param mer 商家信息
     * @param model
     * @param image 店铺图片的base64
     * @param code 验证码
     * @param request
     * @return
     */
    @PostMapping("/regist_merchants")
    private String regist_merchants(@Nullable Merchants mer, Model model,@Nullable String image,@Nullable VerificationCode code,HttpServletRequest request) throws Exception {
        Message msg = new Message();
        //判断验证码是否为空
        if (code.getCode()!=null&&code.getId()!=null) {
            //判断注册表单是否为空
            if (mer.getPhonenum() != null) {
                Optional<VerificationCode> codebyId = codeRepository.findById(code.getId());
                //判断验证码是否存在
                if (codebyId.isPresent()){
                    //判断验证码是否正确
                    if(codebyId.get().getCode().equals(code.getCode())){
                        //判断上传图片是否为空
                        if (!image.isEmpty()) {
                            Optional<Merchants> byId = merchantsRepository.findById(mer.getPhonenum());
                            //判断手机号是否注册
                            if (!byId.isPresent()) {
                                FileLoadController fileLoadController = new FileLoadController();
                                String s = fileLoadController.base64ToImg(image, mer.getStore_name(), "Store_Images");
                                //判断图片保存是否成功
                                if (s != null) {
                                    Store store = new Store();
                                    String time = TimeUtil.time(new Date());
                                    store.setStore_image(s);
                                    store.setBoss_name(mer.getName());
                                    store.setStart_time(time);
                                    store.setStore_introduce(mer.getStore_introduce());
                                    store.setStore_name(mer.getStore_name());
                                    store.setTurnover(0.0);
                                    try {
                                        Store save = storeRepository.save(store);
                                        //判断商铺信息保存是否成功
                                        if (save != null) {
                                            mer.setTime(time);
                                            mer.setStore_id(save.getStore_id());
                                            mer.setState(0);//账号正常
                                            Merchants save1 = merchantsRepository.save(mer);
                                            log.info("商家注册:" + save1.getPhonenum() + ",姓名:" + save1.getName());
                                            msg.setCode(200);
                                            msg.setMessage("恭喜:" + save1.getName() + "，注册成功！<a href=\"/login_merchantsPage\">去登录</a>");
                                        } else {
                                            msg.setCode(500);
                                            msg.setMessage("商铺信息保存失败");
                                            fileLoadController.deleteFile(s);
                                        }
                                    }catch (Exception e){
                                        msg.setMessage("错误："+e.getMessage());
                                        msg.setCode(500);
                                        fileLoadController.deleteFile(s);
                                    }

                                } else {
                                    msg.setCode(500);
                                    msg.setMessage("商铺图片保存失败，请检查文件重试");
                                }
                            }else {
                                msg.setCode(500);
                                msg.setMessage("此手机号已注册,请勿重复注册");
                            }
                        }else {
                            msg.setCode(500);
                            msg.setMessage("上传图片为空");
                        }
                    }else {
                        msg.setCode(500);
                        msg.setMessage("验证码错误");
                    }
                }else {
                    msg.setCode(500);
                    msg.setMessage("验证码为空");
                }
//                System.out.println(mer.toString());
            } else {
                msg.setCode(404);
                msg.setMessage("注册内容为空");
            }
        }else {
            msg.setCode(404);
            msg.setMessage("验证码为空");
        }
        List<VerificationCode> all = codeRepository.findAll();
        if (!all.isEmpty()){
            int i = Message.RandomNum(0, all.size() - 1);
            model.addAttribute("codeId",all.get(i).getId());
        }
        model.addAttribute("msg",msg);
        return "merchants/registration_mer";
    }

    /**
     * 退出登录，并清除商家的登录信息
     * @param request
     * @return
     */
    @GetMapping("/exit_mer")
    private String exit_mer(HttpServletRequest request){
        request.getSession().removeAttribute("merchants");
        request.getSession().removeAttribute("store");
        return "redirect:/login_merchantsPage";
    }

    /**
     * 获取邮箱验证码
     * @param email
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/get_EmailCode")
    private Message get_EmailCode(@Nullable String email,HttpServletRequest request){
        Message msg = new Message();
        if (email!=null&&email.trim().length()>0) {
            if (FormatUtil.isEmail(email)) {
                //生成随机6位验证码
                String word = RandomrUtil.createPassWord(6, RandomrUtil.Number);
                try {
                    List<OperationLog> all = logRepository.findAllByTelAndType(email, 3);
                    String req = CusAccessObjectUtil.getRequst(request);
                    OperationLog log;
                    //查找是否有记录
                    if (all!=null&&all.size()>0){
                       log = all.get(0);
                        int difference = TimeUtil.minutesDifference(TimeUtil.time(new Date()), log.getTime());
                        if (difference <LoginService.Wait_Time){
                            msg.setCode(404);
                            msg.setMessage("邮件已发送，请"+(LoginService.Wait_Time-difference)+"分钟后重试！");
                       }else {
                            if (log.getCount() < Max_Count) {
                                log.setCount(log.getCount() + 1);
                                log.setInfo(word);
                                log.setRequest(req);
                                //发送验证码
                                emailService.merSendCode(email,word);
                                log.setTime(TimeUtil.time(new Date()));
                                OperationLog operationLog = logRepository.saveAndFlush(log);
                                if (operationLog != null && operationLog.getId() > 0) {
                                    if (operationLog.getCount()== Max_Count-1){
                                        msg.setCode(200);
                                        msg.setMessage("验证码已发出，验证码有效期" + LoginService.Wait_Time + "分钟，因为验证码发送频繁，所以还有最后一次机会！");
                                    }else {
                                        msg.setCode(200);
                                        msg.setMessage("验证码已发出，请注意查收，验证码有效期" + LoginService.Wait_Time + "分钟");
                                    }
                                } else {
                                    msg.setCode(500);
                                    msg.setMessage("错误1：验证信息保存失败！请重试");
                                }
                            }else {
                                msg.setCode(500);
                                msg.setMessage("对不起，此邮箱请求验证码的次数过多，已被封禁，请切换其他邮箱。");
                            }
                        }
                    }else {//没有记录
                         log= new OperationLog();
                         log.setTel(email);
                         log.setRequest(req);
                         log.setType(3);
                         log.setInfo(word);
                         log.setCount(0);
                        //发送验证码
                        emailService.merSendCode(email,word);
                        log.setTime(TimeUtil.time(new Date()));
                        OperationLog operationLog = logRepository.saveAndFlush(log);
                        if (operationLog!=null&&operationLog.getId()>0){
                            msg.setCode(200);
                            msg.setMessage("验证码已发出，请注意查收，验证码有效期"+LoginService.Wait_Time+"分钟");
                        }else {
                            msg.setCode(500);
                            msg.setMessage("错误2：验证信息保存失败！请重试");
                        }
                    }
                }catch (Exception e){
                    msg.setCode(500);
                    msg.setMessage("错误："+e.getMessage());
                }
            }else {
                msg.setCode(500);
                msg.setMessage("邮箱格式错误");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("邮箱地址不能为空");
        }
        return msg;
    }


    /**
     * 验证邮箱验证码
     * @param email
     * @param code
     * @return
     */
    @ResponseBody
    @PostMapping("/ver_EmailCode")
    private Message ver_EmailCode(@Nullable String email,@Nullable String code){
        Message msg = new Message();
        if (email!=null&&code!=null){
            if (FormatUtil.isEmail(email)){
                if (code.trim().length()>0){
                    List<OperationLog> all = logRepository.findAllByTelAndType(email, 3);
                    if (all!=null&&all.size()>0){
                        OperationLog operationLog = all.get(0);
                        int difference = TimeUtil.minutesDifference(TimeUtil.time(new Date()), operationLog.getTime());
                        if (difference<=LoginService.Wait_Time) {
                            if (code.equals(operationLog.getInfo())) {
                                msg.setCode(200);
                                msg.setMessage("验证成功");
                                operationLog.setCount(0);//重置次数
                                logRepository.saveAndFlush(operationLog);
                            } else {
                                msg.setCode(500);
                                msg.setMessage("对不起，验证码错误");
                            }
                        }else {
                            msg.setCode(500);
                            msg.setMessage("对不起，验证码已过期，请重试");
                        }
                    }else {
                        msg.setCode(500);
                        msg.setMessage("对不起，没有验证信息，请先获取验证码");
                    }
                }else {
                    msg.setCode(500);
                    msg.setMessage("验证码不能为空");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("邮箱格式错误");
            }
        }else {
         msg.setCode(500);
         msg.setMessage("邮箱或验证码不能为空");
        }
        return msg;
    }
}
