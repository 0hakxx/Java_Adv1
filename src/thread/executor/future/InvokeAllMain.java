package thread.executor.future;

import thread.executor.CallableTask;

import java.util.List;
import java.util.concurrent.*;

import static util.MyLogger.log;

public class InvokeAllMain {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 1. 10개의 스레드를 가진 ExecutorService(스레드풀) 생성
        ExecutorService es = Executors.newFixedThreadPool(10);

        // 2. 각각 실행 시간이 다른 CallableTask 3개 생성
        CallableTask task1 = new CallableTask("task1", 1000);  // 1초 후 결과 반환
        CallableTask task2 = new CallableTask("task2", 2000);  // 2초 후 결과 반환
        CallableTask task3 = new CallableTask("task3", 3000);  // 3초 후 결과 반환

        // 3. 작업들을 리스트로 묶음
        List<CallableTask> tasks = List.of(task1, task2, task3);

        // 4. invokeAll()로 여러 작업을 한 번에 제출
        //    - 모든 작업이 완료될 때까지 블로킹
        //    - 각 작업의 결과를 받을 수 있는 Future 리스트 반환
        List<Future<Integer>> futures = es.invokeAll(tasks);

        // 5. 각 Future에서 결과 꺼내기 (모든 작업이 끝났으므로 get()은 바로 반환 가능)
        for (Future<Integer> future : futures) {
            Integer value = future.get(); // 각 작업의 반환값(Integer) 획득
            log("value = " + value);      // 결과 출력
        }

        // 6. ExecutorService 종료
        es.close();
    }
}

