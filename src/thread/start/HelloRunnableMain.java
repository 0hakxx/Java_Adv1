package thread.start;

public class HelloRunnableMain {

    public static void main(String[] args) {
        // 메인 스레드의 시작을 알리는 메시지 출력
        System.out.println(Thread.currentThread().getName() + ": main() start");

        // HelloRunnable 객체 생성
        // - Runnable 인터페이스를 구현한 클래스의 객체를 생성
        // - 이 객체는 스레드가 실행할 코드를 담고 있음
        HelloRunnable runnable = new HelloRunnable();

        // Thread 객체 생성 및 Runnable 객체 전달
        // - Thread 클래스의 생성자에 Runnable 객체를 전달하여 새로운 스레드 생성
        // - 이 스레드는 Runnable 객체의 run() 메서드를 실행하게 됨
        Thread thread = new Thread(runnable);

        // 스레드 시작
        // - start() 메서드를 호출하여 새로운 스레드를 생성하고 실행
        // - run() 메서드는 새로운 스레드에서 실행됨
        thread.start();

        // 메인 스레드의 종료를 알리는 메시지 출력
        System.out.println(Thread.currentThread().getName() + ": main() end");
    }
}