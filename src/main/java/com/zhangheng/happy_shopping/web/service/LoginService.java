package com.zhangheng.happy_shopping.web.service;

import cn.hutool.core.util.DesensitizedUtil;
import com.zhangheng.happy_shopping.android.entity.Merchants;
import com.zhangheng.happy_shopping.android.repository.MerchantsRepository;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import com.zhangheng.happy_shopping.web.entity.OperationLog;
import com.zhangheng.happy_shopping.web.repository.OperaLogRepository;
import com.zhangheng.log.printLog.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private OperaLogRepository logRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MerchantsRepository merchantsRepository;

    @Value("${max_count}")
    private String max_count;
    @Value("${wait_time}")
    private String wait_time;

    public static int Max_Count=5;//最大重试次数
    public static int Wait_Time=5;//等待时间（分钟）

    //读取配置文件中的设置
    public void init() {
        if (max_count!=null){
            Integer max_count = Integer.valueOf(this.max_count);
            if (max_count>3) {
                Max_Count= max_count;
            }
        }
        if (wait_time!=null){
            Integer wait_time = Integer.valueOf(this.wait_time);
            if (wait_time>3) {
                Wait_Time = wait_time;
            }
        }
    }

    /**
     * 登录次数判断
     * @param log
     * @return
     */
    public boolean login_log(OperationLog log){
        init();
        if (log.getRequest()!=null){
            //根据request和操作类型查询
            Optional<OperationLog> requestAndType = logRepository.findByRequestAndType(log.getRequest(), log.getType());
            //判断有无操作记录
            if (requestAndType.isPresent()){
                OperationLog log1 = requestAndType.get();
                if (log1.getCount()>=Max_Count){//次数大于等于最大次数
                    int i = TimeUtil.minutesDifference(TimeUtil.time(new Date()), log1.getTime());
                    if (i<Wait_Time){//时间间隔小于等待时间
                        //次数大于最大次数 间隔小于等待时间 拦截
                        return false;
                    }else {//时间间隔大于等待时间
                        //重置次数 放行
                        log1.setCount((int) (Max_Count*0.7));
                        log1.setTime(log.getTime());
                        log1.setTel(log.getTel());
                        log1.setInfo(log.getInfo());
                        logRepository.saveAndFlush(log1);
                    }
                }else {//次数小于最大次数
                    //次数+1
                    log1.setCount(log1.getCount()+1);
                    log1.setTime(log.getTime());
                    log1.setTel(log.getTel());
                    log1.setInfo(log.getInfo());
                    logRepository.saveAndFlush(log1);
                    //商家登录重试达到限制前时，发送邮箱警告
                    if (log1.getType()==0) {
                        if (log1.getCount()==Max_Count-1) {
                            Optional<Merchants> byId = merchantsRepository.findById(log1.getTel());
                            if (byId.isPresent()) {
                                emailService.loginsendWarn(byId.get().getEmail(), log1.getRequest(), byId.get().getPhonenum());
                            }
                        }
                    }
                }
            }else {//没有操作记录
                log.setCount(0);
                OperationLog save = logRepository.save(log);
            }
        }
        return true;
    }
}
