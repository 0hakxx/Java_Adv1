package thread.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

/*
V4:
락을 얻을 때까지 무조건 대기 → 락을 얻으면 반드시 임계 영역(출금 코드 부분) 실행
V5/V6:
락을 즉시(또는 잠깐) 시도해서 못 얻으면 바로 실패 → 임계 영역(출금 코드 부분)을 아예 실행하지 않음
*/

public class BankAccountV5 implements BankAccount {

    private int balance;

    // ReentrantLock 인스턴스 생성 (V4와 동일)
    private final Lock lock = new ReentrantLock();

    public BankAccountV5(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public boolean withdraw(int amount) {
        log("거래 시작: " + getClass().getSimpleName());

        // [V4 vs V5 핵심 차이점]
        // V4: lock.lock()을 사용 → 락이 풀릴 때까지 무한 대기(블로킹)
        // V5: lock.tryLock()을 사용 → 락 획득을 즉시 시도, 실패 시 바로 false 반환 (Non-blocking)
        //     즉, 이미 다른 스레드가 출금 처리를 하고 있으면 대기하지 않고 바로 실패 처리
        if (!lock.tryLock()) {
            log("[진입 실패] 이미 처리중인 작업이 있습니다."); // 락 획득 실패 시 메시지 출력
            return false;
        }

        try {
            log("[검증 시작] 출금액: " + amount + ", 잔액: " + balance);
            if (balance < amount) {
                // 잔액 부족: 출금 실패
                log("[검증 실패] 출금액: " + amount + ", 잔액: " + balance);
                return false;
            }

            // 잔고가 출금액 보다 많으면, 진행
            log("[검증 완료] 출금액: " + amount + ", 잔액: " + balance);
            sleep(1000); // 출금에 걸리는 시간으로 가정 (동기화 영역 내)
            balance = balance - amount;
            log("[출금 완료] 출금액: " + amount + ", 잔액: " + balance);
        } finally {
            // tryLock()으로 락을 획득했다면 반드시 unlock()으로 해제해야 함
            lock.unlock();
        }
        log("거래 종료");
        return true;
    }

    @Override
    public int getBalance() {
        // V4와 동일하게 getBalance도 락으로 보호
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}
