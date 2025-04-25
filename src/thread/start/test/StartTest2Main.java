package thread.start.test;

import static util.MyLogger.log;

public class StartTest2Main {

    public static void main(String[] args) {
        CounterRunnable counterRunnable = new CounterRunnable();
        Thread thread = new Thread(counterRunnable);
        thread.start();
    }

    static class CounterRunnable implements Runnable {
        @Override
        public void run() {
            // 1부터 5까지 반복하며 카운터 값을 출력하고 1초 동안 대기
            for (int i = 1; i <= 5; i++) {
                // 카운터 값을 로그로 출력
                log("value: " + i);

                // 1초 동안 스레드를 일시 정지
                // - Thread.sleep() 메서드를 사용하면 스레드를 지정된 시간 동안 일시 정지시킬 수 있음
                // - InterruptedException 예외가 발생할 수 있으므로 try-catch 블록으로 감싸야 함
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // InterruptedException 예외 처리
                    // - 예외가 발생하면 RuntimeException으로 감싸서 다시 던짐
                    // - 스레드 실행 중 예외가 발생하면 스레드가 종료될 수 있음
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
