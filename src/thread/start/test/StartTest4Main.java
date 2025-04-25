package thread.start.test;
import static util.MyLogger.log;

public class StartTest4Main {

    public static void main(String[] args) {
        // Thread-A 스레드 생성
        // - PrintWork 객체를 생성하여 Thread 객체에 전달
        // - 스레드 이름은 "Thread-A"로 지정
        Thread threadA = new Thread(new PrintWork("A", 1000), "Thread-A");

        // Thread-B 스레드 생성
        // - PrintWork 객체를 생성하여 Thread 객체에 전달
        // - 스레드 이름은 "Thread-B"로 지정
        Thread threadB = new Thread(new PrintWork("B", 500), "Thread-B");

        // 스레드 시작
        // - start() 메서드를 호출하여 각 스레드를 실행
        threadA.start();
        threadB.start();
    }

    static class PrintWork implements Runnable {

        private String content; // 출력할 내용
        private int sleepMs;   // 스레드 일시 정지 시간 (밀리초)

        public PrintWork(String content, int sleepMs) {
            this.content = content;
            this.sleepMs = sleepMs;
        }


        @Override
        public void run() {

            while (true) {
                log(content);
                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

