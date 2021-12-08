package com.zhangheng.happy_shopping.web.controller_mer;

import com.zhangheng.happy_shopping.android.entity.Merchants;
import com.zhangheng.happy_shopping.android.entity.submitgoods.SubmitGoods;
import com.zhangheng.happy_shopping.android.entity.submitgoods.goods;
import com.zhangheng.happy_shopping.android.repository.GoodsRepository;
import com.zhangheng.happy_shopping.android.repository.ListGoodsRepository;
import com.zhangheng.happy_shopping.android.repository.StoreRepository;
import com.zhangheng.happy_shopping.android.repository.SubmitGoodsRepository;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import com.zhangheng.happy_shopping.web.controller_mer.data.main.goodsnumBygoodstype;
import com.zhangheng.happy_shopping.web.controller_mer.data.main.goodsnumByoderstate;
import com.zhangheng.happy_shopping.web.controller_mer.data.main.goodsnumBytimeAndtype;
import com.zhangheng.happy_shopping.web.controller_mer.data.main.listgoodsByState;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;


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
    @Autowired
    private SubmitGoodsRepository submitGoodsRepository;

    /**
     * 跳转至商家主页
     * @param model
     * @param request
     * @return
     */
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
//        log.info("根据商品类型查询本店商品数量");
        Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
        ArrayList<goodsnumBygoodstype> list = new ArrayList<>();
        List<goods_type> all = goodsTypeRepository.findAll();
        for (goods_type type : all) {
            Integer i = goodsRepository.countByStore_idAndGoods_type(merchants.getStore_id(), type.getType());
            if (i>=0) {
                goodsnumBygoodstype gn = new goodsnumBygoodstype();
                gn.setType(type.getType());
                gn.setNum(i);
                list.add(gn);
            }
        }
        return list;
    }

    /**
     * 根据商品的订单状态查询本店订单商品数量
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/findGoodsNumByOrderState")
    private List<goodsnumByoderstate> findGoodsNumByOrderState(HttpServletRequest request){
//        log.info("根据订单状态查询本店商品数量");
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

    /**
     * 查询本店总营业额
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/count_price")
    private Double count_price(HttpServletRequest request){
//        log.info("查询本店营业额");
        Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
        //根据店铺id查询订单商品
        List<goods> byStore_id = listGoodsRepository.findByStore_idAndState(merchants.getStore_id(),3);
        double count=0;
        //计算总价
        for (goods g:byStore_id){
            count+=g.getGoods_price()*g.getNum();
        }
        //保留两位小数
        count=Message.twoDecimalPlaces(count);//保留两位小数
        return count;
    }

    /**
     *  查询近几天的订单情况
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/findGoodnumBytimeAndtype")
    private List<goodsnumBytimeAndtype> findGoodnumBytimeAndtype(HttpServletRequest request){
//        log.info("根据时间和订单类型查询本店营业额");
        Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
        ArrayList<goodsnumBytimeAndtype> list = new ArrayList<>();
        //查询近7天的数据
        for (int i=0;i<7;i++){
            String time=TimeUtil.fewDaysAgo(TimeUtil.time(new Date()), 6-i).substring(0,11);
            List<SubmitGoods> timeLike = submitGoodsRepository.findByTimeLike(time);
            goodsnumBytimeAndtype bytimeAndtype = new goodsnumBytimeAndtype();
            bytimeAndtype.setTime(time.substring(8));
//            double count_price=0;//计算当天总收益
            //遍历当天的订单
            for (SubmitGoods submitGoods : timeLike) {
                List<goods> goods_list = submitGoods.getGoods_list();
                //遍历订单中的商品
                for (goods g : goods_list) {
                    //判断订单商品是否属于本店
                    if (g.getStore_id().equals(merchants.getStore_id())){

                        switch (g.getState()){
                            case 0://待处理
//                                count_price+=g.getGoods_price()*g.getNum();
                                int i0 = bytimeAndtype.getSta0() + 1;
                                bytimeAndtype.setSta0(i0);
                                break;
                            case 1://拒绝发货
//                                count_price+=g.getGoods_price()*g.getNum();
                                int i1 = bytimeAndtype.getSta1()+1;
                                bytimeAndtype.setSta1(i1);
                                break;
                            case 2://待收货
//                                count_price+=g.getGoods_price()*g.getNum();
                                int i2 = bytimeAndtype.getSta2() + 1;
                                bytimeAndtype.setSta2(i2);
                                break;
                            case 3://已收货
//                                count_price+=g.getGoods_price()*g.getNum();
                                int i3 = bytimeAndtype.getSta3() + 1;
                                bytimeAndtype.setSta3(i3);
                                break;
                            case 4://退货
                                int i4 = bytimeAndtype.getSta4() + 1;
                                bytimeAndtype.setSta4(i4);
                                break;
                        }
                    }
                }
            }
//            bytimeAndtype.setCont_price(Message.twoDecimalPlaces(count_price));
            list.add(bytimeAndtype);
        }
        return list;
    }

    /**
     * 查询本店不同订单状态的订单商品
     * @param store_id  店铺id
     * @param state 订单状态
     * @return
     */
    private List<listgoodsByState> findlistgoodsByState(int store_id,int state){
        ArrayList<listgoodsByState> list = new ArrayList<>();
        List<goods> state_list = listGoodsRepository.findByStore_idAndState(store_id, state);
        for (goods g : state_list) {
            listgoodsByState listgoods = new listgoodsByState();
            String list_id = g.getList_id();
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

            list.add(listgoods);
        }

        return list;
    }
    /**
     * 查询本店未处理的订单商品
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/un_goodsOrder")
    private List<listgoodsByState> un_goodsOrder(HttpServletRequest request){
//        log.info("查询本店未处理的订单商品");
        Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
        List<listgoodsByState> list = findlistgoodsByState(merchants.getStore_id(), 0);
        return list;
    }

    /***
     * 商家确认商品订单
     * @param id 订单商品id
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("Ok_Order")
    private Message Ok_Oder(Integer id,HttpServletRequest request){
        Message msg = new Message();
        if (id!=null) {
            Optional<goods> byId = listGoodsRepository.findById(id);
            if (byId.isPresent()) {
                Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
                if (merchants.getStore_id().equals(byId.get().getStore_id())) {
                    if (byId.get().getState()==0) {
                        int i = listGoodsRepository.updateStateById(2, id);
                        if (i>0){
                            msg.setCode(200);
                            msg.setMessage("订单:"+byId.get().getList_id().substring(byId.get().getList_id().indexOf("_")+1)+",确认成功!");
                        }else {
                            msg.setCode(500);
                            msg.setMessage("错误，订单确认失败");
                        }
                    }else {
                        msg.setCode(500);
                        msg.setMessage("错误，该订单已处理过，无法重复处理");
                    }
                }else {
                    msg.setCode(500);
                    msg.setMessage("错误，确认订单非本店商品");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("确认订单商品ID不存在");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("确认ID为空");
        }
        return msg;
    }

    /**
     * 商家拒绝订单
     * @param id 订单商品id
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("No_Order")
    private Message No_Order(Integer id,HttpServletRequest request){
        Message msg = new Message();
        if (id!=null) {
            Optional<goods> byId = listGoodsRepository.findById(id);
            if (byId.isPresent()) {
                Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
                if (merchants.getStore_id().equals(byId.get().getStore_id())) {
                    if (byId.get().getState()==0) {
                        int i = listGoodsRepository.updateStateById(1, id);
                        if (i>0){
                            msg.setCode(200);
                            msg.setMessage("订单:"+byId.get().getList_id().substring(byId.get().getList_id().indexOf("_")+1)+",拒绝成功!");
                        }else {
                            msg.setCode(500);
                            msg.setMessage("错误，订单拒绝失败");
                        }
                    }else {
                        msg.setCode(500);
                        msg.setMessage("错误，该订单已处理过，无法重复处理");
                    }
                }else {
                    msg.setCode(500);
                    msg.setMessage("错误，拒绝订单非本店商品");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("拒绝订单商品ID不存在");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("拒绝ID为空");
        }
        return msg;
    }
}
