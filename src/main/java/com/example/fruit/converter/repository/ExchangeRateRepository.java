package com.example.fruit.converter.repository;

import com.example.fruit.converter.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    @Query("""
    SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
    FROM Currency c
    WHERE c.active = true
      AND c.code <> 'RUB'
      AND NOT EXISTS (
          SELECT 1
          FROM ExchangeRate er
          WHERE er.base.code = 'RUB'
            AND er.target = c
            AND er.expiresAt > :now
      )
    """)
    boolean existsExpiredOrMissing(@Param("now") Instant now);

    Optional<ExchangeRate> findFirstByBase_IdAndTarget_IdAndExpiresAtAfterOrderByFetchedAtDesc(Long baseId, Long targetId, Instant now);
}
