package com.zhangheng.happy_shopping.web.controller;

import com.zhangheng.happy_shopping.android.entity.Merchants;
import com.zhangheng.happy_shopping.android.entity.submitgoods.goods;
import com.zhangheng.happy_shopping.android.repository.ListGoodsRepository;
import com.zhangheng.happy_shopping.android.repository.SubmitGoodsRepository;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.web.controller.data.main.listgoodsByState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//商家订单页面控制器
@Controller
public class OderListController {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private ListGoodsRepository listGoodsRepository;
    @Autowired
    private SubmitGoodsRepository submitGoodsRepository;
    /**
     * 跳转至商家订单界面
     * @param model
     * @return
     */
    @GetMapping("/OderListPage")
    private String OderListPage(Model model){
        model.addAttribute("active",1);
        return "merchants/orderlist_mer";
    }
    /**
     * 查询本店不同订单状态的订单商品
     * @param store_id  店铺id
     * @param state 订单状态
     * @return
     */
    private List<listgoodsByState> findlistgoodsByState( int id,int store_id, int state){
        List<listgoodsByState> list = new ArrayList<>();
        List<goods> state_list;
        if (id>0) {
            //根据订单状态查询本店订单
            state_list = listGoodsRepository.findByStore_idAndState(store_id, state);
        }else{
            //查询本店所有类型的订单
            state_list = listGoodsRepository.findByStore_id(store_id);
        }
        for (goods g : state_list) {
            listgoodsByState listgoods = new listgoodsByState();
            String list_id = g.getList_id();
            listgoods.setOrder_id(list_id);
            //将订单号转换为时间
            String time=list_id.substring(0,4)+"年"+list_id.substring(4,6)+"月"+list_id.substring(6,8)+"日 "
                    +list_id.substring(8,10)+":"+list_id.substring(10,12);
            listgoods.setTime(time);
            Optional<String> name = submitGoodsRepository.findNameBySubmit_id(list_id);
            if (name.isPresent()){
                listgoods.setUsername(name.get());
            }
            Optional<String> phone = submitGoodsRepository.findPhoneBySubmit_id(list_id);
            if (phone.isPresent()){
                listgoods.setTel(phone.get());
            }
            Optional<String> address = submitGoodsRepository.findAddressBySubmit_id(list_id);
            if (address.isPresent()){
                listgoods.setAddress(address.get());
            }
            listgoods.setGoods_name(g.getGoods_name());
            listgoods.setGoods_id(g.getGoods_id());
            listgoods.setId_goods(g.getId());
            listgoods.setPrice(g.getGoods_price());
            listgoods.setNum(g.getNum());
            listgoods.setAll_price(Message.twoDecimalPlaces(g.getGoods_price()*g.getNum()));
            listgoods.setState(g.getState());
            list.add(listgoods);
        }
        return list;
    }

    /**
     * 查询本店订单商品
     * @param method
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/OrderGoodsByState")
    private List<listgoodsByState> OrderGoodsByState(@RequestParam("method")Integer method, HttpServletRequest request){
        log.info("查询本店"+method+"的订单商品");
        List<listgoodsByState> list = new ArrayList<>();
        Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
        if (method>=0){//根据点单类型查询本店订单商品
            list = findlistgoodsByState(1, merchants.getStore_id(), method);
        }else {//查询本店所有订单商品
            list = findlistgoodsByState(0, merchants.getStore_id(), method);
        }
        return list;
    }
}
