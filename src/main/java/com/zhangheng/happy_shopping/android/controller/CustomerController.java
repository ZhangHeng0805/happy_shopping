package com.zhangheng.happy_shopping.android.controller;

import com.google.gson.Gson;
import com.zhangheng.happy_shopping.android.entity.Customer;
import com.zhangheng.happy_shopping.android.entity.ShareLocation;
import com.zhangheng.happy_shopping.android.repository.CustomerRepository;
import com.zhangheng.happy_shopping.android.repository.ShareLocationRepository;
import com.zhangheng.happy_shopping.utils.FolderFileScanner;
import com.zhangheng.happy_shopping.utils.Message;
import com.zhangheng.happy_shopping.utils.PhoneNumUtil;
import com.zhangheng.happy_shopping.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequestMapping("Customer")
@RestController
public class CustomerController {
    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ShareLocationRepository locationRepository;
    private ArrayList<Object> list = new ArrayList<>();

    private static final String[] accont_state={"正常","封禁"};

    /**
     * 顾客登录
     * @param loginJson 登录的JSON数据（手机号，密码）
     * @return
     */
    @PostMapping("Login")
    public Message Login(@Nullable @RequestParam("CustomerLogin") String loginJson){
        Message msg = new Message();
        //判断提交数据不为空
        if (loginJson!=null){
            Gson gson = new Gson();
            Customer user = gson.fromJson(loginJson, Customer.class);
            //判断登录手机号格式是否正确
            if (PhoneNumUtil.isMobile(user.getPhone())){
                //判断密码长度
                if (user.getPassword().length()>=6&&user.getPassword().length()<=18){
                    Optional<Customer> customer = customerRepository.findById(user.getPhone());
                    //判断用户是否操作
                    if (customer.isPresent()){
                        //验证密码是否正确
                        if (customer.get().getPassword().equals(user.getPassword())){
                            if (customer.get().getState()==0) {
                                msg.setCode(200);
                                msg.setTime(TimeUtil.time(new Date()));
                                msg.setTitle("登录成功");
                                msg.setMessage(customer.get().getUsername());
                            }else {
                                msg.setCode(500);
                                msg.setTime(TimeUtil.time(new Date()));
                                msg.setTitle("账号"+accont_state[customer.get().getState()]);
                                msg.setMessage("对不起！您的账号状态为:"+accont_state[customer.get().getState()]+",暂时无法登录。");
                            }
                        }else {
                            msg.setCode(500);
                            msg.setTime(TimeUtil.time(new Date()));
                            msg.setTitle("登录失败");
                            msg.setMessage("密码错误");
                        }
                    }else {
                        msg.setCode(500);
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setTitle("登录失败");
                        msg.setMessage("账号错误，该用户不存在");
                    }

                }else {
                    msg.setCode(500);
                    msg.setTime(TimeUtil.time(new Date()));
                    msg.setTitle("登录失败");
                    msg.setMessage("密码格式错误");
                }
            }else {
                msg.setCode(500);
                msg.setTime(TimeUtil.time(new Date()));
                msg.setTitle("登录失败");
                msg.setMessage("账号格式错误");
            }
        }else {
            msg.setTitle("登录失败");
            msg.setMessage("服务器没有收到登录内容");
        }
        log.info(msg.toString());
        return msg;
    }

    /**
     * 根据手机号，密码获取顾客信息
     * @param customerJson 顾客的JSON数据（手机号，密码）
     * @return
     */
    @PostMapping("getCustomer")
    public Customer getCustomer(@Nullable String customerJson){
        Gson gson = new Gson();
        Customer customer = new Customer();
        Customer cus = gson.fromJson(customerJson, Customer.class);
        //判断提交信息是否为空
        if (cus!=null) {
            Optional<Customer> byId = customerRepository.findById(cus.getPhone());
            //判断用户是否存在
            if (byId.isPresent()) {
                //判断密码是否正确
                if (byId.get().getPassword().equals(cus.getPassword())){
                    customer=byId.get();
                }else {
                    log.info("密码错误");
                }
            }else {
                log.info("用户不存在");
            }
        }else {
            log.info("提交数据为空");
        }
        return customer;
    }

