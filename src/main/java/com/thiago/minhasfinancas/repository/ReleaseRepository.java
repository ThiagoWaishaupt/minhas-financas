package com.thiago.minhasfinancas.repository;

import com.thiago.minhasfinancas.model.Release;
import com.thiago.minhasfinancas.model.enums.ReleaseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ReleaseRepository extends JpaRepository<Release, Long> {

    @Query(value =
            "SELECT sum(r.value) " +
            "FROM Release r " +
            "JOIN r.user u " +
            "WHERE u.id = :userId " +
            "AND r.releaseType = :type " +
            "GROUP BY u"
    )
    BigDecimal getUserBalance(@Param("userId") Long userId, @Param("type") ReleaseType type);

}
