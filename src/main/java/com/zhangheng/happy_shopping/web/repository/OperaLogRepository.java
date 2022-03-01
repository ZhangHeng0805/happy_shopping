package com.zhangheng.happy_shopping.web.repository;

import com.zhangheng.happy_shopping.web.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface OperaLogRepository extends JpaRepository<OperationLog,Integer> {
    /**
     * 根据request和操作类型查询
     * @param request
     * @param type
     * @return
     */
    @Query(value = "select * from operation_log where request = ?1 and type = ?2",nativeQuery = true)
    Optional<OperationLog> findByRequestAndType(String request, Integer type);

    /**
     * 根据操作类型查询
     * @param type
     * @return
     */
    @Query(value = "select * from operation_log where type = ?1",nativeQuery = true)
    List<OperationLog> findAllByType(int type);

    /**
     * 更具时间降序查询
     * @return
     */
    @Query(value = "select * from operation_log where type =?1 order by time desc",nativeQuery = true)
    List<OperationLog> findAllByTypeOrderByTime(int type);
}
