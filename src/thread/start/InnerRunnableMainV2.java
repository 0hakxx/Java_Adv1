package thread.start;

import static util.MyLogger.log;

// 익명 내부 클래스를 사용하여 Runnable 인터페이스를 구현한 스레드를 실행하는 메인 클래스
public class InnerRunnableMainV2 {

    public static void main(String[] args) {

        log("main() start");

        // Runnable 객체 생성 (익명 내부 클래스 사용)
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // "run()" 메시지 출력
                log("run()");
            }
        };

        Thread thread = new Thread(runnable);

        // 스레드 시작
        // - start() 메서드를 호출하여 새로운 스레드를 생성하고 실행
        // - run() 메서드는 새로운 스레드에서 실행됨
        thread.start();

        // 메인 스레드의 종료를 알리는 메시지 출력
        log("main() end");
    }

}
