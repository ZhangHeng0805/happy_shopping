package com.zhangheng.happy_shopping.web.controller_adm;

import com.zhangheng.happy_shopping.android.entity.Merchants;
import com.zhangheng.happy_shopping.android.entity.Store;
import com.zhangheng.happy_shopping.android.repository.MerchantsRepository;
import com.zhangheng.happy_shopping.android.repository.StoreRepository;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import com.zhangheng.happy_shopping.web.controller_adm.bean.MerchantsInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 商家管理界面
 */
@Controller
public class MerchantsListController {
    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    private MerchantsRepository merchantsRepository;
    @Autowired
    private StoreRepository storeRepository;

    @GetMapping("/merchantsListPage")
    private String merchantsListPage(Model model){
        model.addAttribute("active",2);
        return "administrator/merchantsList";
    }

    /**
     * 获取所有商家的信息
     * 商家列表中的表格数据
     * @return
     */
    @ResponseBody
    @PostMapping("/getMerchantsList")
    private List<MerchantsInfo> getMerchantsList(){
        List<MerchantsInfo> list =new ArrayList<>();
        List<Merchants> all = merchantsRepository.findAll();
        for (Merchants mer : all) {
            Optional<Store> byId = storeRepository.findById(mer.getStore_id());
            if (byId.isPresent()){
                Store store = byId.get();
                MerchantsInfo info = new MerchantsInfo();
                info.setStore_image(store.getStore_image());
                info.setStore_name(store.getStore_name());
                info.setStore_id(store.getStore_id());
                info.setStore_address(mer.getAddress());
                info.setStore_introduce(mer.getStore_introduce());
                info.setTime(String.valueOf(TimeUtil.daysDifference(TimeUtil.time(new Date()),store.getStart_time())));
                info.setMer_name(mer.getName());
                info.setEmail(mer.getEmail());
                info.setTel(mer.getPhonenum());
                info.setState(mer.getState());
                info.setTurnover(store.getTurnover());
                list.add(info);
            }
        }
        return list;
    }

    @ResponseBody
    @PostMapping("/set_merchantsState")
    private Message set_merchantsState(String tel,int state){
        Message msg = new Message();
        if (tel!=null){
            if (state==0 || state==1){
                int i = merchantsRepository.updateStateByPhonenum(state, tel);
                if (i>0){
                    msg.setCode(200);
                    msg.setMessage("修改成功！");
                }else {
                    msg.setCode(500);
                    msg.setMessage("修改失败！");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("商家状态设置错误！");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("表单信息提交为空！");
        }
        return msg;
    }
}