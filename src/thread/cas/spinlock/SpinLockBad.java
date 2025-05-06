package thread.cas.spinlock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

/**
 * 잘못된(비효율적이고 안전하지 않은) 스핀락(Spin Lock) 구현 예제
 * - 멀티스레드 환경에서 락을 획득할 때까지 반복적으로 확인(바쁜 대기, busy waiting)하는 방식
 * - 원자적(atomic) 연산이 아니므로 동시성 문제(경쟁 조건, race condition)가 발생할 수 있음
 */
public class SpinLockBad {

    // 락의 상태를 나타내는 변수 (true: 락 획득됨, false: 락 해제됨)
    // volatile로 선언해 여러 스레드에서 값 변경이 즉시 반영되도록 함
    private volatile boolean lock = false;

    /**
     * 락을 획득하는 메서드
     * - 락이 해제되어 있으면(lock == false) 락을 획득(lock = true)
     * - 락이 이미 다른 스레드에 의해 획득된 경우, 반복적으로 락 상태를 확인하며 대기(스핀)
     * - 문제점: 락 획득/설정 과정이 원자적이지 않아서 여러 스레드가 동시에 락을 획득할 수 있음 (race condition)
     */
    public void lock() {
        log("락 획득 시도");
        while(true) {
            if (!lock) { // 1. 락 사용 여부 확인
                sleep(100); // (문제 상황 확인용) 일부러 대기시켜 더 쉽게 관찰
                lock = true; // 2. 락의 값 변경 (문제: 이 부분이 원자적이지 않음!)
                break;
            } else {
                // 락을 획득할 때까지 스핀 대기(바쁜 대기, busy waiting)
                log("락 획득 실패 - 스핀 대기");
            }
        }
        log("락 획득 완료");
    }

    /**
     * 락을 해제하는 메서드
     * - lock 값을 false로 변경해서 다른 스레드가 락을 획득할 수 있게 함
     */
    public void unlock() {
        lock = false;
        log("락 반납 완료");
    }
}
