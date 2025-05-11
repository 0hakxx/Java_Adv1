package thread.executor.poolsize;

import thread.executor.RunnableTask;

import java.util.concurrent.*;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;

/**
 * FixedThreadPool(고정 크기 스레드 풀) 예제
 * 고정 크기 스레드 풀의 큐의 개수는 LinkedBlockingQueue으로 무제한으로 가능.
 * 고정 스레드의 수는 스레드 수가 고정되어 CPU,메모리 리소스 예측이 가능한 장점
 * 단점은 점진적인 사용자가 확대, 갑작스런 요청 증가에서  응답이 느려지는 단점이 존재
 */
public class PoolSizeMainV2 {

    public static void main(String[] args) {
        // 1. 고정 크기 스레드 풀(2개 스레드) 생성
        ExecutorService es = Executors.newFixedThreadPool(2);
        // 자바에서 제공하는 newFiexdTrheadPool(2) 는 내부적으로 아래와 같이 동작
        // ExecutorService es = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());


        log("pool 생성");
        printState(es); // [pool=0, active=0, queuedTasks=0, completedTask=0]
        // 아직 스레드가 생성되지 않았으므로 pool=0

        // 2. 6개의 작업을 스레드 풀에 제출
        for (int i = 1; i <= 6; i++) {
            String taskName = "task" + i;
            es.execute(new RunnableTask(taskName));
            printState(es, taskName); // 각 작업 제출 후 풀 상태 출력
        }

        // 3. 스레드 풀 종료 (Java 21 이상: close(), Java 8~17: shutdown())
        es.close();
        log("== shutdown 완료 ==");
    }
}
