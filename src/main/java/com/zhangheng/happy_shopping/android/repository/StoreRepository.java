package com.zhangheng.happy_shopping.android.repository;

import com.zhangheng.happy_shopping.android.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    /**
     * 根据店铺id和店铺名和老板名查询
     * @param id
     * @param store_name
     * @param name
     * @return
     */
    @Query(value = "select * from stores where store_id=?1 and store_name=?2 and boss_name=?3",nativeQuery = true)
    Optional<Store> findByStore_idAndStore_nameAndBoss_name(Integer id, String store_name, String name);

    /**
     * 根据店铺id修改店铺营业额
     * @param turnover
     * @param store_id
     * @return
     */
    @Modifying
    @Query("update Store s set s.turnover=?1 where s.store_id=?2")
    Integer updateTurnoverByStore_id(double turnover,int store_id);

}
