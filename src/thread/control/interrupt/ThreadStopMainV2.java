package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV2 {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        // main 스레드는 4초간 대기 (작업 스레드가 한 번 이상 작업을 수행할 수 있도록 시간 확보)
        sleep(4000);

        // 4초 후 작업 중단 지시
        log("작업 중단 지시 thread.interrupt() 호출");
        // [V2의 핵심] 스레드에 interrupt() 호출 → 스레드의 인터럽트 상태를 true로 설정
        thread.interrupt();

        // interrupt() 호출 직후 스레드의 인터럽트 상태 확인 (true)
        log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted());
    }

    static class MyTask implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    log("작업 중");
                    Thread.sleep(3000); // 3초마다 "작업 중" 로그 출력
                    // [핵심] sleep() 도중 interrupt()가 호출되면 즉시 InterruptedException 발생
                }
            } catch (InterruptedException e) {
                log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted());
                log("interrupt message=" + e.getMessage());
                log("state=" + Thread.currentThread().getState());
            }
            // 예외 처리 후 자원 정리 및 종료 로그 출력
            log("자원 정리");
            log("자원 종료");
        }
    }
}

/*
==================================================================================
[V1과 V2의 차이점]
즉각 종료 가능 여부
----------------------
- V1: sleep()이 끝나기 전에는 종료 신호(runFlag=false)를 확인할 수 없어 즉각 종료가 불가함
- V2: sleep() 중에도 interrupt() 호출 시 즉시 InterruptedException이 발생하여 즉각 종료 가능

4. 실무적 의미
---------------
- V1: 안전하지만, sleep 등 블로킹 작업 중에는 즉각 반응하지 못함
- V2: 블로킹 작업(sleep, wait, join 등) 중에도 즉각 반응 가능, 더 실용적이고 빠른 종료가 필요할 때 적합
*/
