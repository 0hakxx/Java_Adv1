package thread.start;

// Thread 클래스를 상속받아 새로운 스레드 클래스를 정의
public class HelloThread extends Thread {

    // run() 메서드 오버라이드
    // - 스레드가 시작되면 실행되는 코드(스레드의 작업 내용)를 정의
    // - start() 메서드를 호출하면 JVM이 내부적으로 run()을 실행함
    @Override
    public void run() {
        // 현재 실행 중인 스레드의 이름과 "run()"을 출력
        // Thread.currentThread().getName()을 통해 스레드 이름을 얻을 수 있음
        System.out.println(Thread.currentThread().getName() + ": run()");
    }
}

/*
[상세 설명]
- Thread 클래스를 상속받아 사용자 정의 스레드 클래스를 만든다.
- run() 메서드는 스레드가 실제로 실행할 코드를 작성하는 곳이다.
- HelloThread 인스턴스를 생성한 뒤 start()를 호출하면,
  새로운 스레드가 생성되고, 그 스레드에서 run() 메서드가 실행된다.
- run() 메서드 안에서는 현재 실행 중인 스레드의 이름을 출력한다.
  (예: "Thread-0: run()")
- 직접 run()을 호출하면 새로운 스레드가 만들어지지 않고,
  단순히 현재 스레드에서 메서드가 실행된다. 반드시 start()를 통해 실행해야 멀티스레딩이 된다.
*/
