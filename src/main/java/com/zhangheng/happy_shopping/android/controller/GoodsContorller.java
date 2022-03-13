package com.zhangheng.happy_shopping.android.controller;

import com.google.gson.Gson;
import com.zhangheng.happy_shopping.android.entity.Customer;
import com.zhangheng.happy_shopping.android.entity.Goods;
import com.zhangheng.happy_shopping.android.entity.Store;
import com.zhangheng.happy_shopping.android.entity.submitgoods.SubmitGoods;
import com.zhangheng.happy_shopping.android.entity.submitgoods.goods;
import com.zhangheng.happy_shopping.android.repository.*;
import com.zhangheng.happy_shopping.utils.CusAccessObjectUtil;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.PhoneNumUtil;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 顾客操作商品
 */
@RequestMapping("Goods")
@Controller
public class GoodsContorller {
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SubmitGoodsRepository submitGoodsRepository;
    @Autowired
    private ListGoodsRepository listGoodsRepository;
    @Autowired
    private StoreRepository storeRepository;
    private Logger log = LoggerFactory.getLogger(getClass());


    /**
     * 根据类型名称检索商品
     * @param GoodsType
     * @return
     */
    @PostMapping("allgoodslist")
    @ResponseBody
    public List<Goods> getAllList(@Nullable @RequestParam String GoodsType,@Nullable @RequestParam String Name, HttpServletRequest request){
        List<Goods> goods = new ArrayList<>();
//        log.info("获取商品列表请求："+ CusAccessObjectUtil.getIpAddress(request)+"/"+CusAccessObjectUtil.getUser_Agent(request));
//        log.info("查询商品类型："+GoodsType);
        if (GoodsType!=null) {
            if(Name==null||Name.replace(" ","").length()<=0) {
                if (GoodsType.equals("全部")) {
                    goods = goodsRepository.findByState(0);
                } else {
                    goods = goodsRepository.findByGoods_typeAndState(GoodsType, 0);
                }
            }else {
                if (GoodsType.equals("全部")) {
                    //根据商品名模糊查询
                    goods = goodsRepository.findByStateAndGoods_nameLike(0,Name);
                    if (goods.size()<=0){
                        //根据店铺名模糊查询
                        goods = goodsRepository.findByStateAndStore_nameLike(0,Name);
                    }
                } else {
                    //根据商品类型和商品名模糊查询
                    goods = goodsRepository.findByGoods_typeAndStateAndGoods_nameLike(GoodsType, 0,Name);
                    if (goods.size()<=0){
                        //根据商品类型和店铺名模糊查询
                        goods = goodsRepository.findByGoods_typeAndStateAndStore_nameLike(GoodsType,0,Name);
                    }
                }
            }
        }
        return goods;
    }

