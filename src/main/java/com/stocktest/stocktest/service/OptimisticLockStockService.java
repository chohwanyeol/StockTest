package com.stocktest.stocktest.service;

import com.stocktest.stocktest.domain.StockLock;
import com.stocktest.stocktest.repository.StockLockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticLockStockService {

    private final StockLockRepository stockLockRepository;


    @Retryable(
            value = { OptimisticLockingFailureException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    @Transactional
    public void decrease(Long id, int quantity) {
        StockLock stockLock = stockLockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("재고 없음"));

        stockLock.setQuantity(stockLock.getQuantity() - quantity);
    }
}
