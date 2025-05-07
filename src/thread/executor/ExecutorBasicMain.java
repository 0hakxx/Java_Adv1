package thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static thread.executor.ExecutorUtils.*;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;


public class ExecutorBasicMain {

    public static void main(String[] args) {

        ExecutorService es = new ThreadPoolExecutor(
                2, // corePoolSize: 항상 유지할 스레드 수
                2, // maximumPoolSize: 최대 스레드 수 (여기선 core와 같음)
                0, // keepAliveTime: 초과 스레드가 살아있는 시간 (core == max면 의미 없음)
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>() // 작업 큐 (무제한)
        );

        // 초기 상태 출력
        log("== 초기 상태 ==");
        printState(es);

        // 작업 4개를 스레드 풀에 제출
        // 스레드 풀 크기가 2이므로, 2개는 바로 실행되고 2개는 큐에 대기
        es.execute(new RunnableTask("taskA"));
        es.execute(new RunnableTask("taskB"));
        es.execute(new RunnableTask("taskC"));
        es.execute(new RunnableTask("taskD"));

        // 작업 수행 중 상태 출력
        log("== 작업 수행 중 ==");
        printState(es);

        // 3초 대기 (작업이 모두 끝날 때까지 기다리는 시간)
        sleep(3000);

        // 작업 수행 완료 후 상태 출력
        log("== 작업 수행 완료 ==");
        printState(es);

        // 스레드 풀 종료 (shutdown)
        es.close(); // Java 21 이상에서 지원. Java 8~17에서는 es.shutdown() 사용
        log("== showdown 완료 ==");
        printState(es);
    }
}
