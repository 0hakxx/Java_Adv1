package thread.start;

// HelloRunnable 클래스: Runnable 인터페이스를 구현하여 스레드 실행 코드를 정의
public class HelloRunnable implements Runnable {

    // run() 메서드 오버라이드
    // - 스레드가 실행할 코드를 정의하는 메서드
    // - Thread 클래스의 start() 메서드를 호출하면 JVM이 이 메서드를 실행함
    @Override
    public void run() {
        // 현재 스레드의 이름과 "run()" 메시지 출력
        System.out.println(Thread.currentThread().getName() + ": run()");
    }
}

/*
[상세 설명]
**Thread 상속 vs Runnable 인터페이스**
*   자바에서 스레드를 생성하는 방법은 두 가지가 있습니다.
    *   `Thread` 클래스를 상속받는 방법
    *   `Runnable` 인터페이스를 구현하는 방법
*   `Runnable` 인터페이스를 구현하는 방법이 더 권장됩니다.
    *   자바는 다중 상속을 지원하지 않으므로, `Thread` 클래스를 상속받으면 다른 클래스를 상속받을 수 없습니다.
    *   `Runnable` 인터페이스를 구현하면 다른 클래스를 상속받을 수 있습니다.
    *   코드의 재사용성과 유지보수성이 향상됩니다.
*/