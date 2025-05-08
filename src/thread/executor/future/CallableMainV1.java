package thread.executor.future;

import java.util.Random;
import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class CallableMainV1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(1);

        // Callable은 결과 값을 반환할 수 있음 (vs Runnable은 void 반환)
        Future<Integer> future = es.submit(new MyCallable());



        Integer result = future.get();
        log("result value = " + result);

        // 명시적 리소스 정리 필요 (vs RunnableMain의 Thread는 자동 종료)
        es.close();
    }

    static class MyCallable implements Callable<Integer> {
        @Override
        public Integer call() {  // Callable은 리턴 타입 지정 가능 (vs Runnable의 void run())
            log("Callable 시작");
            sleep(2000);
            int value = new Random().nextInt(10);
            log("create value = " + value);
            log("Callable 완료");
            return value;  // 결과 직접 반환 (vs Runnable은 필드에 저장해야 함)
        }
    }
}
