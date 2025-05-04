package thread.sync.lock;

import java.util.concurrent.locks.LockSupport;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class LockSupportMainV1 {

    public static void main(String[] args) {
        Thread thread1 = new Thread(new ParkTest(), "Thread-1");
        thread1.start();

        // 잠시 대기하여 Thread-1이 park 상태에 빠질 시간을 준다.
        sleep(100);
        log("Thread-1 state: " + thread1.getState()); // [출력1] Thread-1의 상태 출력 (대부분 WAITING)

        log("main -> unpark(Thread-1)");
        LockSupport.unpark(thread1); // 1. unpark 사용: Thread-1이 park에서 깨어나게 된다.
        //thread1.interrupt(); // 2. interrupt() 사용: unpart() 대신 인터럽트로도 park를 해제할 수 있다.
    }

    static class ParkTest implements Runnable {

        @Override
        public void run() {
            log("park 시작"); // [출력2] park 시작 로그
            LockSupport.park(); // park 호출: 현재 스레드는 대기 상태(WAITING)로 진입
            log("park 종료, state: " + Thread.currentThread().getState()); // [출력3] park 해제 후 상태 출력 (RUNNABLE)
            log("인터럽트 상태: " + Thread.currentThread().isInterrupted()); // [출력4] 인터럽트 여부 출력 (unpark 사용 시 false)
        }
    }
}