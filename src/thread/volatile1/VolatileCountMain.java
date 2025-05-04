package thread.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileCountMain {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t = new Thread(task, "work");
        t.start();

        // 메인 스레드에서 1초(1000ms) 대기
        sleep(1000);

        // 1초 후, task의 flag를 false로 변경하여 while 루프 종료 유도
        task.flag = false;
        // flag와 count 값을 로그로 출력 (main 스레드에서)
        log("flag = " + task.flag + ", count = " + task.count + " in main");
    }

    static class MyTask implements Runnable {
        // volatile 키워드 사용: 여러 스레드에서 값의 변경이 즉시 반영됨을 보장
        volatile boolean flag = true; // 루프를 제어하는 플래그
        volatile long count;

        @Override
        public void run() {
            while(flag) {
                count++; // count 증가
                if (count % 100_000_000 == 0) {
                    log("flag = " + flag + ", count = " + count + " in while()");
                }
            }
            log("flag = " + flag + ", count = " + count + " 종료");
        }
    }
}
