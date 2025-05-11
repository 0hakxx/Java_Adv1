package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * CallerRunsPolicy 거절 정책 실습 예제
 * CallerRunsPolicy 정책은 작업을 거절하는 대신, 요청한 스레드(main)스레드가 대신 일을 해준다.
 */
public class RejectMainV3 {

    public static void main(String[] args) {
        // 스레드 풀 생성: 스레드 1개, 대기 큐 없음, CallerRunsPolicy 사용
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 1, 0, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        // 1. 첫 번째 작업 제출
        executor.submit(new RunnableTask("task1"));
        // - pool-1-thread-1이 즉시 생성되어 task1을 실행 (task1 시작/완료 로그는 pool-1-thread-1에서 찍힘)

        // 2. 두 번째 작업 제출
        executor.submit(new RunnableTask("task2"));
        // - 이미 pool-1-thread-1이 task1을 실행 중이고, SynchronousQueue는 대기 불가
        // - 따라서 거절 정책(CallerRunsPolicy)이 동작: "작업을 제출한 main 스레드"가 직접 task2를 실행
        // - task2 시작/완료 로그는 main 스레드에서 찍힘

        // 3. 세 번째 작업 제출
        executor.submit(new RunnableTask("task3"));
        // - 이 시점에서 main과 pool-1-thread-1 모두 바쁠 수 있으나,
        //   task1과 task2가 끝난 뒤라면 pool-1-thread-1이 다시 사용 가능
        // - pool-1-thread-1이 task3을 실행 (task3 시작/완료 로그는 pool-1-thread-1에서 찍힘)

        // 4. 네 번째 작업 제출
        executor.submit(new RunnableTask("task4"));
        // - pool-1-thread-1이 task3을 실행 중이면, 또 거절 정책이 동작하여 main 스레드가 task4를 실행
        // - task4 시작/완료 로그는 main 스레드에서 찍힘

        executor.close();
    }
}
