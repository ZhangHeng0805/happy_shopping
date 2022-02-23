package com.zhangheng.happy_shopping.android.repository;


import com.zhangheng.happy_shopping.android.entity.PhoneInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface PhoneInfoRepository  extends JpaRepository<PhoneInfo, String> {
}
