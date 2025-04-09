package com.stocktest.stocktest.service;

import com.stocktest.stocktest.domain.StockLock;
import com.stocktest.stocktest.repository.StockLockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PessimisticLockStockService {

    private final StockLockRepository stockLockRepository;

    @Transactional
    public void decrease(Long id, int quantity) {
        StockLock stockLock = stockLockRepository.findByIdWithPessimisticLock(id)
                .orElseThrow(() -> new RuntimeException("재고 없음"));

        stockLock.setQuantity(stockLock.getQuantity() - quantity);
    }
}
