package com.stocktest.stocktest.repository;

import com.stocktest.stocktest.domain.StockLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface StockLockRepository extends JpaRepository<StockLock, Long> {

    // 비관적 락용 메서드
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from StockLock s where s.id = :id")
    Optional<StockLock> findByIdWithPessimisticLock(Long id);
}
