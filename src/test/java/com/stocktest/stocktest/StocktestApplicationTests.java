package com.stocktest.stocktest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class StocktestApplicationTests {

	private final String baseUrl = "http://localhost:8080/stock";
	private final RestTemplate restTemplate = new RestTemplate();


	@Test
	void 동시에_100개_기본요청() throws InterruptedException {
		restTemplate.postForEntity(baseUrl + "/init", null, String.class);
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.execute(() -> {
				try {
					// ✅ 여기를 바꿔가며 테스트: basic / optimistic / pessimistic
					String url = baseUrl + "/decrease/basic";
					restTemplate.postForEntity(url, null, String.class);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		// 결과 확인
		Integer quantity = restTemplate.getForObject(baseUrl+"/get", Integer.class);
		System.out.println("✅ 최종 재고 수량: " + quantity);
	}

	@Test
	void 동시에_100개_낙관적락_요청() throws InterruptedException {
		restTemplate.postForEntity(baseUrl + "/initLock", null, String.class);
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.execute(() -> {
				try {
					String url = baseUrl + "/decrease/optimistic";
					restTemplate.postForEntity(url, null, String.class);
				} finally {
					latch.countDown();
				}
			});
		}



		latch.await();
		Integer quantity = restTemplate.getForObject(baseUrl+"/getLock", Integer.class);
		System.out.println("✅ [낙관적 락] 최종 재고 수량: " + quantity);
	}

	@Test
	void 동시에_100개_비관적락_요청() throws InterruptedException {
		restTemplate.postForEntity(baseUrl + "/initLock", null, String.class);
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.execute(() -> {
				try {
					String url = baseUrl + "/decrease/pessimistic";
					restTemplate.postForEntity(url, null, String.class);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		Integer quantity = restTemplate.getForObject(baseUrl+"/getLock", Integer.class);
		System.out.println("✅ [비관적 락] 최종 재고 수량: " + quantity);
	}


}
