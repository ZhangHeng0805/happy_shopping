package com.zhangheng.happy_shopping.android.repository;

import com.zhangheng.happy_shopping.android.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    //根据店铺id和店铺名和老板名查询
    @Query(value = "select * from Store  where store_id=?1 and store_name=?2 and boss_name=?3",nativeQuery = true)
    Optional<Store> findByStore_idAndStore_nameAndBoss_name(Integer id, String store_name, String name);
}
