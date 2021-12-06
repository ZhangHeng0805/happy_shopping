package com.zhangheng.happy_shopping.web.controller_adm;

import com.zhangheng.happy_shopping.android.entity.Goods;
import com.zhangheng.happy_shopping.android.repository.GoodsRepository;
import com.zhangheng.happy_shopping.utils.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/*审核处理商家商品*/
@Controller
public class HandleGoodsController {
    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    private GoodsRepository goodsRepository;

    @GetMapping("/handle_goodsPage")
    private String handle_goodsPage(Model model){
        model.addAttribute("active",1);
        return "administrator/handleGoods_adm";
    }

    @ResponseBody
    @PostMapping("/get_sta2Goods")
    private List<Goods> get_sta2Goods(HttpServletRequest request){
        List<Goods> byState = goodsRepository.findByState(2);
        return byState;
    }
    @ResponseBody
    @PostMapping("/setState_sta2Goods")
    private Message setState_sta2Goods(int goods_id,int state){
        Message msg = new Message();
        if (state==0 || state==3){
            if (goods_id>0){
                Optional<Goods> byId = goodsRepository.findById(goods_id);
                if (byId.isPresent()){
                    if (!byId.get().getState().equals(state)){
                        int i = goodsRepository.updateGoods_StateByGoods_id(state, goods_id);
                        if (i>0){
                            msg.setCode(200);
                            msg.setMessage("商品状态变更成功！");
                        }else {
                            msg.setCode(200);
                            msg.setMessage("商品状态变更失败！");
                        }
                    }else {
                        msg.setCode(500);
                        msg.setMessage("商品状态已修改，请勿重复修改！");
                    }
                }else {
                    msg.setCode(500);
                    msg.setMessage("商品不存在！");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("商品id错误！");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("商品状态错误！");
        }
        return msg;
    }
}
