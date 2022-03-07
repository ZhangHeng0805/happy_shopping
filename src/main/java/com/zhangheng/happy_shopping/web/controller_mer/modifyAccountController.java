package com.zhangheng.happy_shopping.web.controller_mer;

import com.zhangheng.happy_shopping.android.entity.Merchants;
import com.zhangheng.happy_shopping.android.entity.Store;
import com.zhangheng.happy_shopping.android.repository.MerchantsRepository;
import com.zhangheng.happy_shopping.android.repository.StoreRepository;
import com.zhangheng.happy_shopping.bean.VerificationCode;
import com.zhangheng.happy_shopping.controller.FileLoadController;
import com.zhangheng.happy_shopping.repository.CodeRepository;
import com.zhangheng.happy_shopping.utils.EncryptUtil;
import com.zhangheng.happy_shopping.utils.Message;
import org.aspectj.apache.bcel.classfile.Code;
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


/*商家修改账号控制器*/
@Controller
public class modifyAccountController {

    private Logger log=LoggerFactory.getLogger(getClass());
    @Autowired
    private MerchantsRepository merchantsRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CodeRepository codeRepository;

    /**
     * 跳转至账号设置界面
     * @return
     */
    @GetMapping("/modify_accountPage")
    private String modify_accountPage(){
        return "merchants/modifyAccount_mer";
    }


    /**
     * 设置账号表单提交
     * @param mer 商家信息
     * @param sto 店铺信息
     * @param image 店铺图片base64
     * @param request
     * @param model
     * @return
     */
    @PostMapping("/modify_account")
    private String modify_account(Merchants mer, Store sto, String image,HttpServletRequest request, Model model){
        Message msg = new Message();
        if (mer!=null && sto!=null){
            Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
            Store store = (Store) request.getSession().getAttribute("store");
            //验证密码是否正确
            if (mer.getPassword().equals(merchants.getPassword())){
                //补充商家信息
                mer.setPhonenum(merchants.getPhonenum());
                mer.setStore_id(merchants.getStore_id());
                mer.setTime(merchants.getTime());
                mer.setState(merchants.getState());
                //补充店铺信息
                sto.setStore_id(store.getStore_id());
                sto.setBoss_name(store.getBoss_name());
                sto.setStart_time(store.getStart_time());
                sto.setTurnover(store.getTurnover());
                //判断是否上传图片
                if (image.isEmpty()){//没有上传
                    sto.setStore_image(store.getStore_image());
                }else {
                    String s = new FileLoadController().base64ToImg(image, sto.getStore_name(), "Store_Images");
                    if (s!=null){
                        //设置新的店铺图片
                        sto.setStore_image(s);
                    }else {
                        msg.setCode(500);
                        msg.setMessage("图片保存失败！");
                        model.addAttribute("msg",msg);
                        return "merchants/modifyAccount_mer";
                    }
                }
                try {
                    Merchants merchants1 = merchantsRepository.saveAndFlush(mer);
                    Store store1 = storeRepository.saveAndFlush(sto);
                    //判断照片是否更改
                    if (!sto.getStore_image().equals(store.getStore_image())){
                        //删除旧店铺图片
                        new FileLoadController().deleteImg(store.getStore_image());
                    }
                    request.getSession().setAttribute("merchants",merchants1);
                    request.getSession().setAttribute("store",store1);
                    msg.setCode(200);
                    msg.setMessage("账户："+merchants1.getAccount()+"，设置成功！");
                }catch (Exception e){
                    //判断照片是否更改
                    if (!sto.getStore_image().equals(store.getStore_image())){
                        //异常删除新店铺图片
                        new FileLoadController().deleteImg(sto.getStore_image());
                    }
                    msg.setCode(500);
                    msg.setMessage("错误："+e.getMessage());
                }
            }else {
                msg.setCode(500);
                msg.setMessage("密码错误");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("表单提交信息为空");
        }
        model.addAttribute("msg",msg);
        return "merchants/modifyAccount_mer";
    }

    /**
     * 修改密码
     * @param old_pwd
     * @param new_pwd
     * @param code
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/modify_password")
    private Message modify_password(String old_pwd,String new_pwd,VerificationCode code,HttpServletRequest request){
        Message msg = new Message();
        if (old_pwd!=null&&new_pwd!=null) {
            if (code!=null) {
                Optional<VerificationCode> byId = codeRepository.findById(code.getId());
                if (byId.isPresent()) {
                    if (byId.get().getCode().equals(code.getCode())) {
                        Merchants merchants = (Merchants) request.getSession().getAttribute("merchants");
                        if (old_pwd.equals(merchants.getPassword())) {
                            if (!old_pwd.equals(new_pwd)) {
                                int i = merchantsRepository.updatePasswordByPhonenum(new_pwd, merchants.getPhonenum());
                                if (i > 0) {
                                    msg.setCode(200);
                                    msg.setMessage("密码修改成功！请重新登录");
                                } else {
                                    msg.setCode(500);
                                    msg.setMessage("密码修改失败！");
                                }
                            } else {
                                msg.setCode(500);
                                msg.setMessage("新旧密码不能相同!");
                            }
                        } else {
                            msg.setCode(500);
                            msg.setMessage("旧密码错误!");
                        }
                    }else {
                        msg.setCode(500);
                        msg.setMessage("验证码错误!");
                    }
                }else {
                    msg.setCode(500);
                    msg.setMessage("错误：验证码不存在!");
                }
            }else {
                msg.setCode(500);
                msg.setMessage("错误：验证码信息为空!");
            }
        }else {
            msg.setCode(500);
            msg.setMessage("错误：表单提交信息为空!");
        }
        return msg;
    }
    @ResponseBody
    @GetMapping("/Verification_Code")
    private String Verification_Code(){
        List<VerificationCode> all = codeRepository.findAll();
        String id="";
        if (!all.isEmpty()){
            int i = Message.RandomNum(0, all.size() - 1);
            id=all.get(i).getId();
        }
        return id;
    }
}
