package com.zhangheng.happy_shopping.web.repository;

import com.zhangheng.happy_shopping.web.entity.OperationLog;
import com.zhangheng.happy_shopping.web.entity.goods_type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface GoodsTypeRepository extends JpaRepository<goods_type,Integer> {
    //根据商品类型查询
    @Query(value = "select * from goods_type where type = ?1",nativeQuery = true)
    Optional<goods_type>  findByType(String type);

}
