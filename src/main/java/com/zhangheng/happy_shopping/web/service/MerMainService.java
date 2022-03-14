package com.zhangheng.happy_shopping.web.service;

import com.zhangheng.happy_shopping.android.entity.submitgoods.SubmitGoods;
import com.zhangheng.happy_shopping.android.entity.submitgoods.goods;
import com.zhangheng.happy_shopping.web.controller_mer.data.main.goodsnumBytimeAndtype;
import com.zhangheng.util.MathUtil;
import com.zhangheng.util.TimeUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张恒
 * @program: happy_shopping
 * @email zhangheng.0805@qq.com
 * @date 2022-03-14 11:29
 */
@Service
public class MerMainService {

    public List<goodsnumBytimeAndtype> handle1(List<goods> list, int i){
        List<goodsnumBytimeAndtype> res=new ArrayList<>();
        List<goods> gList=new ArrayList<>();
        String dateFormat="yyyy-MM-dd";
        String flag_time="";
        String[] time_arr=new String[i];
        int flag_count=0;
        for (goods sg : list) {
            String t = TimeUtil.toTime(TimeUtil.toDate(sg.getList_id().split("_")[0], "yyyyMMddHHmmss"), dateFormat);
            if (flag_count<i) {
                if (flag_time.equals(t)) {
                    gList.add(sg);
                } else {
                    time_arr[flag_count]=t;
                    flag_count++;
                    flag_time=t;
                    gList.add(sg);
                }
            }else {
                break;
            }
        }
        for (int n=0;n<i;n++) {
            String s = time_arr[(i-1)-n];
            if (s!=null) {
                goodsnumBytimeAndtype bytimeAndtype = new goodsnumBytimeAndtype();
                bytimeAndtype.setTime(s);
                for (goods g : gList) {
                    String t = TimeUtil.toTime(TimeUtil.toDate(g.getList_id().split("_")[0], "yyyyMMddHHmmss"), dateFormat);
                    if (bytimeAndtype.getTime().equals(t)) {
                        switch (g.getState()) {
                            case 0://待处理
                                bytimeAndtype.setSta0(bytimeAndtype.getSta0() + 1);
                                break;
                            case 1://拒绝发货
                                bytimeAndtype.setSta1(bytimeAndtype.getSta1() + 1);
                                break;
                            case 2://待收货
                                bytimeAndtype.setSta2(bytimeAndtype.getSta2() + 1);
                                break;
                            case 3://已收货
                                bytimeAndtype.setSta3(bytimeAndtype.getSta3() + 1);
                                break;
                            case 4://退货
                                bytimeAndtype.setSta4(bytimeAndtype.getSta4() + 1);
                                break;
                        }
                        double c = g.getGoods_price() * g.getNum();
                        bytimeAndtype.setCont_price(MathUtil.twoDecimalPlaces(bytimeAndtype.getCont_price() + c));
                    }
                }
                res.add(bytimeAndtype);
            }
        }
        return res;
    }
}