    /**
     * 修改顾客用户名
     * @param customerJSON 顾客的JSON数据（手机号，密码，用户名）
     * @return
     */
    @PostMapping("updateUsername")
    public Message updateUsername(@Nullable String customerJSON){
        Gson gson = new Gson();
        Message msg = new Message();
        if (customerJSON!=null) {
            Customer customer = gson.fromJson(customerJSON, Customer.class);
            if (customer != null) {
                Optional<Customer> byId = customerRepository.findById(customer.getPhone());
                //判断用户是否存在
                if (byId.isPresent()) {
                    //判断用户密码是否正确
                    if (byId.get().getPassword().equals(customer.getPassword())) {
                        //修改用户名
                        byId.get().setUsername(customer.getUsername());
                        customerRepository.saveAndFlush(byId.get());
                        msg.setCode(200);
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setTitle("用户名修改成功");
                        msg.setMessage(customer.getUsername());
                    }else {
                        msg.setCode(500);
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setTitle("密码错误");
                        msg.setMessage("用户密码错误");
                    }
                } else {
                    msg.setCode(500);
                    msg.setTime(TimeUtil.time(new Date()));
                    msg.setTitle("用户不存在");
                    msg.setMessage("对不起没有此账户的信息");
                }
            } else {
                msg.setCode(500);
                msg.setTime(TimeUtil.time(new Date()));
                msg.setTitle("Customer为空");
                msg.setMessage("Customer信息为空");
            }
        }else {
            msg.setCode(500);
            msg.setTime(TimeUtil.time(new Date()));
            msg.setTitle("参数为空");
            msg.setMessage("服务器没有收到请求参数");
        }
        log.info(msg.toString());
        return msg;
    }

    /**
     * 修改顾客头像
     * @param cus 顾客的JSON数据（手机号，密码，头像路径）
     * @return
     */
    @PostMapping("updateIcon")
    public Message updateIcon(@Nullable String cus){
        Gson gson = new Gson();
        Message msg = new Message();
        if (cus!=null) {
            Customer customer = gson.fromJson(cus, Customer.class);
            if (customer != null) {
                Optional<Customer> byId = customerRepository.findById(customer.getPhone());
                //判断用户是否存在
                if (byId.isPresent()) {
                    //判断密码是否正确
                    if (byId.get().getPassword().equals(customer.getPassword())) {
                        //修改头像
                        byId.get().setIcon(customer.getIcon());
                        Customer customer1 = customerRepository.saveAndFlush(byId.get());
                        msg.setCode(200);
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setTitle("头像修改成功");
                        msg.setMessage(customer1.getIcon());//返回新的头像路径
                    }else {
                        msg.setCode(500);
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setTitle("密码错误");
                        msg.setMessage("用户密码错误");
                    }
                } else {
                    msg.setCode(500);
                    msg.setTime(TimeUtil.time(new Date()));
                    msg.setTitle("用户不存在");
                    msg.setMessage("对不起没有此账户的信息");
                }
            } else {
                msg.setCode(500);
                msg.setTime(TimeUtil.time(new Date()));
                msg.setTitle("Customer为null");
                msg.setMessage("用户信息为空");
            }
        }else {
            msg.setCode(500);
            msg.setTime(TimeUtil.time(new Date()));
            msg.setTitle("参数为空");
            msg.setMessage("服务器没有收到请求参数");
        }
        log.info(msg.toString());
        return msg;
    }

    /**
     * 顾客修改密码
     * @param json 顾客的JSON数据(手机号，密码，用户名)
     * @return
     */
    @PostMapping("updatePassWord")
    public Message updatePassWord(@Nullable String json){
        Message msg = new Message();
        Gson gson = new Gson();
        if (json!=null) {
            Customer customer = gson.fromJson(json, Customer.class);
            if (customer != null) {
                Optional<Customer> byId = customerRepository.findById(customer.getPhone());
                //判断用户是否存在
                if (byId.isPresent()) {
                    //判断新旧密码是否相同
                    if (!byId.get().getPassword().equals(customer.getPassword())) {
                        //判断用户名是否正确
                        if (byId.get().getUsername().equals(customer.getUsername())) {
                            byId.get().setPassword(customer.getPassword());//设置新密码
                            Customer customer1 = customerRepository.saveAndFlush(byId.get());
                            msg.setCode(200);
                            msg.setTime(TimeUtil.time(new Date()));
                            msg.setTitle("密码修改成功");
                            msg.setMessage(customer1.getPassword());
                        }else {
                            msg.setCode(500);
                            msg.setTime(TimeUtil.time(new Date()));
                            msg.setTitle("用户名错误");
                            msg.setMessage("用户名错误");
                        }
                    }else {
                        msg.setCode(500);
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setTitle("新旧密码相同");
                        msg.setMessage("新旧密码不能相同");
                    }
                } else {
                    msg.setCode(500);
                    msg.setTime(TimeUtil.time(new Date()));
                    msg.setTitle("用户不存在");
                    msg.setMessage("对不起没有此账户的信息");
                }
            } else {
                msg.setCode(500);
                msg.setTime(TimeUtil.time(new Date()));
                msg.setTitle("Customer为null");
                msg.setMessage("用户信息为空");
            }
        }else {
            msg.setCode(500);
            msg.setTime(TimeUtil.time(new Date()));
            msg.setTitle("参数为空");
            msg.setMessage("服务器没有收到请求参数");
        }
        log.info(msg.toString());
        return msg;
    }

