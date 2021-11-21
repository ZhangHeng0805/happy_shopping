package com.zhangheng.happy_shopping.android.repository;


import com.zhangheng.happy_shopping.android.entity.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantsRepository extends JpaRepository<Merchants, String> {
}
