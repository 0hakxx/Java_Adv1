package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV4 {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(100); // 0.1초 후에 작업 중단 지시
        log("작업 중단 지시 thread.interrupt()");
        thread.interrupt();
        log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted());
    }

    static class MyTask implements Runnable {

        @Override
        public void run() {
            while (!Thread.interrupted()) { // 인터럽트 상태 확인 + 상태 클리어
                log("작업 중");
            }
            // 반복문 탈출 후, 인터럽트 상태는 false
            log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted());

            try {
                log("자원 정리");
                Thread.sleep(1000); // 인터럽트 상태가 이미 false이므로, sleep 정상 동작
                log("자원 종료");
            } catch (InterruptedException e) {
                // 자원 정리 중에 추가 인터럽트가 오면 이 블록이 실행됨
                log("자원 정리 실패 - 자원 정리 중 인터럽트 발생");
                log("work 스레드 인터럽트 상태3 = " + Thread.currentThread().isInterrupted());
            }
            log("작업 종료");
        }
    }
}

/*
==================================================================================
[V3과 V4의 차이점 상세 설명]
==================================================================================
--------------------------------------
- V3: while (!Thread.currentThread().isInterrupted())
      - isInterrupted()는 인터럽트 상태를 "조회"만 하고, 상태를 변경하지 않음
      - 반복문 탈출 후에도 인터럽트 상태가 true로 남아 있음
      - 이 상태에서 sleep(), wait(), join() 등 블로킹 메서드를 호출하면 즉시 InterruptedException 발생
      - 자원 정리 코드가 정상적으로 실행되지 않을 위험이 있음

- V4: while (!Thread.interrupted())
      - 인터럽트 상태라면 true 반환하고, false 으로 바꿔준다,
        인터럽트 상태가 아니라면 false 를 반환하고, 해당 스레드의 인터럽트 상태를 변경하지 않는다.
      - Thread.interrupted()는 인터럽트 상태를 "조회"하면서 동시에 상태를 false(초기화)로 만듦
      - 반복문 탈출 후에는 인터럽트 상태가 false로 바뀜
      - 이후 sleep(), wait(), join() 등 블로킹 메서드 사용 시 InterruptedException이 발생하지 않음
      - 자원 정리 코드가 정상적으로 실행될 수 있음

2. 자원 정리 단계의 안정성
--------------------------
- V3: 반복문 탈출 후 인터럽트 상태가 true → 자원 정리 단계에서 sleep() 등 호출 시 즉시 예외 발생(자원 정리 실패)
- V4: 반복문 탈출 후 인터럽트 상태가 false → 자원 정리 단계에서 sleep() 등 정상 동작(자원 정리 성공)

*/