    /**
     * 设置顾客的地址
     * @param json 顾客的JSON数据(手机号，密码，地址)
     * @return
     */
    @PostMapping("setAddress")
    public Message setAddress(@Nullable String json){
        Message msg=new Message();
        Gson gson = new Gson();
        if(json!=null) {
            Customer customer = gson.fromJson(json, Customer.class);
            //判断手机号格式正确
            if (PhoneNumUtil.isMobile(customer.getPhone())) {
                //判断地址是否过短
                if (customer.getAddress().length() > 3) {
                    Optional<Customer> byId = customerRepository.findById(customer.getPhone());
                    //判断用户是否存在
                    if (byId.isPresent()) {
                        //验证密码
                        if (byId.get().getPassword().equals(customer.getPassword())) {
                            byId.get().setAddress(customer.getAddress());//修改地址
                            customerRepository.saveAndFlush(byId.get());
                            msg.setTime(TimeUtil.time(new Date()));
                            msg.setCode(200);
                            msg.setTitle("保存成功");
                            msg.setMessage("地址保存成功");
                        }else {
                            msg.setTime(TimeUtil.time(new Date()));
                            msg.setCode(500);
                            msg.setTitle("密码错误");
                            msg.setMessage("用户密码错误");
                        }
                    } else {
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setCode(500);
                        msg.setTitle("用户不存在");
                        msg.setMessage("账号错误，该账号的用户不存在");
                    }

                } else {
                    msg.setTime(TimeUtil.time(new Date()));
                    msg.setCode(500);
                    msg.setTitle("地址为空");
                    msg.setMessage("地址过短或为空");
                }
            } else {
                msg.setTime(TimeUtil.time(new Date()));
                msg.setCode(500);
                msg.setTitle("手机号格式错误");
                msg.setMessage("手机号的长度错误");
            }
        }else {
            msg.setCode(500);
            msg.setTime(TimeUtil.time(new Date()));
            msg.setTitle("参数为空");
            msg.setMessage("服务器没有收到请求参数");
        }
        return msg;
    }

    /**
     * 获取用户头像集合
     * @return
     */
    @GetMapping("customericonlist")
    public List<String> UserIcon(){
        ArrayList<String> iconList=new ArrayList<>();
        iconList.clear();
        try {
            list.clear();
            //扫描该文件夹下的所有文件
            list= FolderFileScanner.scanFilesWithNoRecursion("files/customer");
            for (Object o:list){
                String s1="";
                String s= (String) o;
                if (s.indexOf("files") > 1) {
                    s1 = s.substring(s.indexOf("files") + 6);
                } else {
//                    s1 = s;
                }
                s1 = s1.replace("\\", "/");
                if (s1.length()>8) {
                    iconList.add(s1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iconList;
    }

    /**
     * 顾客注册接口
     * @param json
     * @return
     */
    @PostMapping("register")
    public Message zhuce(@Nullable @RequestParam("customerJson") String json){
        Message msg = new Message();
        if (json!=null) {
            Gson gson = new Gson();
            Customer customer = gson.fromJson(json, Customer.class);
            //判断手机号和密码是否为空
            if (customer.getPhone().length() > 0 && customer.getPassword().length() > 0) {
                //判断手机号格式是否正确
                if (PhoneNumUtil.isMobile(customer.getPhone())) {
                    Optional<Customer> byId = customerRepository.findById(customer.getPhone());
                    //判断用户是否存在
                    if (!byId.isPresent()) {
                        Customer save = customerRepository.save(customer);
                        msg.setCode(200);
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setTitle("注册成功");
                        msg.setMessage("恭喜：" + save.getUsername() + "注册成功！");
                    } else {
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setCode(500);
                        msg.setTitle("注册失败");
                        msg.setMessage("此手机号码已存在");
                    }
                }else {
                    msg.setTime(TimeUtil.time(new Date()));
                    msg.setCode(500);
                    msg.setMessage("手机号格式错误");
                    msg.setTitle("请输入正确的手机号");
                }
            } else {
                msg.setTime(TimeUtil.time(new Date()));
                msg.setCode(500);
                msg.setMessage("注册内容为空");
                msg.setTitle("注册失败");
            }
        }else {
            msg.setTime(TimeUtil.time(new Date()));
            msg.setCode(500);
            msg.setTitle("注册失败");
            msg.setMessage("服务器没有收到注册内容");
        }
        log.info(msg.toString());
        return msg;
    }

    /**
     * 顾客位置接口
     * @param location
     * @return
     */
    @PostMapping("share_location")
    public List<ShareLocation> location(String location){
        if (location!=null) {
            Gson gson = new Gson();
            ShareLocation shareLocation = gson.fromJson(location, ShareLocation.class);
            if (PhoneNumUtil.isMobile(shareLocation.getPhone())) {
                Optional<Customer> byId = customerRepository.findById(shareLocation.getPhone());
                if (byId.isPresent()) {
//                    log.info("位置共享：" + shareLocation);
                    ShareLocation save = locationRepository.saveAndFlush(shareLocation);
                    List<ShareLocation> locations = new ArrayList<>();
                    locations = locationRepository.findAll();
                    return locations;
                } else {
                    log.warn("位置共享：用户不存在");
                }
            }else {
                log.error("位置共享：手机号格式异常");
            }
        }else {
            log.error("位置共享：接收数据为null");

        }
        return null;
    }

}
