package thread.control.yield;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class YieldMain {

    static final int THREAD_COUNT = 1000;

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(new MyRunnable());
            thread.start();
        }
    }

    static class MyRunnable implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + " - " + i);

                // 1. empty
                // [설명] 아무것도 하지 않으면, 각 스레드는 반복문을 빠르게 실행하여 CPU를 점유함

                //sleep(1); // 2. sleep
                // [설명] 주석 해제 시, 각 반복마다 1ms 동안 스레드를 일시정지(sleep)
                // [의미] sleep을 사용하면 스레드는 지정 시간 동안 CPU를 양보하고, 다른 스레드가 실행될 기회를 얻음
                // [효과] 스레드 간 실행 순서가 더 섞이게 되고, CPU 점유율이 낮아짐

                Thread.yield(); // 3. yield
                // [중요] yield()는 현재 스레드가 실행 중이던 CPU를 다른 스레드에게 양보하도록 힌트
                // [설명] yield는 반드시 다른 스레드가 실행   된다는 보장은 없지만, 스케줄러에게 "양보 의사"를 전달
                // [효과] 많은 스레드가 있을 때, 각 스레드가 더 자주 CPU를 교체하여 실행될 수 있음
                // [주의] yield는 sleep과 달리, 스레드가 즉시 다시 실행될 수도 있음 (일시정지 아님)
            }
        }
    }
}
