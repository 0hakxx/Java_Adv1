package thread.executor.poolsize;

import thread.executor.ExecutorUtils;
import thread.executor.RunnableTask;

import java.util.concurrent.*;

import static thread.executor.ExecutorUtils.*;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

/**
 * ThreadPoolExecutor의 corePoolSize, maximumPoolSize, 큐 동작, 작업 거절 정책을 실습하는 예제
 *
 * - corePoolSize: **항상 유지되는(기본) 스레드 개수** (여기서는 2)
 * - maximumPoolSize: **풀에서 동시에 존재할 수 있는 최대 스레드 개수** (여기서는 4)
 * - 작업 큐(ArrayBlockingQueue): **스레드가 모두 바쁠 때 대기 중인 작업을 저장하는 공간** (여기서는 2)
 */

public class PoolSizeMainV1 {

    public static void main(String[] args) {
        // 1. 작업 큐 생성 (최대 2개까지 대기 가능)
        //    - 스레드가 모두 바쁠 때, 추가 작업이 이 큐에 저장됨
        //    - 큐가 가득 차면, 스레드 풀은 최대 크기까지 스레드를 늘려서 작업을 처리함
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);

        // 2. ThreadPoolExecutor 생성
        //    - corePoolSize: 2 (항상 최소 2개 스레드 유지)
        //    - maximumPoolSize: 4 (최대 4개까지 스레드 확장 가능)
        //    - keepAliveTime: 3초 (최대 스레드는 3초 동안 놀고 있으면 제거됨)
        //    - workQueue: 2개까지 작업 대기 가능
        ExecutorService es = new ThreadPoolExecutor(2, 4,
                3000, TimeUnit.MILLISECONDS, workQueue);

        printState(es); // [pool=0, active=0, queuedTasks=0, completedTask=0]

        // 3. 작업 제출 및 상태 출력
        es.execute(new RunnableTask("task1"));
        printState(es, "task1"); // [pool=1, active=1, queuedTasks=0, completedTask=0]
        // → 스레드 1개 생성, task1 바로 실행

        es.execute(new RunnableTask("task2"));
        printState(es, "task2"); // [pool=2, active=2, queuedTasks=0, completedTask=0]
        // → 스레드 2개 생성, task2 바로 실행 (corePoolSize=2까지는 바로 스레드 생성)

        es.execute(new RunnableTask("task3"));
        printState(es, "task3"); // [pool=2, active=2, queuedTasks=1, completedTask=0]
        // → 스레드가 모두 바쁘므로 큐에 task3 저장

        es.execute(new RunnableTask("task4"));
        printState(es, "task4"); // [pool=2, active=2, queuedTasks=2, completedTask=0]
        // → 큐에 task4 저장 (큐 용량 2개 모두 채움)

        // 4. 큐가 가득 찼으므로, 추가 작업은 pool 확장(최대 4개)로 처리
        es.execute(new RunnableTask("task5"));
        printState(es, "task5"); // [pool=3, active=3, queuedTasks=2, completedTask=0]
        // → 큐가 가득 찼으므로, 스레드 풀 크기를 3으로 늘려서 task5 바로 실행

        es.execute(new RunnableTask("task6"));
        printState(es, "task6"); // [pool=4, active=4, queuedTasks=2, completedTask=0]
        // → 최대 스레드 개수(4)까지 확장, task6 바로 실행

        // 5. 최대 스레드, 큐 모두 가득 찬 상태에서 추가 작업 제출 시 예외 발생
        /*
        es.execute(new RunnableTask("task7"));
        printState(es, "task7");
        따라서 아래로 예외처리 진행
        * */
        try {
            es.execute(new RunnableTask("task7"));
        } catch (RejectedExecutionException e) {
            log("task7 실행 거절 예외 발생: " + e);
            // → 스레드(4개)와 큐(2개) 모두 가득 차서 task7은 거절됨
        }

        // 6. 3초 대기 (모든 작업이 완료될 때까지 대기)
        sleep(3000);
        log("== 작업 수행 완료 ==");
        printState(es);
        // [pool=4, active=0, queuedTasks=0, completedTask=6]
        // completedTask=6 → 모든 작업이 끝났지만, pool=4 → 최대 스레드는 위의 keepAliveTime 설정대로 3초 동안 남아 있음

        // 7. 3초 추가 대기 (keepAliveTime 초과 시, 최대 스레드가 corePoolSize로 줄어듦)
        sleep(3000);
        log("== maximumPoolSize 대기 시간 초과 ==");
        printState(es); // [pool=2, active=0, queuedTasks=0, completedTask=6]
        // → 3초가 지나면, corePoolSize(2)만 남고 나머지 스레드는 정리됨

        // 8. 종료
        es.close();
        log("== shutdown 완료 ==");
        printState(es); // [pool=0, active=0, queuedTasks=0, completedTask=6]
        // → 풀 완전히 종료, 모든 스레드와 큐 비움
    }
}
