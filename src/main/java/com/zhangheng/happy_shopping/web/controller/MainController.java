package com.zhangheng.happy_shopping.web.controller;

import com.zhangheng.happy_shopping.android.entity.Merchants;
import com.zhangheng.happy_shopping.android.repository.GoodsRepository;
import com.zhangheng.happy_shopping.android.repository.ListGoodsRepository;
import com.zhangheng.happy_shopping.android.repository.StoreRepository;
import com.zhangheng.happy_shopping.web.controller.data.goodsnumBygoodstype;
import com.zhangheng.happy_shopping.web.controller.data.goodsnumByoderstate;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/*商家主页*/
@Controller
public class MainController {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private GoodsTypeRepository goodsTypeRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ListGoodsRepository listGoodsRepository;

    @GetMapping("/main")
    private String main(Model model, HttpServletRequest request){
        model.addAttribute("active",0);
        return "merchants/main_mer";
    }

    /**
     * 根据商品类型查询本店商品数量
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/findGoodsNumByType")
    private List<goodsnumBygoodstype> findGoodsNumByType(HttpServletRequest request){
        log.info("根据商品类型查询本店商品数量");
        Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
        ArrayList<goodsnumBygoodstype> list = new ArrayList<>();
        List<goods_type> all = goodsTypeRepository.findAll();
        for (goods_type type : all) {
            Integer i = goodsRepository.countByStore_idAndGoods_type(merchants.getStore_id(), type.getType());
            if (i>0) {
                goodsnumBygoodstype gn = new goodsnumBygoodstype();
                gn.setType(type.getType());
                gn.setNum(i);
                list.add(gn);
            }
        }
        return list;
    }

    @ResponseBody
    @PostMapping("/findGoodsNumByOrderState")
    private List<goodsnumByoderstate> findGoodsNumByOrderState(HttpServletRequest request){
        log.info("根据订单状态查询本店商品数量");
        Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
        ArrayList<goodsnumByoderstate> list = new ArrayList<>();
        for (int i=0;i<=4;i++) {
            Integer num = listGoodsRepository.countByStore_idAndState(merchants.getStore_id(), i);
            goodsnumByoderstate byoderstate = new goodsnumByoderstate();
            byoderstate.setState(i);
            byoderstate.setNum(num);
            list.add(byoderstate);
        }
        return list;
    }
}
