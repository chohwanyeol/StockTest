package com.stocktest.stocktest.service;

import com.stocktest.stocktest.domain.Stock;
import com.stocktest.stocktest.domain.StockLock;
import com.stocktest.stocktest.repository.StockLockRepository;
import com.stocktest.stocktest.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicStockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long id, int quantity) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("재고 없음"));

        stock.setQuantity(stock.getQuantity() - quantity);
    }
}
