package thread.executor.future;

import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;
/*
- 작업(call())에서 예외가 발생해도, 메인 스레드에는 바로 전달되지 않음.
- 메인 스레드에서 future.get()을 호출할 때,
  - 작업이 예외로 종료되었으면 ExecutionException이 발생.
  - ExecutionException.getCause()로 실제 예외(원인)를 꺼낼 수 있음.
*/

public class FutureExceptionMain {

    public static void main(String[] args) {

        ExecutorService es = Executors.newFixedThreadPool(1);

        log("작업 전달");
        // 예외를 발생시키는 작업(ExCallable)을 스레드풀에 제출
        Future<Integer> future = es.submit(new ExCallable());

        sleep(1000); // 메인 스레드 1초간 대기(작업이 실행될 시간을 줌)

        try {
            // 4. 작업 결과를 받아오기 위해 future.get() 호출
            //    - 작업이 정상 종료되면 결과 반환
            //    - 작업이 예외로 종료되면 ExecutionException 발생
            log("future.get() 호출 시도, future.state(): " + future.state());
            Integer result = future.get();
            log("result value = " + result);
        } catch (InterruptedException e) {
            // get()이 대기 중 인터럽트 되면 이 블록 진입
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            // 실행예외 : 작업 내에서 예외가 발생하면 ExecutionException으로 감싸져서 전달됨
            log("e = " + e); // ExecutionException 출력
            Throwable cause = e.getCause(); // 실제 원인 예외 추출
            log("cause = " + cause); // 원본 예외 출력
        }
        // 6. ExecutorService 종료
        es.close();
    }

    /**
     * ExCallable은 Callable<Integer> 구현체
     * - call() 메서드에서 예외를 발생시킴
     * - 이 예외는 Future.get()에서 ExecutionException으로 감싸져 전달됨
     */
    static class ExCallable implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            log("Callable 실행, 예외 발생");
            throw new IllegalStateException("ex!"); // 예외 발생
        }
    }
}

