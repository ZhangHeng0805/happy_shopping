package com.zhangheng.happy_shopping.web.controller_adm;

import com.zhangheng.happy_shopping.android.entity.Goods;
import com.zhangheng.happy_shopping.android.repository.GoodsRepository;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.TimeUtil;
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
 * 商品管理界面
 * @author 张恒
 * @program: happy_shopping
 * @email zhangheng.0805@qq.com
 * @date 2022-02-22 14:34
 */
@Controller
public class GoodsListController {
    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 跳转至商品管理界面
     * @param model
     * @return
     */
    @GetMapping("/goodsListPage")
    private String goodsListPage(Model model){
        model.addAttribute("active",4);
        return "administrator/goodsList";
    }

    @ResponseBody
    @PostMapping("/getGoodsList")
    private List<Goods> getGoodsList(){
        List<Goods> list=new ArrayList<>();
        List<Goods> all = goodsRepository.findAll();
        list=all;
        return list;
    }

    @ResponseBody
    @PostMapping("/set_goodsState")
    private Message set_goodsState(Integer id,Integer state){
        Message msg = new Message();
        msg.setTime(TimeUtil.time(new Date()));
        if (state>=0&&state<=3){
            Optional<Goods> byId = goodsRepository.findById(id);
            if (byId.isPresent()){
                int i = goodsRepository.updateGoods_StateByGoods_id(state, id);
                if (i>0){
                    msg.setCode(200);
                    msg.setMessage("商品状态更新成功！");
                }else {
                    msg.setCode(500);
                    msg.setMessage("商品状态修改失败！");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("商品不存在！");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("修改状态错误！");
        }
        return msg;
    }
}
