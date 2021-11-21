package com.zhangheng.happy_shopping.android.repository;


import com.zhangheng.happy_shopping.android.entity.submitgoods.SubmitGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface SubmitGoodsRepository extends JpaRepository<SubmitGoods, String> {
    //根据订单号查询订单
    @Query(value = "select * from submit_goodslist  where submit_id = ?1",nativeQuery = true)
    Optional<SubmitGoods> findBySubmit_id(String submit_id);
    //根据手机号查询订单
    @Query(value = "select * from submit_goodslist where phone = ?1",nativeQuery = true)
    List<SubmitGoods> findAllByPhone(String phone);
}
