package thread.executor.future;

import java.util.Random;
import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class CallableMainV2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. 스레드 1개짜리 ExecutorService(스레드풀) 생성
        ExecutorService es = Executors.newFixedThreadPool(1);

        log("submit() 호출");
        // 2. 작업(Callable 객체)을 스레드풀에 제출
        //    - 작업을 제출하면 즉시 Future 객체가 반환됨 (작업이 끝난 것은 아님)
        //    - Future는 비동기 작업의 결과를 나중에 받아올 수 있는 객체
        Future<Integer> future = es.submit(new MyCallable());
        log("future 즉시 반환, future = " + future);

        // 3. future.get() 호출 시점에 메인 스레드는 결과가 나올 때까지 대기(블로킹)
        //    - 작업이 끝나면 결과를 반환함
        log("future.get() [블로킹] 메서드 호출 시작 -> main 스레드 WAITING");
        Integer result = future.get();
        log("future.get() [블로킹] 메서드 호출 완료 -> main 스레드 RUNNABLE");

        // 4. 결과 출력
        log("result value = " + result);
        log("future 완료, future = " + future);

        // 5. ExecutorService 자원 해제 (Java 19+에서 close() 사용 가능)
        es.close();
    }

    /**
     * MyCallable 클래스는 Callable<Integer>를 구현.
     * - call() 메서드가 스레드풀에서 실행됨.
     * - 2초간 대기 후 0~9 사이의 랜덤값을 생성해서 반환.
     * - Callable의 call()은 throws Exception이므로, 자식도 예외를 던질 수 있음.
     */
    static class MyCallable implements Callable<Integer> {
        @Override
        public Integer call() throws InterruptedException {
            log("Callable 시작");
            sleep(2000); // 2초간 대기(작업 지연 시뮬레이션)
            int value = new Random().nextInt(10); // 0~9 난수 생성
            log("create value = " + value);
            log("Callable 완료");
            return value; // 작업 결과 반환 (Future에서 받아올 수 있음)
        }
    }
}

/*
-------------------------------------------------------
[Future를 굳이 받아야 하는 이유 - 병렬 처리의 효과]
-------------------------------------------------------
- Future는 비동기(병렬) 작업의 결과를 나중에 받아올 수 있도록 해주는 객체입니다.
- 작업을 submit()으로 스레드풀에 맡긴 뒤, main 스레드는 바로 다음 코드로 진행할 수 있습니다.
- 여러 개의 작업을 동시에 제출하고, 각각의 Future로 결과를 받아오면 진정한 병렬 처리가 가능합니다.
- 예시:
    Future<Integer> f1 = es.submit(new Task1());
    Future<Integer> f2 = es.submit(new Task2());
    // 멀티스레드에게 작업을 맡긴 뒤 요청 스레드(main)는 다른 작업을 하다가...
    Integer r1 = f1.get(); // Task1 결과 대기
    Integer r2 = f2.get(); // Task2 결과 대기
- 이렇게 하면 여러 작업이 동시에 실행되어 전체 처리 시간이 단축됩니다.
- 즉, Future는 병렬 처리를 효과적으로 하기 위해 반드시 필요합니다!
*/
