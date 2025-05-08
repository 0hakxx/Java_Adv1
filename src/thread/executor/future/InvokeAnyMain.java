package thread.executor.future;

import thread.executor.CallableTask;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.MyLogger.log;

/*
invokeAny 는 invokeAll 와 달리 모든 결과가 필요한 게 아니라 "가장 빠른 결과 하나"만 필요할 때 사용
invokeAny 는 여러 작업 중 가장 먼저 성공적으로 끝난 작업의 결과 값 하나만 반환하는 메서드이기 때문에, 결과를 담는 객체로 Future가 아니라
해당 결과 타입(T) 자체를 반환하고 나머지 작업은 취소한다.
반면, invokeAll은 모든 작업의 결과를 받아야 하므로 각 작업의 상태와 결과를 추적할 수 있는 Future을 반환한다.
*/

public class InvokeAnyMain {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 1. 10개의 스레드를 가진 ExecutorService(스레드풀) 생성
        ExecutorService es = Executors.newFixedThreadPool(10);

        // 2. 각각 실행 시간이 다른 CallableTask 3개 생성
        CallableTask task1 = new CallableTask("task1", 1000);  // 1초 후 결과 반환
        CallableTask task2 = new CallableTask("task2", 2000);  // 2초 후 결과 반환
        CallableTask task3 = new CallableTask("task3", 3000);  // 3초 후 결과 반환

        // 3. 작업들을 리스트로 묶음
        List<CallableTask> tasks = List.of(task1, task2, task3);

        // 4. invokeAny()로 여러 작업을 한 번에 제출
        //    - 여러 작업 중 "가장 먼저 끝나는 작업의 결과"만 반환
        //    - 나머지 작업은 자동으로 취소됨
        //    - 가장 빨리 끝난 작업의 결과를 value에 저장
        Integer value = es.invokeAny(tasks);

        // 5. 가장 먼저 완료된 작업의 결과 출력
        log("value = " + value);

        // 6. ExecutorService 종료
        es.close();
    }
}

