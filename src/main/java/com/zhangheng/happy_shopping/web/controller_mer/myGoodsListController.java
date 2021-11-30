package com.zhangheng.happy_shopping.web.controller_mer;

import com.zhangheng.happy_shopping.android.entity.Goods;
import com.zhangheng.happy_shopping.android.entity.Merchants;
import com.zhangheng.happy_shopping.android.repository.GoodsRepository;
import com.zhangheng.happy_shopping.web.controller_mer.data.mygoodslist.GoodByGoodType;
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

/*商家店铺商品列表界面*/
@Controller
public class myGoodsListController {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private GoodsTypeRepository goodsTypeRepository;
    @Autowired
    private GoodsRepository goodsRepository;

    @GetMapping("/myGoodsListpage")
    private String myGoodsListpage(Model model){
        model.addAttribute("active",3);
        return "merchants/myGoodsList_mer";
    }

    @ResponseBody
    @PostMapping("/findGoodsByType")
    private List<GoodByGoodType> findGoodsByType(Integer id,HttpServletRequest request){
        Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
        List<GoodByGoodType> list = new ArrayList<>();
//        log.info("查询的商品类型id:"+id);
        if (id!=null) {
            List<Goods> byStore_id;
            if (id>0){
                //根据店铺id和商品类型查询
                byStore_id = goodsRepository.findByStore_idAndGoods_type(merchants.getStore_id(), goodsTypeRepository.findById(id).get().getType());
            }else {
                //根据店铺id查询全部
                byStore_id = goodsRepository.findByStore_id(merchants.getStore_id());
            }
            for (Goods g : byStore_id) {
                GoodByGoodType goodType = new GoodByGoodType();
                goodType.setId(g.getGoods_id());
                goodType.setGoods_name(g.getGoods_name());
                goodType.setGoods_image(g.getGoods_image());
                goodType.setGoods_introduction(g.getGoods_introduction());
                goodType.setGoods_num(g.getGoods_num());
                goodType.setGoods_price(g.getGoods_price());
                goodType.setGoods_sales(g.getGoods_sales());
                goodType.setGoods_type(g.getGoods_type());
                goodType.setState(g.getState());
                list.add(goodType);
            }
        }
        return list;
    }
}
