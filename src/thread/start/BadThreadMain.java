package thread.start;

//Thread의 안 좋은 예시 클래스
public class BadThreadMain {

    public static void main(String[] args) {

        System.out.println(Thread.currentThread().getName() + ": main() start");


        HelloThread helloThread = new HelloThread();


        System.out.println(Thread.currentThread().getName() + ": start() 호출 전");

        // helloTrhead.start()가 아닌 helloThread.run()을 직접 호출 (잘못된 방식!)
        // - Stack에 새로운 스레드를 생성하지 않고, 단순히 현재 스레드(메인 스레드)에서 run() 메서드를 실행
        // - 멀티스레딩의 효과를 얻을 수 없음
        helloThread.run();

        // run() 호출 후 상태 출력
        System.out.println(Thread.currentThread().getName() + ": start() 호출 후");

        // 메인 스레드의 "main() end" 출력
        System.out.println(Thread.currentThread().getName() + ": main() end");
    }
}

/*
[상세 설명]
- BadThreadMain 클래스는 HelloThreadMain 클래스와 비슷하지만,
  스레드를 실행하는 방식에 중요한 차이가 있다.
- HelloThreadMain 클래스에서는 helloThread.start()를 호출하여 새로운 스레드를 생성하고,
  해당 스레드에서 run() 메서드를 실행한다.
- 반면, BadThreadMain 클래스에서는 helloThread.run()을 직접 호출한다.
- run() 메서드를 직접 호출하는 것은 새로운 스레드를 생성하지 않고,
  단순히 현재 스레드(메인 스레드)에서 메서드를 실행하는 것과 같다.
- 따라서 멀티스레딩의 효과를 얻을 수 없으며, 모든 코드는 메인 스레드에서 순차적으로 실행된다.
- 이 경우 "start() 호출 전", "run() 메서드", "start() 호출 후", "main() end"가
  모두 메인 스레드에서 순서대로 실행되므로, HelloThreadMain 클래스와는 다른 결과가 나타난다.

[결론]
- 스레드를 실행하려면 반드시 start() 메서드를 호출해야 한다.
- run() 메서드를 직접 호출하는 것은 멀티스레딩이 아니므로 주의해야 한다.
*/
