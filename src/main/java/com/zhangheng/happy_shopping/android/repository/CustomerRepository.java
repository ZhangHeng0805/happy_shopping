package com.zhangheng.happy_shopping.android.repository;


import com.zhangheng.happy_shopping.android.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//@Service
//@EnableJpaRepositories
@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, String> {


}
