package com.zhangheng.happy_shopping.web.controller_adm;

import com.zhangheng.happy_shopping.android.entity.Customer;
import com.zhangheng.happy_shopping.android.entity.Store;
import com.zhangheng.happy_shopping.android.entity.submitgoods.SubmitGoods;
import com.zhangheng.happy_shopping.android.repository.CustomerRepository;
import com.zhangheng.happy_shopping.android.repository.StoreRepository;
import com.zhangheng.happy_shopping.android.repository.SubmitGoodsRepository;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.PhoneNumUtil;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import com.zhangheng.happy_shopping.web.controller_adm.bean.CustomerInfo;
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
 * 店铺管理界面
 * @author 张恒
 * @program: happy_shopping
 * @email zhangheng.0805@qq.com
 * @date 2022-02-24 15:36
 */
@Controller
public class StoresListController {
    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    private StoreRepository storeRepository;


    /**
     * 跳转至店铺管理界面
     * @param model
     * @return
     */
    @GetMapping("/storesListPage")
    private String customersListPage(Model model){
        model.addAttribute("active",6);
        return "administrator/storesList";
    }

    @ResponseBody
    @PostMapping("/getstoresList")
    private List<Store> getstoresList(){
        List<Store> list = new ArrayList<>();
        List<Store> all = storeRepository.findAll();
        list=all;
        return list;
    }


}
