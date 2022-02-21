package com.zhangheng.happy_shopping.web.controller_adm;

import com.zhangheng.happy_shopping.android.entity.submitgoods.SubmitGoods;
import com.zhangheng.happy_shopping.android.entity.submitgoods.goods;
import com.zhangheng.happy_shopping.android.repository.*;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.web.controller_adm.bean.count_orderBystate;
import com.zhangheng.happy_shopping.web.controller_mer.data.main.goodsnumBygoodstype;
import com.zhangheng.happy_shopping.web.entity.goods_type;
import com.zhangheng.happy_shopping.web.repository.GoodsTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*系统主页*/
@Controller
public class HomeController {
    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    private SubmitGoodsRepository submitGoodsRepository;
    @Autowired
    private MerchantsRepository merchantsRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ListGoodsRepository listGoodsRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsTypeRepository goodsTypeRepository;

    /**
     * 跳转至管理主页
     * @param model
     * @return
     */
    @GetMapping("/home")
    private String homePage(Model model){
        model.addAttribute("active",0);
        return "administrator/home_adm";
    }

    /**
     * 获取平台统计数据（成交额，成交量，商家数，顾客数）
     * 四个数据标签的数据接口
     * @return
     */
    @ResponseBody
    @PostMapping("/get_count")
    private Map<String, Object> count(){
        List<SubmitGoods> all = submitGoodsRepository.findAll();
        double count_price = 0;
        long count_num = 0,
                count_mer = 0,
                count_cus = 0;
        Map<String, Object> map = new HashMap<>();
        for (SubmitGoods sg : all) {
            List<goods> list = sg.getGoods_list();
            for (goods g : list) {
                //判断商品是否已经收货
                if (g.getState()==3){
                    //计算总成交额
                    count_price+=g.getGoods_price()*g.getNum();
                    //计算总成交量
                    count_num++;
                }
            }
        }
        count_mer = merchantsRepository.count();
        count_cus = customerRepository.count();
        map.put("count_price",Message.twoDecimalPlaces(count_price));
        map.put("count_num",count_num);
        map.put("count_merchants",count_mer);
        map.put("count_customer",count_cus);

        return map;
    }

    /**
     * 根据订单状态获取平台的订单类型数据
     * 环形图数据接口
     * @return
     */
    @ResponseBody
    @PostMapping("/get_order")
    private List<count_orderBystate> get_order(){
        List<count_orderBystate> list = new ArrayList<>();
        int count=0;
        double val=0;
        for (int i=0;i<=4;i++) {
            count_orderBystate bystate = new count_orderBystate();
            int i1 = listGoodsRepository.countByState(i);
            bystate.setValue(i1);
            //计算订单总数
            count+=i1;
            list.add(i,bystate);
        }
        for (int i=0;i<=4;i++) {
            count_orderBystate order = list.get(i);
            int value = order.getValue();
            switch (i){
                case 0:
                    order.setLabel("未处理");
                    String s0;
                    if (count>0) {
                        s0 = "订单数比例：" + Message.twoDecimalPlaces(((double)value / count) * 100) + "%";
                    }else {
                        s0 = "订单数比例：" + 0.0 + "%";
                    }
                    order.setFormatted(s0);
                    break;
                case 1:
                    order.setLabel("拒绝发货");
                    String s1;
                    if (count>0) {
                        s1 = "订单数比例：" + Message.twoDecimalPlaces(((double)value / count) * 100) + "%";
                    }else {
                        s1 = "订单数比例：" + 0.0 + "%";
                    }
                    order.setFormatted(s1);
                    break;
                case 2:
                    order.setLabel("待收货");
                    String s2;
                    if (count>0) {
                        s2 = "订单数比例：" + Message.twoDecimalPlaces(((double)value / count) * 100) + "%";
                    }else {
                        s2 = "订单数比例：" + 0.0 + "%";
                    }
                    order.setFormatted(s2);
                    break;
                case 3:
                    order.setLabel("已收货");
                    String s3;
                    if (count>0) {
                        s3 = "订单数比例：" + Message.twoDecimalPlaces(((double)value / count) * 100) + "%";
                    }else {
                        s3 = "订单数比例：" + 0.0 + "%";
                    }
                    order.setFormatted(s3);
                    break;
                case 4:
                    order.setLabel("退货");
                    String s4;
                    if (count>0) {
                        s4 = "订单数比例：" + Message.twoDecimalPlaces(((double)value / count) * 100) + "%";
                    }else {
                        s4 = "订单数比例：" + 0.0 + "%";
                    }
                    order.setFormatted(s4);
                    break;
            }
        }
        return list;
    }

    /**
     * 根据商品类型获取平台的商品数量
     * 柱状图的数据接口
     * @return
     */
    @ResponseBody
    @PostMapping("/getGoodsNum_ByState")
    private List<goodsnumBygoodstype> getGoodsNum_ByState(){
        ArrayList<goodsnumBygoodstype> list = new ArrayList<>();
        List<goods_type> all = goodsTypeRepository.findAll();
        for (goods_type type : all) {
            Integer i = goodsRepository.countByGoods_type(type.getType());
            if (i>=0) {
                goodsnumBygoodstype gn = new goodsnumBygoodstype();
                gn.setType(type.getType());
                gn.setNum(i);
                list.add(gn);
            }
        }
        return list;
    }

}
