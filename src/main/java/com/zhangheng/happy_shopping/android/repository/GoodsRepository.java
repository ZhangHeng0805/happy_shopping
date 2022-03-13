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


    /**
     * 根据商品名和介绍查询商品
     * @param goods_name
     * @param goods_introduction
     * @return
     */
    @Query(value = "select * from goods where goods_name = ?1 and goods_introduction = ?2",nativeQuery = true)
    Optional<Goods> findByGoods_nameAndGoods_introduction(String goods_name, String goods_introduction);

    /**
     * 根据店铺id和商品类型查询商品数
     * @param store_id
     * @param goods_type
     * @return
     */
    @Query(value = "select count(*) from goods where store_id = ?1 and goods_type = ?2",nativeQuery = true)
    Integer countByStore_idAndGoods_type(Integer store_id, String goods_type);


    /**
     * 修改商品销量
     * @param num
     * @param goods_id
     */
    @Modifying
    @Query("update Goods sc set sc.goods_sales = sc.goods_sales + ?1 where sc.goods_id = ?2")
    public void addGoods_Sales(Integer num, Integer goods_id);

    /**
     * 根据商品id修改商品状态
     * @param state 修改的状态
     * @param goods_id 商品id
     */
    @Modifying
    @Query("update Goods sc set sc.state = ?1 where sc.goods_id = ?2")
    public int updateGoods_StateByGoods_id(Integer state, Integer goods_id);

    /**
     * 修改商品库存
     * @param goods_num
     * @param goods_id
     */
    @Modifying
    @Query("update Goods sc set sc.goods_num = sc.goods_num + ?1 where sc.goods_id = ?2")
    public void updateGoods_num(Integer goods_num, Integer goods_id);

    /**
     * 根据店铺id查询商品
     * @param store_id
     * @return
     */
    @Query(value = "select * from goods where store_id = ?1",nativeQuery = true)
    List<Goods> findByStore_id(Integer store_id);


    /**
     * 根据店铺id和商品状态查询商品
     * @param store_id
     * @param type
     * @return
     */
    @Query(value = "select * from goods where store_id = ?1 and goods_type = ?2",nativeQuery = true)
    List<Goods> findByStore_idAndGoods_type(Integer store_id,String type);

    /**
     * 根据商品类型统计数量
     * @param type
     * @return
     */
    @Query(value = "select count(*) from goods where goods_type = ?1",nativeQuery = true)
    int countByGoods_type(String type);
    /**
     * 根据商品类型查询商品
     * @param type
     * @return
     */
    @Query("select u from Goods u where u.goods_type = ?1")
    List<Goods> findByGoods_type(String type);

    /**
     * 根据商品类型和状态查询商品
     * @param type
     * @param state
     * @return
     */
    @Query("select u from Goods u where u.goods_type = ?1 and u.state= ?2")
    List<Goods> findByGoods_typeAndState(String type,int state);

    /**
     * 根据商品状态、名称模糊查询
     * @param state
     * @param name
     * @return
     */
    @Query("select g from Goods g where g.state=?1 and g.goods_name like %?2%")
    List<Goods> findByStateAndGoods_nameLike(int state,String name);
    /**
     * 根据商品类型、状态、名称模糊查询
     * @param type
     * @param state
     * @param name
     * @return
     */
    @Query("select g from Goods g where g.goods_type=?1 and g.state=?2 and g.goods_name like %?3%")
    List<Goods> findByGoods_typeAndStateAndGoods_nameLike(String type,int state,String name);
    /**
     * 根据商品状态、店铺名模糊查询
     * @param state
     * @param name
     * @return
     */
    @Query("select g from Goods g where g.state=?1 and g.store_name like %?2%")
    List<Goods> findByStateAndStore_nameLike(int state,String name);
    /**
     * 根据商品类型、状态、店铺名称模糊查询
     * @param type
     * @param state
     * @param name
     * @return
     */
    @Query("select g from Goods g where g.goods_type=?1 and g.state=?2 and g.store_name like %?3%")
    List<Goods> findByGoods_typeAndStateAndStore_nameLike(String type,int state,String name);
    /**
     * 根据商品状态查询
     * @param state 商品状态
     * @return
     */
    @Query("select u from Goods u where u.state = ?1")
    List<Goods> findByState(int state);

    /**
     * 根据商品id删除商品
     * @param goods_id 商品id
     */
    @Modifying
    @Query("delete from Goods sc where sc.goods_id = ?1")
    public int deleteByGoods_id(Integer goods_id);

}
