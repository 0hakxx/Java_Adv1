package thread.start;

public class HelloThreadMain {

    public static void main(String[] args) {
        // 메인 스레드의 이름과 "main() start"를 출력
        // Thread.currentThread().getName()을 통해 스레드 이름을 얻을 수 있음 (일반적으로 "main")
        System.out.println(Thread.currentThread().getName() + ": main() start");

        // HelloThread 객체 생성 (새로운 스레드 객체 생성)
        HelloThread helloThread = new HelloThread();

        // start() 호출 전 상태 출력
        System.out.println(Thread.currentThread().getName() + ": start() 호출 전");

        // helloThread.start() 호출
        // - 새로운 스레드를 생성하고, 해당 스레드에서 HelloThread 객체의 run() 메서드를 실행
        // - start() 메서드는 즉시 반환되며, run() 메서드는 별도의 스레드에서 비동기적으로 실행됨
        helloThread.start();

        // start() 호출 후 상태 출력
        System.out.println(Thread.currentThread().getName() + ": start() 호출 후");

        // 메인 스레드의 "main() end" 출력
        System.out.println(Thread.currentThread().getName() + ": main() end");
    }
}
