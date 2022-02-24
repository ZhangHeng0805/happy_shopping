package com.zhangheng.happy_shopping.web.repository;

import com.zhangheng.happy_shopping.web.entity.OperationLog;
import com.zhangheng.happy_shopping.web.entity.goods_type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface GoodsTypeRepository extends JpaRepository<goods_type,Integer> {
    /**
     * 根据商品类型查询
     * @param type 商品类型
     * @return
     */
    @Query(value = "select * from goods_type where type = ?1",nativeQuery = true)
    Optional<goods_type> findByType(String type);

    /**
     * 根据id修改类型
     * @param type
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update goods_type gt set gt.type = ?1 where gt.id = ?2")
    int updateTypeById(String type,Integer id);

    /**
     * 根据id删除类型
     * @param id
     * @return
     */
    @Modifying
    @Query("delete from goods_type gt where gt.id = ?1")
    int delById(Integer id);

}