    /**
     * 顾客提交商品订单
     * @param submitGoodsList
     * @return
     */
    @PostMapping("submitgoodslist")
    @ResponseBody
    public Message submit(@RequestParam String submitGoodsList){
        Message msg = new Message();
        if (submitGoodsList!=null) {
            Gson gson = new Gson();
            SubmitGoods submitGoods = gson.fromJson(submitGoodsList, SubmitGoods.class);
            Optional<Customer> byId1 = customerRepository.findById(submitGoods.getPhone());
            if (byId1.isPresent()) {
                if (byId1.get().getState()==0) {
                    List<goods> goods_list = submitGoods.getGoods_list();
                    List<Integer> id = new ArrayList<>();
                    List<Integer> n = new ArrayList<>();
                    for (goods g : goods_list) {
                        id.add(g.getGoods_id());
                        n.add(g.getNum());
                        g.setState(0);
                    }
                    List<Goods> allById = goodsRepository.findAllById(id);//根据订单的商品id查询商品信息
                    double count = 0;
                    //计算订单总价
                    for (Goods goods : allById) {
                        for (goods g : goods_list) {
                            if (g.getGoods_id().equals(goods.getGoods_id())) {
                                count += goods.getGoods_price() * g.getNum();
                            }
                        }
                    }
                    BigDecimal bg = new BigDecimal(count);
                    count = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
                    log.info("订单：" + submitGoods.getSubmit_id() + "，总金额：" + count);
                    if (count == submitGoods.getCount_price()) {
                        //修改商品库存
                        for (goods g : goods_list) {
                            Optional<Goods> byId = goodsRepository.findById(g.getGoods_id());
                            if (byId.get().getGoods_num() >= g.getNum()) {
                                //减少商品的库存
                                goodsRepository.updateGoods_num(-g.getNum(), g.getGoods_id());
                            } else {
                                msg.setCode(500);
                                msg.setTitle(g.getGoods_name() + "的库存不足");
                                msg.setMessage("商品：[" + g.getGoods_name() + "]的库存还有" + byId.get().getGoods_num() + "件，请刷新后重新选购");
                                return msg;
                            }
                        }
                        SubmitGoods submitGoods1 = submitGoodsRepository.saveAndFlush(submitGoods);
                        if (submitGoods1 != null) {
                            msg.setCode(200);
                            msg.setMessage("订单提交成功");
                        } else {
                            msg.setTitle("订单提交失败");
                            msg.setCode(500);
                        }

                    } else {
                        msg.setTitle("订单提交失败");
                        msg.setMessage("价格异常,订单提交失败");
                        msg.setCode(500);
                    }
                }else {
                    msg.setTitle("账号异常");
                    msg.setCode(500);
                    msg.setMessage("对不起，您的账号状态异常，请前往“我的”查看或重新登录");
                }
            }else {
                msg.setTitle("订单手机号错误");
                msg.setCode(500);
                msg.setMessage("订单手机号必须为顾客账号的手机号");
            }
        }else {
            msg.setTitle("订单提交失败");
            msg.setCode(500);
            msg.setMessage("信息提交为空");
        }
        return msg;
    }

