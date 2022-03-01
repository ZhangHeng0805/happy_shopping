package com.zhangheng.happy_shopping.web.controller_adm;

import com.zhangheng.happy_shopping.android.entity.PhoneInfo;
import com.zhangheng.happy_shopping.android.repository.PhoneInfoRepository;
import com.zhangheng.happy_shopping.web.entity.OperationLog;
import com.zhangheng.happy_shopping.web.repository.OperaLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统日志界面
 * @author 张恒
 * @program: happy_shopping
 * @email zhangheng.0805@qq.com
 * @date 2022-03-01 10:42
 */
@Controller
public class SysLogController {
    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    private OperaLogRepository logRepository;
    @Autowired
    private PhoneInfoRepository phoneInfoRepository;

    /**
     * 跳转至系统日志界面
     * @param model
     * @return
     */
    @GetMapping("/sysLogPage")
    private String goodsListPage(Model model){
        model.addAttribute("active",8);
        return "administrator/sysLog";
    }

    @ResponseBody
    @PostMapping("/getLogByType")
    private List<OperationLog> getLogByType(int type){
        List<OperationLog> logs = new ArrayList<>();
        //判断类型
        if (type>=0&&type<=2) {
            List<OperationLog> allByType = logRepository.findAllByTypeOrderByTime(type);
            logs=allByType;
        }
        return logs;
    }
    @ResponseBody
    @PostMapping("/getPhoneInfoList")
    private List<PhoneInfo> getPhoneInfoList(){
        List<PhoneInfo> phoneInfos = new ArrayList<>();
        List<PhoneInfo> all = phoneInfoRepository.findAll();
        phoneInfos=all;
        return phoneInfos;
    }


}
