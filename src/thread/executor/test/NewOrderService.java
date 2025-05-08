package thread.executor.test;

import java.util.List;
import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class NewOrderService {
    // [NEW] 1. ExecutorService(스레드풀) 사용 선언
    // - 여러 작업을 병렬로 처리할 수 있도록 스레드풀을 생성
    private final ExecutorService es = Executors.newFixedThreadPool(10);

    public void order(String orderNo) throws ExecutionException, InterruptedException {
        // [NEW] 2. 각 작업을 Callable로 생성 (병렬 실행 가능)
        Callable<Boolean> inventoryWork = new InventoryWork(orderNo);
        Callable<Boolean> shippingWork = new ShippingWork(orderNo);
        Callable<Boolean> accountingWork = new AccountingWork(orderNo);

        // [NEW] 3. 작업들을 리스트로 묶음
        // - invokeAll()로 한 번에 여러 작업을 제출하기 위함
        List<Callable<Boolean>> tasks = List.of(inventoryWork, shippingWork, accountingWork);

        // [NEW] 4. invokeAll()로 모든 작업을 한 번에 제출 (병렬 실행)
        // - 세 작업이 동시에 실행되며, 모두 끝날 때까지 대기
        List<Future<Boolean>> futures = es.invokeAll(tasks);

        // [NEW] 5. 결과 확인 (Future에서 결과 꺼내기)
        // - 각 작업의 실행 결과를 Future에서 가져옴
        boolean allSuccess = true;
        for (Future<Boolean> future : futures) {
            if (!future.get()) { // [NEW] 각 작업이 실패하면 allSuccess = false
                allSuccess = false;
            }
        }
        // [NEW] 6. 모든 작업이 성공/실패 여부에 따라 로그 출력
        if (allSuccess) {
            log("모든 주문 처리가 성공적으로 완료되었습니다.");
        } else {
            log("일부 작업이 실패했습니다.");
        }
    }

    // [NEW] 7. 서비스 종료 메서드 (스레드풀 자원 해제)
    public void shutdown() {
        es.close();
    }

    // [NEW] 8. 각각의 작업을 Callable로 구현 (스레드풀에서 실행 가능)
    static class InventoryWork implements Callable<Boolean> {
        private final String orderNo;
        public InventoryWork(String orderNo) { this.orderNo = orderNo; }
        @Override
        public Boolean call() {
            log("재고 업데이트: " + orderNo);
            sleep(1000);
            return true;
        }
    }

    static class ShippingWork implements Callable<Boolean> {
        private final String orderNo;
        public ShippingWork(String orderNo) { this.orderNo = orderNo; }
        @Override
        public Boolean call() {
            log("배송 시스템 알림: " + orderNo);
            sleep(1000);
            return true;
        }
    }

    static class AccountingWork implements Callable<Boolean> {
        private final String orderNo;
        public AccountingWork(String orderNo) { this.orderNo = orderNo; }
        @Override
        public Boolean call() {
            log("회계 시스템 업데이트: " + orderNo);
            sleep(1000);
            return true;
        }
    }
}
