
---

# 🔒 락 전략 비교 실험 프로젝트

Spring Boot 기반으로 동시성 환경에서 데이터 정합성을 어떻게 보장할 수 있을지 실험해본 프로젝트입니다.  
JPA에서 제공하는 락 전략(기본 / 낙관적 / 비관적)을 직접 적용해보고, 요청 충돌 상황에서 어떤 결과가 나오는지 수치로 비교했습니다.

---

## ✅ 프로젝트 정보

- **프로젝트명**: Stock Lock Test
- **기술스택**: Java 21, Spring Boot 3.4.4, Spring Web, Lombok, Spring Data JPA, Oracle DB, Swagger
- **실험 목적**:
  - 동시성 환경에서 자주 발생하는 정합성 문제를 직접 체감해보기
  - JPA의 다양한 락 전략이 실제 충돌 상황에서 어떻게 동작하는지 실험
  - 실패율, 처리율, 안정성 등을 기준으로 전략별 효과 비교

---

## 📦 주요 의존성

```groovy
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-devtools'
implementation 'org.projectlombok:lombok'
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0'
runtimeOnly 'com.oracle.database.jdbc:ojdbc11'
```

## 🧪 실험 API

| Method | Endpoint                  | 설명 |
|--------|----------------------------|------|
| POST   | `/stock/decrease/basic`     | 락 없이 재고 감소 처리 |
| POST   | `/stock/decrease/optimistic`| 낙관적 락 (@Version) 적용 |
| POST   | `/stock/decrease/pessimistic`| 비관적 락 (PESSIMISTIC_WRITE) 적용 |

---

## 📊 실험 결과 비교

### 100개의 요청 동시 발생

| 전략             | 결과                             |
|------------------|----------------------------------|
| 기본             | 100개 중 약 20~30개만 감소 (충돌 다수) |
| 낙관적 락         | 100개 중 약 20~30개만 감소 (충돌 다수) |
| 낙관적 락 + 재시도 | 100개 중 99개 감소 (거의 성공)       |
| 비관적 락         | 100개 중 100개 감소 (완벽 성공)      |

---

## 🧠 느낀 점: "락 전략은 상황 따라 유연하게 써야 한다."

- 이번 실험을 하면서 처음엔 “그냥 락 걸면 다 되는 거 아냐?” 했는데, 막상 해보니까 생각보다 훨씬 복잡했음  
- 락 없이 처리하면 당연히 꼬이긴 하는데, 낙관적 락은 예상보다 많이 실패했고, 재시도 로직을 넣으니까 거의 다 처리되긴 했음  
- 비관적 락은 진짜 안정적이긴 했는데, 락 대기 시간이 생긴다고 하니 병렬 요청이 쌓이면 **과연 괜찮을까?** 싶었음  
- 정리하자면, 트래픽이 적고 재시도가 부담되지 않는 상황이면 **낙관적 + 재시도**가 효율적이고, 유실되면 안 되는 핵심 데이터 처리에는 **비관적 락이 맞을 것 같음**  
- 솔직히 성능 차이를 뚜렷하게 체감하진 못했지만, 비관적 락을 사용하여 대기가 누적되면 문제될 수도 있겠다는 생각이 들었음
