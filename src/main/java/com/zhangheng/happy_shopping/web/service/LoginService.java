package com.zhangheng.happy_shopping.web.service;

import com.zhangheng.happy_shopping.utils.TimeUtil;
import com.zhangheng.happy_shopping.web.entity.OperationLog;
import com.zhangheng.happy_shopping.web.repository.OperaLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private OperaLogRepository logRepository;

    public static final int Max_Count=5;//最大重试次数

    public static final int Wait_Time=5;//等待时间（分钟）

    /**
     * 登录次数判断
     * @param log
     * @return
     */
    public boolean login_log(OperationLog log){
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
                        log1.setCount(3);
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
                }
            }else {//没有操作记录
                log.setCount(0);
                OperationLog save = logRepository.save(log);
            }
        }
        return true;
    }
}
