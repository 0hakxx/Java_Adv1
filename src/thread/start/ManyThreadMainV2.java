package thread.start;

import static util.MyLogger.log;

public class ManyThreadMainV2 {

    public static void main(String[] args) {
        log("main() start");

        // HelloRunnable 객체 생성
        HelloRunnable runnable = new HelloRunnable();

        // 100개의 스레드 생성 및 실행
        for (int i = 0; i < 100; i++) {
            // Thread 객체 생성 및 Runnable 객체 전달
            // - Thread 클래스의 생성자에 Runnable 객체를 전달하여 새로운 스레드 생성
            // - 각 스레드는 동일한 Runnable 객체(runnable)를 공유함
            Thread thread = new Thread(runnable);

            thread.start();
        }

        // 메인 스레드의 종료를 알리는 메시지 출력
        log("main() end");
    }
}

/*
[상세 설명]
- ManyThreadMainV2 클래스는 HelloRunnable 객체를 사용하여 100개의 스레드를 생성하고 실행하는 예제이다.
- 각 스레드는 HelloRunnable 객체의 run() 메서드를 실행하며, 이 메서드는 스레드 이름을 콘솔에 출력한다.
- 모든 스레드는 동일한 HelloRunnable 객체를 공유하므로, 모든 스레드는 동일한 코드를 실행한다.
- 각 스레드는 독립적으로 실행되므로, 스레드의 실행 순서는 예측할 수 없다.
- 따라서 "main() end" 메시지가 모든 스레드의 출력이 완료되기 전에 출력될 수 있다.
*/
