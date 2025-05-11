package thread.executor.poolsize;

import thread.executor.RunnableTask;

import java.util.concurrent.*;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;


/*
실무에서 쓰이는 고정 풀과 캐시 풀에 대한 장점을 이용한 사용자 정의 전략을 구현
1. 일반 상황: 고정 큐를 사용하여 평상 시에 CPU, 메모리 리소스 사용 예측 가능
2. 긴급 상황: 풀 확장으로 병렬 처리 수 증가 → 빠름
3. 거절 상황: 큐와 풀 모두 가득 차면 거절 발생
*/

public class PoolSizeMainV4 {

    // TASK_SIZE를 바꿔가며 실험
    static final int TASK_SIZE = 1100; // 1. 일반 상황: 고정 큐를 사용하여 평상 시에 CPU, 메모리 리소스 사용 예측 가능
    //static final int TASK_SIZE = 1200; // 2. 긴급 상황: 풀 확장으로 병렬 처리 수 증가 → 빠름
    //static final int TASK_SIZE = 1201; // 3. 거절 상황: 큐와 풀 모두 가득 차면 거절 발생

    //일반 상황에서는 1100 초 / 100 개의 스레드 => 작업 시간 : 11초
    //긴급 상황에서는 1200 초 / 200 개의 스레드 => 작업 시간 : 6초

    public static void main(String[] args) {
        ExecutorService es = new ThreadPoolExecutor(
                100, // 항상 100개 스레드 유지
                200, // 최대 200개 스레드까지 확장
                60, TimeUnit.SECONDS, // 초과 스레드는 60초 유휴 후 종료
                // 일반 상황은 추가 스레드는 필요 없음(큐가 아직 안 찼으므로 maximumPoolSize 동작 X)
                // 긴급 상황은 큐가 가득 찬 상태에서 추가로 100개 작업이 들어오면, maximumPoolSize까지 스레드가 확장되어 작업 시간 빠름.
                new ArrayBlockingQueue<>(1000) // 1000개 크기의 고정 큐,

        );
        printState(es);

        long startMs = System.currentTimeMillis();

        // TASK_SIZE만큼 작업 제출
        for (int i = 1; i <= TASK_SIZE; i++) {
            String taskName = "task" + i;
            try {
                es.execute(new RunnableTask(taskName));
                printState(es, taskName);

                // [중요] 일반 상황(TASK_SIZE=1100)에서는
                // - 100개 작업만 즉시 실행(pool=100, active=100)
                // - 나머지 1000개 작업은 큐에 쌓여 대기(queuedTasks=1000)
                // - 큐에 대기한 작업들은 앞의 작업이 끝나야만 순차적으로 실행됨 → 전체 처리 시간 오래 걸림

                // 긴급 상황(TASK_SIZE=1200)에서는
                // - 100개 작업은 즉시 실행, 1000개는 큐 대기
                // - 이후 100개 작업은 큐가 가득 차므로 스레드 풀을 200까지 확장해서 바로 실행
                // - 동시에 200개 작업을 병렬로 처리 → 전체 작업 완료 시간 단축됨

                // 거절 상황(TASK_SIZE=1201)에서는
                // - 1200개까지는 위와 같이 처리
                // - 1201번째 작업부터는 큐와 풀 모두 가득 차서 거절 예외 발생

            } catch (RejectedExecutionException e) {
                // 큐와 풀 모두 가득 차면 작업 거절 예외 발생
                log(taskName + " -> " + e);
            }
        }

        // 풀 종료 (Java 21+: close(), Java 8~17: shutdown())
        es.close();
        long endMs = System.currentTimeMillis();

        // 전체 작업 제출에 걸린 시간 출력
        log("time: " + (endMs - startMs));
    }
}
