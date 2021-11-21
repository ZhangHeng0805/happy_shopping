package com.zhangheng.happy_shopping.android.repository;


import com.zhangheng.happy_shopping.android.entity.submitgoods.goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface ListGoodsRepository extends JpaRepository<goods, Integer> {

    //根据店铺id查询订单
    @Query("select u from goods u where u.store_id = ?1")
    List<goods> findByStore_id(Integer store_id);
    //根据店铺id和订单状态查询订单
    @Query("select u from goods u where u.store_id = ?1 and u.state=?2")
    List<goods> findByStore_idAndState(Integer store_id, String state);
    //根据店铺id和商品类型查询商品数
    @Query(value = "select count(*) from goods_list where store_id = ?1 and state = ?2",nativeQuery = true)
    Integer countByStore_idAndState(Integer store_id, Integer state);

    @Modifying
    @Query("update goods sc set sc.state =  ?1 where sc.list_id = ?2 and sc.goods_id=?3")
    public void updateStateByList_idAndGoods_id(int state, String list_id, Integer goods_id);
}
