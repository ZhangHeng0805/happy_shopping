package com.zhangheng.happy_shopping.android.repository;


import com.zhangheng.happy_shopping.android.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, String> {
    /**
     * 根据手机号修改账号状态
     * @param state
     * @param phone
     * @return
     */
    @Modifying
    @Query("update Customer c set c.state = ?1 where c.phone = ?2")
    int updateStateByPhone(int state,String phone);

    /**
     * 根据手机号修改账号密码
     * @param password
     * @param phone
     * @return
     */
    @Modifying
    @Query("update Customer c set c.password = ?1 where c.phone = ?2")
    int updatePasswordByPhone(String password,String phone);
}
