package com.zhangheng.happy_shopping.android.controller;

import com.google.gson.Gson;
import com.zhangheng.happy_shopping.android.entity.Customer;
import com.zhangheng.happy_shopping.android.entity.Goods;
import com.zhangheng.happy_shopping.android.entity.submitgoods.SubmitGoods;
import com.zhangheng.happy_shopping.android.entity.submitgoods.goods;
import com.zhangheng.happy_shopping.android.repository.CustomerRepository;
import com.zhangheng.happy_shopping.android.repository.GoodsRepository;
import com.zhangheng.happy_shopping.android.repository.ListGoodsRepository;
import com.zhangheng.happy_shopping.android.repository.SubmitGoodsRepository;
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
    private Logger log = LoggerFactory.getLogger(getClass());


    /**
     * 根据类型查询所有商品
     * @param GoodsType
     * @return
     */
    @PostMapping("allgoodslist")
    @ResponseBody
    public List<Goods> getAllList(@Nullable @RequestParam String GoodsType, HttpServletRequest request){
        List<Goods> goods = new ArrayList<>();
        log.info("获取商品列表请求："+ CusAccessObjectUtil.getIpAddress(request)+"/"+CusAccessObjectUtil.getUser_Agent(request));

        log.info("查询商品类型："+GoodsType);
        if (GoodsType!=null) {
            if (GoodsType.equals("全部")) {
                goods = goodsRepository.findAll();
            } else {
                goods = goodsRepository.findByGoods_type(GoodsType);
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
            System.out.println(submitGoods.toString());
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
            log.info("总金额：" + count);
            if (count == submitGoods.getCount_price()) {
                //修改商品库存
                for (goods g : goods_list) {
                    goodsRepository.updateGoods_num(-g.getNum(),g.getGoods_id());
                }
                SubmitGoods submitGoods1 = submitGoodsRepository.saveAndFlush(submitGoods);
                if (submitGoods1 != null) {
                    msg.setCode(200);
                    msg.setMessage("订单提交成功");
                } else {
                    msg.setTitle("订单提交失败");
                    msg.setTitle("订单保存失败");
                    msg.setCode(500);
                }

            } else {
                msg.setTitle("订单提交失败");
                msg.setMessage("价格异常,订单提交失败");
                msg.setCode(500);
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
            Optional<Customer> byId = customerRepository.findById(phone);
            //检查用户是否存在
            if (byId.isPresent()) {
                try {
                    //验证密码是否正确
                    if (byId.get().getPassword().equals(password)) {
                        //根据订单号和商品id修改订单商品状态
                        listGoodsRepository.updateStateByList_idAndGoods_id(3, submit_id, goods_id);
                        //确认收货后根据商品id修改商品的销量
                        goodsRepository.addGoods_Sales(num, goods_id);
                        msg.setCode(200);
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setTitle("收货成功");
                        msg.setMessage(String.valueOf(goods_id));
                    }else {
                        msg.setCode(500);
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setTitle("密码错误");
                        msg.setMessage("对不起,您的密码错误");
                    }
                } catch (Exception e) {
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
        //判断手机号格式
        if (PhoneNumUtil.isMobile(phone)) {
            Optional<Customer> byId = customerRepository.findById(phone);
            //检查用户是否存在
            if (byId.isPresent()) {
                try {
                    //验证密码是否正确
                    if (byId.get().getPassword().equals(password)) {
                        listGoodsRepository.updateStateByList_idAndGoods_id(4, submit_id, goods_id);
                        goodsRepository.updateGoods_num(num,goods_id);
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setCode(200);
                        msg.setTitle("退货成功");
                        msg.setMessage(String.valueOf(goods_id));
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
