package com.zhangheng.happy_shopping.android.repository;


import com.zhangheng.happy_shopping.android.entity.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MerchantsRepository extends JpaRepository<Merchants, String> {
    /**
     * 根据手机号修改密码
     * @param password
     * @param phonenum
     * @return
     */
    @Modifying
    @Query("update Merchants m set m.password = ?1 where m.phonenum = ?2")
    int updatePasswordByPhonenum(String password,String phonenum);

    /**
     * 根据手机号修改账号状态
     * @param state
     * @param phonenum
     * @return
     */
    @Modifying
    @Query("update Merchants m set m.state = ?1 where m.phonenum = ?2")
    int updateStateByPhonenum(int state,String phonenum);

}
