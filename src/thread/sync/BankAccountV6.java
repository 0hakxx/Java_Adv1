package thread.sync;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV6 implements BankAccount {

    private int balance;

    private final Lock lock = new ReentrantLock();

    public BankAccountV6(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public boolean withdraw(int amount) {
        log("거래 시작: " + getClass().getSimpleName());

        // [V5 vs V6 핵심 차이점]
        // V5: lock.tryLock() → 락을 즉시 획득 시도, 실패 시 바로 false 반환 (Non-blocking)
        // V6: lock.tryLock(timeout, TimeUnit) → 지정한 시간(여기선 500ms) 동안 락을 기다렸다가,
        //      그 시간 내에 락을 못 얻으면 false 반환 (Timed blocking)
        //      즉, 락이 잠깐만 점유 중이면 기다렸다가 획득 가능, 오래 점유 중이면 실패 처리
        try {
            if (!lock.tryLock(500, TimeUnit.MILLISECONDS)) { // 500ms 대기 후 실패 처리
                log("[진입 실패] 이미 처리중인 작업이 있습니다.");
                return false;
            }
        } catch (InterruptedException e) {
            // 락 대기 중 인터럽트가 발생하면 런타임 예외로 래핑해서 던짐
            throw new RuntimeException(e);
        }

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
            lock.unlock(); // ReentrantLock 이용하여 lock 해제
        }
        log("거래 종료");
        return true;
    }

    @Override
    public int getBalance() {
        lock.lock(); // ReentrantLock 이용하여 lock을 걸기
        try {
            return balance;
        } finally {
            lock.unlock(); // ReentrantLock 이용하여 lock 해제
        }
    }
}
