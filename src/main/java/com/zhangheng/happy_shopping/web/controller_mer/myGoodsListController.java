package com.zhangheng.happy_shopping.web.controller_mer;

import com.zhangheng.happy_shopping.android.entity.Goods;
import com.zhangheng.happy_shopping.android.entity.Merchants;
import com.zhangheng.happy_shopping.android.repository.GoodsRepository;
import com.zhangheng.happy_shopping.controller.FileLoadController;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.TimeUtil;
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
import java.util.*;

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

    /**
     * 修改商品状态
     * @param id
     * @param state
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/updata_GoodsState")
    private Message updata_GoodsState(int id,int state,HttpServletRequest request){
        Message msg = new Message();
        if (id!=0){
            Optional<Goods> byId = goodsRepository.findById(id);
            if (byId.isPresent()){
                Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
                if (byId.get().getStore_id().equals(merchants.getStore_id())){
                    if (!byId.get().getState().equals(state)){
                        if (state>=0&&state<=3) {
                            int i = goodsRepository.updateGoods_StateByGoods_id(state, id);
                            if (i>0){
                                msg.setCode(200);
                                msg.setMessage("修改成功！");
                            }else {
                                msg.setCode(500);
                                msg.setMessage("对不起！商品的状态修改失败");
                            }
                        }else {
                            msg.setCode(500);
                            msg.setMessage("对不起！商品的状态错误");
                        }
                    }else {
                        msg.setCode(500);
                        msg.setMessage("对不起！该商品的状态修改过，请勿重复修改");
                    }
                }else {
                    msg.setCode(500);
                    msg.setMessage("对不起！该商品不是您店铺的商品，无权修改");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("对不起！没有找到该商品信息");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("提交信息为空！");
        }
        return msg;
    }
    @ResponseBody
    @PostMapping("/delete_Goods")
    private Message delete_Goods(Integer id, HttpServletRequest request){
        Message msg = new Message();
        if (id!=0){
            Optional<Goods> byId = goodsRepository.findById(id);
            if (byId.isPresent()){
                Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
                if (byId.get().getStore_id().equals(merchants.getStore_id())){
                    int i = goodsRepository.deleteByGoods_id(id);
                    if (i>0){
                        new FileLoadController().deleteImg(byId.get().getGoods_image());
                        msg.setCode(200);
                        msg.setMessage("商品删除成功！");
                    }else {
                        msg.setCode(500);
                        msg.setMessage("对不起！商品的状态删除失败");
                    }
                }else {
                    msg.setCode(500);
                    msg.setMessage("对不起！该商品不是您店铺的商品，无权删除");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("对不起！没有找到该商品信息");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("提交信息为空！");
        }
        return msg;
    }
    @ResponseBody
    @PostMapping("/update_goods")
    private Message update_goods(Goods goods,String image,HttpServletRequest request){
        Message msg = new Message();
        String oldImg="";
        if (goods!=null && goods.getGoods_id()!=null){
            Optional<Goods> byId = goodsRepository.findById(goods.getGoods_id());
            if (byId.isPresent()){
//                log.info(byId.get().toString());
                oldImg=byId.get().getGoods_image();
                Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
                if (byId.get().getStore_id().equals(merchants.getStore_id())){
                    goods.setGoods_sales(byId.get().getGoods_sales());
                    goods.setStore_name(byId.get().getStore_name());
                    goods.setStore_id(byId.get().getStore_id());
                    goods.setTime(TimeUtil.time(new Date()));
                    goods.setState(2);
                    if (image.isEmpty()){
                        goods.setGoods_image(byId.get().getGoods_image());
                    }else {
                        String s = new FileLoadController().base64ToImg(image, goods.getGoods_name(), "Goods_Images");
                        if (s==null){
                            msg.setCode(500);
                            msg.setMessage("修改失败，商品图片保存失败！");
                            return msg;
                        }else {
                            goods.setGoods_image(s);
                        }
                    }
                    try {
                        Goods goods1 = goodsRepository.saveAndFlush(goods);
//                        log.info(goods1.toString());
                        msg.setCode(200);
                        msg.setMessage("修改成功!");
                        if (!goods.getGoods_image().equals(oldImg)){
                            //修改成功，删除旧图片
                            new FileLoadController().deleteImg(oldImg);
                        }
                    }catch (Exception e){
                        msg.setCode(500);
                        msg.setMessage("错误："+e.getMessage());
                        if (!goods.getGoods_image().equals(oldImg)){
                            //修改失败，删除新图片
                            new FileLoadController().deleteImg(goods.getGoods_image());
                        }
                    }

                }else {
                    msg.setCode(500);
                    msg.setMessage("修改失败！该商品不是你店铺商品，无法修改");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("错误：商品信息不存在");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("错误：表单提交信息为空");
        }
        return msg;
    }
}
