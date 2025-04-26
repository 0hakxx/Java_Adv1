package thread.control;

import static util.MyLogger.log;

public class ThreadStateMain {

    public static void main(String[] args) throws InterruptedException {
        // [main] 0초: myThread 객체 생성 (상태: NEW)
        Thread thread = new Thread(new MyRunnable(), "myThread");
        log("myThread.state1 = " + thread.getState());  // start() 호출 전이므로, 상태 : NEW

        // [main] 0초: myThread 시작 (상태: RUNNABLE)
        log("myThread.start()");
        thread.start();

        // [main] 0초~1초: main 스레드 1초 sleep
        //   └─> 이 시간 동안 myThread는 run() 진입, sleep(3000) 호출하여 TIMED_WAITING 상태로 진입
        Thread.sleep(1000);

        // [main] 1초: myThread 상태 확인
        log("myThread.state3 = " + thread.getState());  // TIMED_WAITING

        // [main] 1초~5초: main 스레드 4초 sleep (총 5초 경과)
        //   └─> myThread는 sleep(3000) 끝나고 run() 마저 실행 후 종료(TERMINATED)
        Thread.sleep(4000);

        // [main] 5초: myThread 상태 확인 (TERMINATED)
        log("myThread.state5 = " + thread.getState());  // TERMINATED

        log("end");
    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            try {
                // [myThread] 0초~: run() 진입, 상태는 RUNNABLE
                log("start");
                log("myThread.state2 = " + Thread.currentThread().getState());  // RUNNABLE

                // [myThread] sleep(3000) 호출 (상태: TIMED_WAITING)
                log("sleep() start");
                Thread.sleep(3000); // 0~3초 구간에서 sleep

                // [myThread] sleep 종료, 상태는 다시 RUNNABLE
                log("sleep() end");
                log("myThread.state4 = " + Thread.currentThread().getState());  // RUNNABLE

                // [myThread] run() 끝나면 상태는 TERMINATED로 변경됨
                log("end");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

/*
------------------------------------------------------------
실행 흐름/상태 변화 (시간 순서, main vs myThread)
------------------------------------------------------------

[시각]   [main 스레드]              [myThread 스레드]         [myThread 상태]
---------------------------------------------------------------------------------
 0초     생성, start() 호출          (아직 run() 진입 전)        NEW → RUNNABLE
         ↓
         1초간 sleep                run() 진입, sleep(3000)     TIMED_WAITING
         ↓
 1초     상태 확인(getState)        (여전히 sleep 중)           TIMED_WAITING
         ↓
         4초간 sleep                sleep 종료, run() 계속      RUNNABLE → TERMINATED
         ↓
 5초     상태 확인(getState)        (이미 종료됨)               TERMINATED

------------------------------------------------------------
ASCII 타임라인 그림
------------------------------------------------------------
main:     |---start()---|--sleep(1s)--|--check--|---sleep(4s)---|--check--|
myThread:     |---run()---|---sleep(3s)---|--run()끝--|
상태:    NEW→RUNNABLE  TIMED_WAITING       RUNNABLE→TERMINATED

------------------------------------------------------------
핵심 포인트
------------------------------------------------------------
- 두 스레드는 동시에 실행되며, main 스레드가 sleep을 통해 타이밍을 맞추지 않으면 상태 변화 관찰이 불확실하므로 아래와 같이 sleep 사용
- main에서 sleep(1초) 하는 이유: myThread가 sleep(3초) 중임(TIMED_WAITING 상태)임을 관찰하기 위해.
- main에서 sleep(4초) 추가로 하는 이유: myThread가 run()을 끝내고 TERMINATED 상태가 되는 것을 확인하기 위해.
*/
