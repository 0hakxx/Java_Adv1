package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV1 {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();
        sleep(4000);// main 스레드는 4초간 대기 (작업 스레드가 한 번 이상 작업을 수행할 수 있도록 시간 확보)
        // 4초 후 작업 중단 지시
        log("작업 중단 지시 runFlag=false");
        // 작업 스레드의 runFlag를 false로 변경하여 while 루프 종료 유도
        task.runFlag = false;
    }

    static class MyTask implements Runnable {


        // volatile: 여러 스레드에서 runFlag 값을 즉시 반영하도록 보장
        volatile boolean runFlag = true;// true로 설정해도, 외부에서 false로 바꿔 스레드 종료 가능함!

        @Override
        public void run() {
            // runFlag가 true인 동안 반복 작업 수행
            while (runFlag) {
                log("작업 중");
                sleep(3000); // 3초마다 "작업 중" 로그 출력
            }
            // runFlag가 false가 되면 반복문 종료 → 자원 정리 및 종료 로그 출력
            log("자원 정리");
            log("자원 종료");
        }
    }
}
