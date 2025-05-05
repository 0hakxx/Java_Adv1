package thread.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV4 implements BankAccount {

    private int balance;

    // [V3 vs V4 핵심 차이점]
    // - V3: 동기화(synchronized)를 사용 (내부적으로 JVM 모니터 락)
    // - V4: 명시적 Lock 객체(ReentrantLock)를 사용해 동기화 처리
    // 또한 finally를 사용하여 unlock()을 반드시 호출하는 것을 권고.
    private final Lock lock = new ReentrantLock();

    public BankAccountV4(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public boolean withdraw(int amount) {
        log("거래 시작: " + getClass().getSimpleName());

        lock.lock(); // [V4의 핵심] Lock 을 걸기 (synchronized 대신 사용)
        try {
            log("[검증 시작] 출금액: " + amount + ", 잔액: " + balance);
            if (balance < amount) {
                log("[검증 실패] 출금액: " + amount + ", 잔액: " + balance);
                return false;
            }

            // 잔고가 출금액 보다 많으면, 진행
            log("[검증 완료] 출금액: " + amount + ", 잔액: " + balance);
            sleep(1000); // 출금에 걸리는 시간으로 가정
            balance = balance - amount;
            log("[출금 완료] 출금액: " + amount + ", 잔액: " + balance);
        } finally {
            lock.unlock(); // [V4의 핵심] 명시적으로 Lock을 해제 (synchronized 블록의 자동 해제와 다름)
        }
        log("거래 종료");
        return true;
    }

    @Override
    public int getBalance() {
        lock.lock(); // 읽기 작업도 Lock으로 보호
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}
