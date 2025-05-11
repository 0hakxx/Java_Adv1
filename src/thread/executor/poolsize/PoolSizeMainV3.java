package thread.executor.poolsize;

import thread.executor.RunnableTask;

import java.util.concurrent.*;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

/**
 * 캐쉬 스레드 풀 사용 예제
 *
 * - corePoolSize: 0, maximumPoolSize: Integer.MAX_VALUE, keepAliveTime: 3초, queue: SynchronousQueue
 * - SynchronousQueue는 저장 공간이 없는 특별한 블로킹 큐로, 생산자(작업 제출자)와 소비자(스레드)가 직접 작업을 주고받음.
 * - 작업이 들어오면 바로 새로운 스레드를 생성해서 처리(대기 없이 즉시 실행), 초과 스레드 수도 제한 없음.
 * - 작업이 끝난 스레드는 3초 동안 대기 후 자동 종료(keepAliveTime).
 *
 * [초과 스레드의 수는 제한이 없다.]
 * [모든 요청이 대기하지 않고 스레드가 바로바로 처리하여 빠른 처리가 가능하다.]
 *
 * [SynchronousQueue는 특별한 블로킹 큐이다.]
 * [내부에 저장 공간이 없다. 대신에 생산자의 작업을 소비자 스레드에게 직접 전달한다.]
 * [즉, 저장 공간의 크기가 0이고 생산자 스레드가 큐에 작업을 전달하면 소비자 스레드가 큐에서 작업을 꺼낼 때까지 대기한다.]
 * [소비자 작업을 요청하면 기다리던 생산자가 소비자에게 직접 작업을 전달하고 반환된다. 그 반대의 경우도 같다.]
 * [이름 그대로 생산자와 소비자를 동기화하는 큐이다.]
 * [쉽게 이야기해서 중간에 버퍼를 두지 않는 스레드간 직거래 라고 생각하면 된다.]
 */
public class PoolSizeMainV3 {

    public static void main(String[] args) {
        //ExecutorService es = Executors.newCachedThreadPool();과 동일한 설정의 ThreadPoolExecutor 직접 생성
        ExecutorService es = new ThreadPoolExecutor(
                0, // corePoolSize: 0 (기본적으로 스레드 없음)
                Integer.MAX_VALUE, // maximumPoolSize: 제한 없음 (필요할 때마다 스레드 무한 생성 가능)
                3, TimeUnit.SECONDS, // keepAliveTime: 3초 (작업 없으면 3초 후 스레드 종료)
                new SynchronousQueue<>() // 저장 공간이 없는 큐, 생산자-소비자 직접 연결
        );

        log("pool 생성");
        printState(es); // [pool=0, active=0, queuedTasks=0, completedTask=0]
        // 아직 스레드가 없으므로 pool=0

        // 4개의 작업을 제출
        for (int i = 1; i <= 4; i++) {
            String taskName = "task" + i;
            es.execute(new RunnableTask(taskName));
            printState(es, taskName); // 작업 제출 직후 풀 상태 출력
            // 각 작업 제출 시마다 새로운 스레드가 즉시 생성되어 실행됨 (pool=1~4, active=1~4)
            // SynchronousQueue는 저장 공간이 없으므로, 작업이 큐에 쌓이지 않고 바로바로 스레드에게 전달됨
        }

        sleep(3000); // 작업이 모두 끝날 때까지 대기 (RunnableTask 작업은 1초 소요)
        log("== 작업 수행 완료 ==");
        printState(es); // [pool=4, active=0, queuedTasks=0, completedTask=4]
        // 모든 작업이 끝나서 active=0, pool=4(아직 스레드가 살아있음), completedTask=4

        sleep(3000); // 추가 대기 (keepAliveTime 초과)
        log("== maximumPoolSize 대기 시간 초과 ==");
        printState(es); // [pool=0, active=0, queuedTasks=0, completedTask=4]
        // 3초가 지나면 사용되지 않는 스레드가 모두 종료되어 pool=0

        es.close();
        log("== shutdown 완료 ==");
        printState(es);

    }
}
