package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static util.MyLogger.log;

public class RejectMainV4 {

    public static void main(String[] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 1, 0, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new MyRejectedExecutionHandler() // 커스텀 거절 핸들러 등록
        );

        // 첫 번째 작업 제출
        executor.submit(new RunnableTask("task1"));
        // - pool-1-thread-1이 즉시 생성되어 task1 실행 시작
        // - SynchronousQueue는 큐에 쌓을 수 없으므로, 반드시 즉시 실행될 수 있어야 함

        // 두 번째 작업 제출
        executor.submit(new RunnableTask("task2"));
        // - 이미 pool-1-thread-1이 task1을 실행 중이므로, 추가 스레드 생성 불가 (maximumPoolSize=1)
        // - SynchronousQueue는 큐에 쌓을 수 없으므로, 대기도 불가
        // - 따라서 task2는 거절됨 → MyRejectedExecutionHandler의 rejectedExecution() 메서드 호출
        // - "[경고] 거절된 누적 작업 수: 1" 로그 출력

        // 세 번째 작업 제출
        executor.submit(new RunnableTask("task3"));
        // - 위와 동일하게 task3도 거절됨 → rejectedExecution() 호출
        // - "[경고] 거절된 누적 작업 수: 2" 로그 출력

        executor.close(); // 스레드 풀 종료 (Java 21+)
    }

    // 커스텀 거절 정책 구현
    static class MyRejectedExecutionHandler implements RejectedExecutionHandler {

        // 거절된 작업의 누적 개수를 세기 위한 AtomicInteger
        static AtomicInteger count = new AtomicInteger(0);

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // 작업이 거절될 때마다 누적 카운트 증가 및 경고 로그 출력
            int i = count.incrementAndGet();
            log("[경고] 거절된 누적 작업 수: " + i);
        }
    }

}
