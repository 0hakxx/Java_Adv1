package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static util.MyLogger.log;



/*
생산자 소비자 문제를 실무에서 사용할 때는, 결국 소비자가 처리할 수 없을 정도로 생산 요청이 가득 차면 어떻게 할지
를 정해야 한다. 개발자가 인지할 수 있게 로그도 남겨야 하고, 사용자에게 현재 시스템에 문제가 있다고 알리는 것도 필
요하다.
이런 것을 위해 예외 정책이 필요하다.

ThreadPoolExecutor 에 작업을 요청할 때, 큐도 가득차고, 초과 스레드도 더는 할당할 수 없다면 작업을 거절한다.
ThreadPoolExecutor는 작업을 거절하는 다양한 정책을 제공한다
AbortPolicy : 새로운 작업을 제출할 때 RejectedExecutionException을 발생시킨다. 기본 정책(Default)이다.
DiscardPolicy : 새로운 작업을 조용히 예외처리 없이 버린다.
CallerRunsPolicy : 새로운 작업을 제출한 스레드가 대신해서 직접 작업을 실행한다.
사용자 정의(RejectedExecutionHandler): 개발자가 직접 정의한 거절 정책을 사용할 수 있다.
참고로 ThreadPoolExecutor를 shutdown()하면 이후에 요청하는 작업을 거절하는데, 이때도 같은 정책이 적용된다.
*/



public class RejectMainV1 {

    public static void main(String[] args) {

        // SynchronousQueue : 저장 공간이 없는 큐 (직거래 큐) 사용
        // handler = AbortPolicy : 큐와 스레드가 가득 차면 예외(RejectedExecutionException) 발생
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 1, 0, TimeUnit.SECONDS,
                new SynchronousQueue<>(), // 저장 공간이 없는 큐, 작업이 즉시 스레드에 전달되어야 함
                new ThreadPoolExecutor.AbortPolicy() // 거절 정책: 예외 발생
        );

        // 첫 번째 작업 제출
        executor.submit(new RunnableTask("task1"));

        try {
            // 두 번째 작업 제출 시도
            executor.submit(new RunnableTask("task2"));
            // - 이미 스레드 1개가 task1을 실행 중이므로, 추가 스레드 생성 불가 (maximumPoolSize=1)
            // - SynchronousQueue는 큐에 쌓을 수 없으므로, 대기도 불가
            // - 따라서 task2는 거절(RejectedExecutionException) 발생!
        } catch (RejectedExecutionException e) {
            log("요청 초과"); // 작업 거절 시 메시지 출력
            // 이 시점에서 개발자는 포기, 재시도, 대기 등 다양한 정책을 고민할 수 있음
            log(e); // 예외 상세 정보 출력
        }

        executor.close(); // 스레드 풀 종료 (Java 21+)
    }
}
