package com.zhangheng.happy_shopping.web.controller_adm;

import com.zhangheng.happy_shopping.android.entity.Customer;
import com.zhangheng.happy_shopping.android.entity.submitgoods.SubmitGoods;
import com.zhangheng.happy_shopping.android.repository.CustomerRepository;
import com.zhangheng.happy_shopping.android.repository.SubmitGoodsRepository;
import com.zhangheng.happy_shopping.utils.EncryptUtil;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.PhoneNumUtil;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import com.zhangheng.happy_shopping.web.controller_adm.bean.CustomerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * 顾客管理界面
 * @author 张恒
 * @program: happy_shopping
 * @email zhangheng.0805@qq.com
 * @date 2022-02-21 17:36
 */
@Controller
public class CustomersListController {
    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SubmitGoodsRepository submitGoodsRepository;
    @Value("${customer_reset_pwd}")
    private String reset_pwd = Customer.reset_pwd;

    /**
     * 跳转至顾客管理界面
     * @param model
     * @return
     */
    @GetMapping("/customersListPage")
    private String customersListPage(Model model){
        model.addAttribute("active",3);
        return "administrator/customersList";
    }

    /**
     * 查询所有的顾客信息
     * @return
     */
    @ResponseBody
    @PostMapping("/getCustomerssList")
    private List<CustomerInfo> getMerchantsList(){
        List<CustomerInfo> customers = new ArrayList<>();
        List<Customer> all = customerRepository.findAll();
        for (Customer c : all) {
            CustomerInfo customerInfo = new CustomerInfo();
            customerInfo.setPhone(c.getPhone());
            customerInfo.setUsername(c.getUsername());
            customerInfo.setIcon(c.getIcon());
            customerInfo.setAddress(c.getAddress());
            customerInfo.setSex(c.getSex());
            customerInfo.setState(c.getState());
            customerInfo.setTime(String.valueOf(TimeUtil.daysDifference(TimeUtil.time(new Date()),c.getTime())));
            List<SubmitGoods> allByPhone = submitGoodsRepository.findAllByPhone(c.getPhone());
            int order_num=allByPhone.size();
            double order_price=0.00;
            for (SubmitGoods submitGoods : allByPhone) {
                order_price+=submitGoods.getCount_price();
            }
            customerInfo.setOrderNum(order_num);
            customerInfo.setOrderPrice(Message.twoDecimalPlaces(order_price));
            customers.add(customerInfo);
        }
        return customers;
    }

    /**
     * 修改顾客的账号状态
     * @param phone 顾客手机号
     * @param state 修改的状态
     * @return
     */
    @ResponseBody
    @PostMapping("/set_customerState")
    private Message set_customerState(String phone,int state){
        Message msg = new Message();
        msg.setTime(TimeUtil.time(new Date()));
        if (PhoneNumUtil.isMobile(phone)){
            if (state==0||state==1){
                int i = customerRepository.updateStateByPhone(state, phone);
                if (i>0){
                    msg.setCode(200);
                    msg.setMessage("修改成功！");
                }else {
                    msg.setCode(500);
                    msg.setMessage("修改失败！");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("修改状态错误！");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("顾客手机号格式错误！");
        }
        return msg;
    }

    /**
     * 重置顾客账号的密码为初始密码
     * @param phone 顾客的手机号
     * @return
     */
    @ResponseBody
    @PostMapping("/reset_customer_pwd")
    private Message reset_customer_pwd(String phone){
        Message msg = new Message();
        msg.setTime(TimeUtil.time(new Date()));
            if (PhoneNumUtil.isMobile(phone)){
                Optional<Customer> byId = customerRepository.findById(phone);
                if (byId.isPresent()){

                    Message.printLog(reset_pwd);
                    int i = customerRepository.updatePasswordByPhone(EncryptUtil.getMyMd5(reset_pwd), phone);
                    if (i>0){
                        msg.setCode(200);
                        msg.setMessage("密码重置成功！初始密码为："+ reset_pwd);
                    }else {
                        msg.setCode(500);
                        msg.setMessage("密码重置失败！");
                    }
                }else {
                    msg.setCode(500);
                    msg.setMessage("对不起！该账户不存在");
                }
        }else {
            msg.setCode(500);
            msg.setMessage("顾客手机号格式错误！");
        }
        return msg;
    }


}
