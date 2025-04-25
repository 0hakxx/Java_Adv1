package thread.start.test;

import static util.MyLogger.log;

public class StartTest1Main {

    public static void main(String[] args) {

        CounterThread thread = new CounterThread();

        // 스레드 시작
        // - start() 메서드를 호출하여 새로운 스레드를 생성하고 실행
        // - run() 메서드는 새로운 스레드에서 실행됨
        thread.start();
    }

    // CounterThread 클래스: Thread 클래스를 상속받아 스레드 실행 코드를 정의
    static class CounterThread extends Thread {
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

/*
[상세 설명]
- StartTest1Main 클래스는 CounterThread 클래스를 사용하여 스레드를 생성하고 실행하는 예제이다.
- CounterThread 클래스는 Thread 클래스를 상속받아 스레드가 실행할 코드를 정의한다.
- run() 메서드에서는 1부터 5까지 반복하며 카운터 값을 로그로 출력하고, 1초 동안 스레드를 일시 정지시킨다.
- main() 메서드에서는 CounterThread 객체를 생성하고, start() 메서드를 호출하여 스레드를 시작한다.
- main() 메서드가 종료되면 스레드는 독립적으로 실행된다.
*/
