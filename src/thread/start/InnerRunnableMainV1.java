package thread.start;

import static util.MyLogger.log;

// 중첩 클래스를 사용하여 Runnable 인터페이스를 구현한 스레드를 실행하는 메인 클래스
public class InnerRunnableMainV1 {

    public static void main(String[] args) {
        // 메인 스레드의 시작을 알리는 메시지 출력
        log("main() start");

        // MyRunnable 객체 생성
        // - 내부 클래스 MyRunnable의 객체를 생성
        // - 이 객체는 스레드가 실행할 코드를 담고 있음
        MyRunnable runnable = new MyRunnable();
        Thread thread = new Thread(runnable);
        thread.start();

        // 메인 스레드의 종료를 알리는 메시지 출력
        log("main() end");
    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            // "run()" 메시지 출력
            log("run()");
        }
    }

}