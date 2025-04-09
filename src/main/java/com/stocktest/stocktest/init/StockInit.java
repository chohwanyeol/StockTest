package com.stocktest.stocktest.init;

import com.stocktest.stocktest.repository.StockLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockInit {

    private final StockLockRepository stockLockRepository;

//    @PostConstruct
//    public void initStock() {
//        stockRepository.deleteAll();
//
//        Stock stock = Stock.builder()
//                .quantity(100)
//                .build();
//
//        stockRepository.save(stock);
//
//        System.out.println("✅ 초기 재고 100개 삽입 완료");
//    }
}
