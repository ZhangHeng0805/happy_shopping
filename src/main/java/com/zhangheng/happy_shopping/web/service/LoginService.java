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


    public boolean login_log(OperationLog log){
        if (log.getRequest()!=null){
            //根据request和操作类型查询
            Optional<OperationLog> requestAndType = logRepository.findByRequestAndType(log.getRequest(), log.getType());
            //判断有无操作记录
            if (requestAndType.isPresent()){
                OperationLog log1 = requestAndType.get();
                if (log1.getCount()>=5){//次数大于等于5
                    int i = TimeUtil.minutesDifference(TimeUtil.time(new Date()), log1.getTime());
                    if (i<5){//时间间隔小于5分钟
                        //次数大于5 间隔小于5 拦截
                        return false;
                    }else {//时间间隔大于5分钟
                        //重置次数 放行
                        log1.setCount(3);
                        log1.setTime(log.getTime());
                        log1.setTel(log.getTel());
                        log1.setInfo(log.getInfo());
                        logRepository.saveAndFlush(log1);
                    }
                }else {//次数小于5
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
