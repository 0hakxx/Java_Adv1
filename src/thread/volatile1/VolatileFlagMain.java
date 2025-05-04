package thread.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileFlagMain {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t = new Thread(task, "work");
        log("runFlag = " + task.runFlag);
        t.start();

        sleep(1000);
        log("runFlag를 false로 변경 시도");
        task.runFlag = false;
        log("runFlag = " + task.runFlag);
        log("main 종료");
    }

    static class MyTask implements Runnable {
        //boolean runFlag = true; // [설명] volatile 키워드가 없으면 스레드 간 가시성 문제가 발생하여, 메인 스레드가 종료되어도, work 스레드는 종료되지 않음
        volatile boolean runFlag = true; // [중요] volatile로 선언, 모든 스레드가 즉시 최신 값을 읽음

        @Override
        public void run() {
            log("task 시작");
            while (runFlag) {
                // runFlag가 false로 변하면 탈출
            }
            log("task 종료");
        }
    }
}
