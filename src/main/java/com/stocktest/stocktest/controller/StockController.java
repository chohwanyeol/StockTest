package com.stocktest.stocktest.controller;

import com.stocktest.stocktest.domain.Stock;
import com.stocktest.stocktest.domain.StockLock;
import com.stocktest.stocktest.repository.StockLockRepository;
import com.stocktest.stocktest.repository.StockRepository;
import com.stocktest.stocktest.service.BasicStockService;
import com.stocktest.stocktest.service.OptimisticLockStockService;
import com.stocktest.stocktest.service.PessimisticLockStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockController {

    private final StockRepository stockRepository;
    private final StockLockRepository stockLockRepository;
    private final BasicStockService basicStockService;
    private final OptimisticLockStockService optimisticLockStockService;
    private final PessimisticLockStockService pessimisticLockStockService;



    /**
     * 현재 재고 조회
     */
    @GetMapping("/getLock")
    public int getStockLock() {
        return stockLockRepository.findAll().stream()
                .findFirst()
                .map(StockLock::getQuantity)
                .orElse(0);
    }


    @GetMapping("/get")
    public int getStock() {
        return stockRepository.findAll().stream()
                .findFirst()
                .map(Stock::getQuantity)
                .orElse(0);
    }


    /**
     * 재고 초기화 (100개)
     */
    @PostMapping("/init")

    public String initStock() {
        stockRepository.deleteAll();
        Stock stock = Stock.builder()
                .quantity(100)
                .build();
        stockRepository.save(stock);
        return "재고 초기화 완료 (100개)";
    }

    @PostMapping("/initLock")
    public String initStockLock() {
        stockLockRepository.deleteAll();
        StockLock stockLock = StockLock.builder()
                .quantity(100)
                .build();
        stockLockRepository.save(stockLock);
        return "재고 초기화 완료 (100개)";
    }



    /**
     * 기본 방식으로 재고 차감
     */
    @PostMapping("/decrease/basic")
    public String decreaseBasic() {
        basicStockService.decrease(1L, 1); // id=1, 1개 차감
        return "기본 차감 완료";
    }

    /**
     * 낙관적 락 방식으로 재고 차감
     */
    @PostMapping("/decrease/optimistic")
    public String decreaseOptimistic() {
        optimisticLockStockService.decrease(1L, 1);
        return "낙관적 락 차감 완료";
    }

    /**
     * 비관적 락 방식으로 재고 차감
     */
    @PostMapping("/decrease/pessimistic")
    public String decreasePessimistic() {
        pessimisticLockStockService.decrease(1L, 1);
        return "비관적 락 차감 완료";
    }
}
