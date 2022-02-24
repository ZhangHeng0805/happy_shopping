package com.zhangheng.happy_shopping.web.controller_adm;

import com.zhangheng.happy_shopping.android.entity.Goods;
import com.zhangheng.happy_shopping.android.repository.GoodsRepository;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 商品类型管理界面
 * @author 张恒
 * @program: happy_shopping
 * @email zhangheng.0805@qq.com
 * @date 2022-02-24 10:34
 */
@Controller
public class GoodsTypeController {
    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    private GoodsTypeRepository goodsTypeRepository;

    /**
     * 跳转至商品管理界面
     * @param model
     * @return
     */
    @GetMapping("/goodsTypePage")
    private String goodsListPage(Model model){
        model.addAttribute("active",5);
        return "administrator/goodsType";
    }

    @ResponseBody
    @PostMapping("/update_goodsType")
    private Message update_goodsType(Integer id,String type){
        Message msg = new Message();
        msg.setTime(TimeUtil.time(new Date()));
        if (id>0&&type!=null){
            Optional<goods_type> byId = goodsTypeRepository.findById(id);
            if (byId.isPresent()){
                int i = goodsTypeRepository.updateTypeById(type, id);
                if (i>0){
                    msg.setCode(200);
                    msg.setMessage("商品类型修改成功！");
                }else {
                    msg.setCode(500);
                    msg.setMessage("商品类型修改失败！");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("商品类型不存在");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("接收数据为null");
        }
        return msg;
    }
    @ResponseBody
    @PostMapping("/del_goodsType")
    private Message del_goodsType(Integer id){
        Message msg = new Message();
        msg.setTime(TimeUtil.time(new Date()));
        if (id>0){
            int i = goodsTypeRepository.delById(id);
            if (i>0){
                msg.setCode(200);
                msg.setMessage("商品类型删除成功！");
            }else {
                msg.setCode(500);
                msg.setMessage("商品类型删除失败！");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("id错误");
        }
        return msg;
    }
    @ResponseBody
    @PostMapping("/add_goodsType")
    private Message add_goodsType(String type){
        Message msg = new Message();
        msg.setTime(TimeUtil.time(new Date()));
        if (type!=null&&type.length()>0){
            Optional<goods_type> byType = goodsTypeRepository.findByType(type);
            if (!byType.isPresent()){
                goods_type gt = new goods_type();
                gt.setType(type);
                goods_type type1 = goodsTypeRepository.saveAndFlush(gt);
                if (type1!=null&&type1.getType().length()>0){
                    msg.setCode(200);
                    msg.setMessage("["+type1.getType()+"]商品类型新增成功！");
                }else {
                    msg.setCode(500);
                    msg.setMessage("商品类型新增失败！");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("["+type+"]商品类型已存在");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("商品类型不能为null");
        }
        return msg;
    }
}
