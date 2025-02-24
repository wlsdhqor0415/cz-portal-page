package com.example.czportalpage.info.repository;

import com.example.czportalpage.info.entity.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InfoRepository extends JpaRepository<Info, Long> {

    // username으로 사용자를 조회하는 메서드
    Optional<Info> findByUsername(String username);
}
