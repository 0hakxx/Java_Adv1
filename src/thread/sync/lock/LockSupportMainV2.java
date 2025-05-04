package thread.sync.lock;

import java.util.concurrent.locks.LockSupport;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class LockSupportMainV2 {

    public static void main(String[] args) {
        Thread thread1 = new Thread(new ParkTest(), "Thread-1");
        thread1.start();

        // 잠시 대기하여 Thread-1이 park 상태에 빠질 시간을 준다.
        sleep(100);
        log("Thread-1 state: " + thread1.getState()); // [V1과 동일] Thread-1의 현재 상태 출력 (대부분 WAITING 또는 TIMED_WAITING)
    }

    static class ParkTest implements Runnable {

        @Override
        public void run() {
            log("park 시작");
            // [V1과의 차이점]
            // - V1: LockSupport.park()를 사용해 무한 대기(외부에서 unpark() 또는 interrupt() 필요)
            // - V2: LockSupport.parkNanos(2000_000000) 사용 → 2초(2,000,000,000ns) 동안만 대기 후 자동 해제됨
            //   즉, 외부에서 unpark()나 interrupt()가 없어도 2초 후 자동으로 park 상태가 풀림
            LockSupport.parkNanos(2000_000000);  // parkNanos 사용
            log("park 종료, state: " + Thread.currentThread().getState());
            log("인터럽트 상태: " + Thread.currentThread().isInterrupted());
        }
    }
}

/*
[V1과 V2의 주요 차이]
- V1: LockSupport.park() → 외부에서 unpark()나 interrupt()가 있어야 park 해제
- V2: LockSupport.parkNanos(long nanos) → 지정한 시간(여기선 2초) 후 자동으로 park 해제 (외부 신호 불필요)
- 따라서 V2는 별도의 unpark 호출이 없고, 일정 시간 후 스레드가 자동으로 깨어남
*/
