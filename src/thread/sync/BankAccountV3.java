package thread.sync;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV3 implements BankAccount {

    private int balance;

    public BankAccountV3(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public boolean withdraw(int amount) {
        log("거래 시작: " + getClass().getSimpleName());

        // [V2 vs V3 핵심 차이점] ----------------------------------------------
        // - V2: 메서드 전체를 synchronized로 동기화
        // - V3: balance 수정 관련 코드만 동기화 -> 성능 개선
        synchronized (this) {
            log("[검증 시작] 출금액: " + amount + ", 잔액: " + balance);
            if (balance < amount) {
                log("[검증 실패] 출금액: " + amount + ", 잔액: " + balance);
                return false;
            }

            log("[검증 완료] 출금액: " + amount + ", 잔액: " + balance);
            balance = balance - amount; // 차감 연산만 동기화 영역 내 처리
        }
        // ---------------------------------------------------------------------

        sleep(1000); // [주의] 동기화 영역 외부 → 락이 해제된 상태에서 지연 발생
        log("[출금 완료] 출금액: " + amount + ", 잔액: " + balance);

        log("거래 종료");
        return true;
    }

    @Override
    public synchronized int getBalance() {  // V2와 동일: 읽기 작업도 동기화 필요
        return balance;
    }
}
