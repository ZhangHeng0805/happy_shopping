package com.zhangheng.happy_shopping.web.controller_mer;

import com.zhangheng.happy_shopping.android.entity.Goods;
import com.zhangheng.happy_shopping.android.entity.Merchants;
import com.zhangheng.happy_shopping.android.repository.GoodsRepository;
import com.zhangheng.happy_shopping.controller.FileLoadController;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.TimeUtil;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/*添加商品页面控制器*/
@Controller
public class addGoodsController {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private GoodsTypeRepository goodsTypeRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    /**
     * 跳转至添加商品界面
     * @return
     */
    @GetMapping("/addGoodsPage")
    private String addGoodsPage(Model model){
        model.addAttribute("active", 2);
        return "merchants/addGoods_mer";
    }

    /**
     * 查询所有的商品类型(未拦截)
     * @return
     */
    @ResponseBody
    @GetMapping("/findGoodsType")
    private List<goods_type> findGoodsType(){
        List<goods_type> all = goodsTypeRepository.findAll();
        List<goods_type> list=new ArrayList<>();
        for (goods_type gt : all) {
            if (gt.getId()!=0){
                list.add(gt);
            }
        }
        return list;
    }

    /**
     * 添加商品表单提交
     * @param goods 商品信息
     * @param image 商品图片
     * @param model
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("addGoods")
    private Message addGoods(Goods goods, String image, Model model, HttpServletRequest request) throws Exception {
        Message msg = new Message();
        if (goods!=null && goods.getGoods_name()!=null){
            if (!image.isEmpty()){
                Optional<Goods> byGoods_nameAndGoods_introduction = goodsRepository.findByGoods_nameAndGoods_introduction(goods.getGoods_name(), goods.getGoods_introduction());
                if (!byGoods_nameAndGoods_introduction.isPresent()) {
                    Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
                    String s = new FileLoadController().base64ToImg(image, goods.getGoods_name(), "Goods_Images/"+merchants.getStore_id());
                    if (s != null) {
                        try {
                            goods.setGoods_image(s);//保存图片路径
                            goods.setGoods_sales(0);//销量为0
                            goods.setState(2);//商品审核中
                            goods.setStore_name(merchants.getStore_name());//设置店名
                            goods.setStore_id(merchants.getStore_id());//设置店铺id
                            goods.setTime(TimeUtil.time(new Date()));//设置时间
                            Goods save = goodsRepository.save(goods);
                            if (save!=null) {
                                log.info("店铺《" + save.getStore_name() + "》[" + merchants.getPhonenum() + "]添加商品：" + save.getGoods_name());
                                msg.setMessage("商品：" + save.getGoods_name() + "，成功添加！");
                                msg.setCode(200);
                            }else {
                                msg.setCode(500);
                                msg.setMessage("商品信息添加失败！");
                                new FileLoadController().deleteFile(s); //删除图片
                            }
                        } catch (Exception e) {
                            new FileLoadController().deleteFile(s); //删除图片
                            log.error(e.getMessage());
                            msg.setCode(500);
                            msg.setMessage("错误：" + e.getMessage());
                        }
                    } else {
                        msg.setCode(500);
                        msg.setMessage("图片保存错误，请检查图片");
                    }
                }else {
                    msg.setCode(500);
                    msg.setMessage("该商品已存在，请勿重复添加商品");
                }

            }else {
                msg.setCode(500);
                msg.setMessage("商品图片为空为空");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("表单提交为空");
        }
//        model.addAttribute("msg",msg);
//        model.addAttribute("active", 2);
//        return "merchants/addGoods_mer";
        return msg;
    }
}
