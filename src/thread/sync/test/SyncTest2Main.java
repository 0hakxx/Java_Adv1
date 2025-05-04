package thread.sync.test;

import static util.MyLogger.log;

public class SyncTest2Main {
    public static void main(String[] args) throws InterruptedException {
        MyCounter myCounter = new MyCounter(); // MyCounter 인스턴스 생성

        // 두 개의 스레드가 실행할 작업(Runnable) 정의
        Runnable task = new Runnable() {
            @Override
            public void run() {
                myCounter.count(); // 각 스레드가 count() 메서드 실행
            }
        };

        // 두 개의 스레드 생성, 같은 Runnable(task)과 MyCounter 인스턴스를 공유
        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");

        thread1.start(); // 첫 번째 스레드 시작
        thread2.start(); // 두 번째 스레드 시작

        // (참고) join()을 사용하지 않았으므로 main()이 먼저 종료될 수 있음
    }

    // MyCounter 클래스: count() 메서드에서 지역 변수 연산 수행
    static class MyCounter {

        /**
         * count() 메서드는 0부터 999까지 1씩 증가시키는 연산을 수행.
         * localValue는 count() 메서드 내부에서 선언된 '지역 변수'이므로
         * 각 스레드마다 독립적으로 존재한다.
         * 즉, 여러 스레드가 동시에 count()를 호출해도 localValue는 서로 영향을 주지 않는다.
         */
        public void count() {
            int localValue = 0; // 메서드 내부에 존재하므로, 지역 변수이다.
            for (int i = 0; i < 1000; i++) {
                localValue = localValue + 1; // 1씩 증가
            }
            // 각 스레드는 자신만의 localValue를 1000까지 증가시킨 후 결과를 출력한다.
            log("결과: " + localValue);
        }
    }

}

/*
[주요 설명]
- main()에서 MyCounter 인스턴스를 생성하고, 두 개의 스레드가 동일한 MyCounter 인스턴스의 count()를 호출.
- count()의 localValue는 지역 변수이기 때문에, 각 스레드마다 별도의 스택 프레임에 존재하여 경쟁 조건(race condition)이 발생하지 않는다.
- 두 스레드는 각각 localValue를 0에서 1000까지 증가시키고, "결과: 1000"을 출력한다.
- 만약 localValue가 인스턴스 변수였다면, 동기화(synchronization)가 필요했을 것이다.
*/
