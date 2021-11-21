package com.zhangheng.happy_shopping.android.repository;

import com.zhangheng.happy_shopping.android.entity.ChatConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*聊天配置数据库的操作接口*/
@Transactional
@Repository
public interface ChatConfigRep extends JpaRepository<ChatConfig,Integer> {
    //根据本地端口查询
    @Query(value = "select * from chatconfig where local_port = ?1" ,nativeQuery = true)
    Optional<ChatConfig> findChatConfigByLocalPort(String local_port);
}
