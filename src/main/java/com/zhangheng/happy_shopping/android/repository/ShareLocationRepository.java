package com.zhangheng.happy_shopping.android.repository;


import com.zhangheng.happy_shopping.android.entity.ShareLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShareLocationRepository extends JpaRepository<ShareLocation, String> {

}