    /**
     *  顾客查询自己的订单列表
     * @param cusInfo 顾客的JSON数据(手机号，密码)
     * @return
     */
    @PostMapping("Insert_Order")//根据手机号查询订单
    @ResponseBody
    public List<SubmitGoods> submitGoodsList(@RequestParam("cusInfo") String cusInfo){
        List<SubmitGoods> list=new ArrayList<>();
        //判断提交数据是否为空
        if (cusInfo!=null) {
            Customer customer = new Gson().fromJson(cusInfo, Customer.class);
            //判断手机号格式是否正确
            if (PhoneNumUtil.isMobile(customer.getPhone())) {
                Optional<Customer> byId = customerRepository.findById(customer.getPhone());
                //判断用户是否存在
                if (byId.isPresent()) {
                    list = submitGoodsRepository.findAllByPhone(customer.getPhone());
                }
            }
        }
        return list;
    }
    /**
     * 顾客本人确认收货
     * @param num 订单商品的数量
     * @param submit_id 订单号
     * @param goods_id 订单商品id
     * @param phone 手机号
     * @param password 密码
     * @return
     */
    @PostMapping("OK_Order")//订单商品确认收货
    @ResponseBody
    public Message submitOrderOK(
            @RequestParam("num") int num
            ,@RequestParam("submit_id") String submit_id
            ,@RequestParam("phone") String phone
            ,@RequestParam("password") String password
            ,@RequestParam("goods_id") Integer goods_id){
        Message msg = new Message();
        goods goods = new goods();
        //检查手机号格式
        if (PhoneNumUtil.isMobile(phone)) {
            //根据手机号查询顾客
            Optional<Customer> byId = customerRepository.findById(phone);
            //检查用户是否存在
            if (byId.isPresent()) {
                try {
                    //验证密码是否正确
                    if (byId.get().getPassword().equals(password)) {
                        //判断订单商品是否存在
                        Optional<com.zhangheng.happy_shopping.android.entity.submitgoods.goods> list_idAndGoods_id = listGoodsRepository.findByList_idAndGoods_id(submit_id, goods_id);
                        if (list_idAndGoods_id.isPresent()) {
                            if (list_idAndGoods_id.get().getNum().equals(num)) {
                                //根据订单号和商品id修改订单商品状态
                                int i = listGoodsRepository.updateStateByList_idAndGoods_id(3, submit_id, goods_id);
                                //确认收货后根据商品id增加商品的销量
                                goodsRepository.addGoods_Sales(num, goods_id);
                                //计算该订单商品的总价
                                double v = Message.twoDecimalPlaces(list_idAndGoods_id.get().getGoods_price() * num);
                                //根据店铺id查询的店铺
                                Optional<Store> byId1 = storeRepository.findById(list_idAndGoods_id.get().getStore_id());
                                //判断店铺是否存在
                                if (byId1.isPresent()) {
                                    Double turnover = Message.twoDecimalPlaces(byId1.get().getTurnover()+v);
                                    int i1=storeRepository.updateTurnoverByStore_id(turnover, byId1.get().getStore_id());
                                    if (i > 0 || i1 > 0) {
                                        msg.setCode(200);
                                        msg.setTime(TimeUtil.time(new Date()));
                                        msg.setTitle("收货成功");
                                        msg.setMessage(String.valueOf(goods_id));
                                    } else {
                                        msg.setCode(500);
                                        msg.setTime(TimeUtil.time(new Date()));
                                        msg.setTitle("收货失败！");
                                        msg.setMessage("对不起,数据修改失败！");
                                    }
                                }else {
                                    msg.setCode(500);
                                    msg.setTime(TimeUtil.time(new Date()));
                                    msg.setTitle("商品的店铺错误！");
                                    msg.setMessage("对不起,商品的店铺不存在！");
                                }
                            }else {
                                msg.setCode(500);
                                msg.setTime(TimeUtil.time(new Date()));
                                msg.setTitle("商品数量错误！");
                                msg.setMessage("对不起,商品数量错误！");
                            }
                        }else {
                            msg.setCode(500);
                            msg.setTime(TimeUtil.time(new Date()));
                            msg.setTitle("商品不见了！");
                            msg.setMessage("对不起,没有查找到该订单商品的信息！");
                        }
                    }else {
                        msg.setCode(500);
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setTitle("密码错误");
                        msg.setMessage("对不起,您的密码错误");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.setCode(500);
                    msg.setTime(TimeUtil.time(new Date()));
                    msg.setTitle("错误");
                    msg.setMessage(e.getMessage());
                }
            }else {
                msg.setCode(500);
                msg.setTime(TimeUtil.time(new Date()));
                msg.setTitle("用户不存在");
                msg.setMessage("对不起,没有找到用户信息");
            }
        }else {
            msg.setCode(500);
            msg.setTime(TimeUtil.time(new Date()));
            msg.setTitle("信息错误");
            msg.setMessage("账户手机号错误");
        }
        return msg;
    }

    /**
     * 顾客本人拒绝收货（退货）
     * @param submit_id 订单号
     * @param phone 手机号
     * @param password 密码
     * @param goods_id 订单商品id
     * @return
     */
    @PostMapping("NO_Order")//订单商品退货
    @ResponseBody
    public Message submitOrderNO(
            @RequestParam("submit_id") String submit_id
            ,@RequestParam("num") int num
            ,@RequestParam("phone") String phone
            ,@RequestParam("password") String password
            ,@RequestParam("goods_id") Integer goods_id){
        Message msg = new Message();
        msg.setTime(TimeUtil.time(new Date()));
        //判断手机号格式
        if (PhoneNumUtil.isMobile(phone)) {
            Optional<Customer> byId = customerRepository.findById(phone);
            //检查用户是否存在
            if (byId.isPresent()) {
                try {
                    //验证密码是否正确
                    if (byId.get().getPassword().equals(password)) {
                        //修改订单商品的状态
                        listGoodsRepository.updateStateByList_idAndGoods_id(4, submit_id, goods_id);
                        //退货后恢复该商品的库存
                        goodsRepository.updateGoods_num(num,goods_id);
                        msg.setCode(200);
                        msg.setTitle("退货成功");
                        msg.setMessage(String.valueOf(goods_id));
                    }else {
                        msg.setCode(500);
                        msg.setTitle("密码错误");
                        msg.setMessage("对不起,您的密码错误");
                    }
                } catch (Exception e) {
                    msg.setCode(500);
                    msg.setTitle("错误");
                    msg.setMessage(e.getMessage());
                }
            }else {
                msg.setCode(500);
                msg.setTitle("用户不存在");
                msg.setMessage("对不起,没有找到用户信息");
            }
        }else {
            msg.setCode(500);
            msg.setTitle("信息错误");
            msg.setMessage("账户手机号错误");
        }
        return msg;
    }
    @PostMapping("Del_Order")//订单商品删除
    @ResponseBody
    public Message submitOrderDel(
            @RequestParam("submit_id") String submit_id
            ,@RequestParam("num") int num
            ,@RequestParam("phone") String phone
            ,@RequestParam("password") String password
            ,@RequestParam("goods_id") Integer goods_id){
        Message msg = new Message();
        //判断手机号格式
        if (PhoneNumUtil.isMobile(phone)) {
            Optional<Customer> byId = customerRepository.findById(phone);
            //检查用户是否存在
            if (byId.isPresent()) {
                try {
                    //验证密码是否正确
                    if (byId.get().getPassword().equals(password)) {
                        Optional<SubmitGoods> byId1 = submitGoodsRepository.findById(submit_id);
                        if (byId1.isPresent()) {
                            if (byId1.get().getGoods_list().size()==1){
                                if (byId1.get().getGoods_list().get(0).getGoods_id().equals(goods_id)){
                                    //如果订单中的商品只有一个，并且和要删除的订单商品一致，则直接删除该订单
                                    submitGoodsRepository.deleteById(submit_id);
                                    msg.setTime(TimeUtil.time(new Date()));
                                    msg.setCode(200);
                                    msg.setTitle("删除成功");
                                    msg.setMessage(String.valueOf(goods_id));
                                    return msg;
                                }
                            }
                            Integer i = listGoodsRepository.deleteByList_idAndGoods_id(submit_id, goods_id);
                            if (i > 0) {
                                msg.setTime(TimeUtil.time(new Date()));
                                msg.setCode(200);
                                msg.setTitle("删除成功");
                                msg.setMessage(String.valueOf(goods_id));
                            } else {
                                msg.setTime(TimeUtil.time(new Date()));
                                msg.setCode(500);
                                msg.setTitle("退货失败");
                                msg.setMessage("对不起,订单商品删除失败");
                            }
                        }else {
                            msg.setTime(TimeUtil.time(new Date()));
                            msg.setCode(500);
                            msg.setTitle("订单不存在");
                            msg.setMessage("对不起,该订单信息找不到");
                        }
                    }else {
                        msg.setCode(500);
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setTitle("密码错误");
                        msg.setMessage("对不起,您的密码错误");
                    }
                } catch (Exception e) {
                    msg.setTime(TimeUtil.time(new Date()));
                    msg.setCode(500);
                    msg.setTitle("错误");
                    msg.setMessage(e.getMessage());
                }
            }else {
                msg.setTime(TimeUtil.time(new Date()));
                msg.setCode(500);
                msg.setTitle("用户不存在");
                msg.setMessage("对不起,没有找到用户信息");
            }
        }else {
            msg.setTime(TimeUtil.time(new Date()));
            msg.setCode(500);
            msg.setTitle("信息错误");
            msg.setMessage("账户手机号错误");
        }
        return msg;
    }
}
