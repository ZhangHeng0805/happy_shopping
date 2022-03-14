package com.zhangheng.happy_shopping.android.repository;


import com.zhangheng.happy_shopping.android.entity.submitgoods.goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ListGoodsRepository extends JpaRepository<goods, Integer> {


    /**
     * 根据店铺id查询订单
     * @param store_id
     * @return
     */
    @Query("select u from goods u where u.store_id = ?1 order by u.list_id desc")
    List<goods> findByStore_id(Integer store_id);

    /**
     * 根据店铺id和订单状态查询订单
     * @param store_id
     * @param state
     * @return
     */
    @Query("select u from goods u where u.store_id = ?1 and u.state=?2")
    List<goods> findByStore_idAndState(Integer store_id, Integer state);

    /**
     * 根据店铺id和商品类型统计商品数
     * @param store_id
     * @param state
     * @return
     */
    @Query(value = "select count(*) from goods_list where store_id = ?1 and state = ?2",nativeQuery = true)
    Integer countByStore_idAndState(Integer store_id, Integer state);

    /**
     * 根据订单号和商品id修改订单状态
     * @param state
     * @param list_id
     * @param goods_id
     * @return
     */
    @Modifying
    @Query("update goods sc set sc.state =  ?1 where sc.list_id = ?2 and sc.goods_id=?3")
    Integer updateStateByList_idAndGoods_id(int state, String list_id, Integer goods_id);

    /**
     * 根据订单号和商品id删除订单商品
     * @param list_id
     * @param goods_id
     * @return
     */
    @Modifying
    @Query("delete from goods sc  where sc.list_id = ?1 and sc.goods_id=?2")
    Integer deleteByList_idAndGoods_id(String list_id, Integer goods_id);

    /**
     * 根据低昂单号和商品id查询订单商品
     * @param list_id
     * @param goods_id
     * @return
     */
    @Query("select gs from goods gs where gs.list_id=?1 and gs.goods_id=?2 ")
    Optional<goods> findByList_idAndGoods_id(String list_id,int goods_id);
    /**
     * 根据订单商品id修改订单商品的状态
     * @param state
     * @param id
     * @return
     */
    @Modifying
    @Query("update goods sc set sc.state =  ?1 where sc.id = ?2")
    int updateStateById(int state, Integer id);

    /**
     * 根据订单商品的状态统计数量
     * @param state
     * @return
     */
    @Query(value = "select count(*) from goods_list where state=?1",nativeQuery = true)
    int countByState(int state);
}
