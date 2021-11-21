package com.zhangheng.happy_shopping.repository;

import com.zhangheng.happy_shopping.bean.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CodeRepository extends JpaRepository<VerificationCode,String> {
}
