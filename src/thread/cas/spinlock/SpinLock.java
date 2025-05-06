package thread.cas.spinlock;

import java.util.concurrent.atomic.AtomicBoolean;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

/**
 * AtomicBoolean을 이용한 안전한 스핀락(Spin Lock) 구현 예제
 * SpinLockBad(안 좋은 예시 코드)를 올바르게 구현
 * - compareAndSet을 활용해 락 획득/해제를 원자적으로 처리
 */
public class SpinLock {

    // 락의 상태를 나타내는 변수 (true: 락 획득됨, false: 락 해제됨)
    // AtomicBoolean을 사용하여 락 상태 변경을 원자적으로 처리
    private final AtomicBoolean lock = new AtomicBoolean(false);

    /**
     * 락을 획득하는 메서드
     * - compareAndSet(false, true): 현재 값이 false(해제)면 true(획득)로 변경
     * - 여러 스레드가 동시에 접근해도 단 한 스레드만 락을 획득할 수 있음
     * - 락이 이미 다른 스레드에 의해 점유된 경우, 반복적으로 락 상태를 확인하며 대기(스핀)
     */
    public void lock() {
        log("락 획득 시도");
        // compareAndSet이 실패하면(다른 스레드가 이미 락을 획득한 경우) 계속 반복
        while (!lock.compareAndSet(false, true)) {
            // 락을 획득할 때까지 스핀 대기(바쁜 대기, busy waiting)
            log("락 획득 실패 - 스핀 대기");
        }
        log("락 획득 완료");
    }

    /**
     * 락을 해제하는 메서드
     * - lock 값을 false로 변경하여 다른 스레드가 락을 획득할 수 있게 함
     */
    public void unlock() {
        lock.set(false);
        log("락 반납 완료");
    }
}
