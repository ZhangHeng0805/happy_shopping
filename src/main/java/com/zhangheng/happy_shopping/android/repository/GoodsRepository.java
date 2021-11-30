package com.zhangheng.happy_shopping.android.repository;

import com.zhangheng.happy_shopping.android.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface GoodsRepository extends JpaRepository<Goods, Integer> {

    //根据商品名和介绍查询商品
    @Query(value = "select * from Goods where goods_name = ?1 and goods_introduction = ?2",nativeQuery = true)
    Optional<Goods> findByGoods_nameAndGoods_introduction(String goods_name, String goods_introduction);
    //根据店铺id和商品类型查询商品数
    @Query(value = "select count(*) from Goods where store_id = ?1 and goods_type = ?2",nativeQuery = true)
    Integer countByStore_idAndGoods_type(Integer store_id, String goods_type);

    //修改商品销量
    @Modifying
    @Query("update Goods sc set sc.goods_sales = sc.goods_sales + ?1 where sc.goods_id = ?2")
    public void addGoods_Sales(Integer num, Integer goods_id);

    //修改商品库存
    @Modifying
    @Query("update Goods sc set sc.goods_num = sc.goods_num + ?1 where sc.goods_id = ?2")
    public void updateGoods_num(Integer goods_num, Integer goods_id);

    //根据店铺id查询商品
    @Query(value = "select * from Goods where store_id = ?1",nativeQuery = true)
    List<Goods> findByStore_id(Integer store_id);
    //根据店铺id和商品状态查询商品
    @Query(value = "select * from Goods where store_id = ?1 and goods_type = ?2",nativeQuery = true)
    List<Goods> findByStore_idAndGoods_type(Integer store_id,String type);

    //根据商品类型查询商品
    @Query("select u from Goods u where u.goods_type = ?1")
    List<Goods> findByGoods_type(String type);
    //根据商品类型和状态查询商品
    @Query("select u from Goods u where u.goods_type = ?1 and u.state= ?2")
    List<Goods> findByGoods_typeAndState(String type,int state);
    //根据商品状态查询商品
    @Query("select u from Goods u where u.state = ?1")
    List<Goods> findByState(int state);

    @Modifying
    @Query("delete from Goods sc where sc.store_id=?1 and sc.goods_id = ?2")
    public void deleteByStore_idAndGoods_id(Integer store_id, Integer goods_id);

}
