package thread.executor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static thread.executor.ExecutorUtils.*;
import static util.MyLogger.log;

/**
 * ExecutorService의 올바른 종료(shutdown) 패턴을 시연하는 클래스
 * - 스레드 풀의 정상 종료와 강제 종료의 차이점 및 구현 방법을 보여줌
 */
public class ExecutorShutdownMain {

    public static void main(String[] args) {
        // 최대 2개 스레드를 갖는 고정 크기 스레드 풀 생성
        // - 동시에 최대 2개 작업만 병렬 처리 가능
        ExecutorService es = Executors.newFixedThreadPool(2);

        // 총 4개 작업 제출 (2개는 즉시 실행, 2개는 대기열에 저장됨)
        es.execute(new RunnableTask("taskA")); // [출력] pool-1-thread-1: taskA 시작
        es.execute(new RunnableTask("taskB")); // [출력] pool-1-thread-2: taskB 시작
        es.execute(new RunnableTask("taskC")); // 대기열에 들어감 (A나 B 완료 후 실행)
        es.execute(new RunnableTask("longTask", 100_000)); // 약 100초 동안 실행되는 장기 작업, 대기열에 들어감.

        // 현재 ExecutorService 상태 출력 (활성 스레드 수, 큐 크기 등)
        printState(es); // [출력] pool=2, active=2, queuedTasks=2

        log("== shutdown 시작");
        shutdownAndAwaitTermination(es); // [출력] 10초 후 "서비스 정상 종료 실패"
        log("== shutdown 완료");  // [출력] pool=0, active=0, queuedTasks=0

        // 종료 후 ExecutorService 상태 확인 (isShutdown, isTerminated 등)
        printState(es); // [출력] pool=0, active=0, queuedTasks=0
    }

    private static void shutdownAndAwaitTermination(ExecutorService es) {
        es.shutdown(); // non-blocking, 새로운 작업을 받지 않는다. 처리 중이거나, 큐에 이미 대기중인 작업은 처리한다. 이후에 풀의 스레드를 종료한다.

        try {
            // 이미 대기중인 작업들을 모두 완료할 때 까지 10초 기다린다.
            // - 반환값: 제한 시간 내 모든 작업 완료되면 true, 시간 초과면 false
            // - 블로킹 메서드: 작업 완료되거나 타임아웃 될 때까지 현재 스레드 차단
            if (!es.awaitTermination(10, TimeUnit.SECONDS)) { // 10초 기다렸으나, 시간이 초과되어 !false = True되어 아래 코드 실행
                // 정상 종료가 너무 오래 걸리면...
                log("서비스 정상 종료 실패 -> 강제 종료 시도");

                // shutdownNow() 강제 종료 수행:
                // 1. 대기열에 있는 모든 작업을 제거하고 그 목록 반환
                // 2. 현재 실행 중인 모든 작업에 interrupt() 시도
                // 3. 작업 취소는 인터럽트 처리가 올바르게 구현된 경우에만 효과적
                es.shutdownNow(); // [작업 중단] longTask 스레드에 interrupt() 전송

                // 강제 종료 수행 시에도 10초 기다린다.
                // - 추가 10초 대기 (총 최대 20초 대기)
                // - 강제 종료 후에도 일부 작업이 즉시 종료되지 않을 수 있음
                if (!es.awaitTermination(10, TimeUnit.SECONDS)) {
                    log("서비스가 종료되지 않았습니다.");
                    // 20초 이상 걸리는 종료는 더 심각한 문제일 수 있음
                    // 실제 상황에서는 추가 조치 필요 (로깅, 모니터링 알림 등)
                }
            }
        } catch (InterruptedException e) { // 메인 스레드가 인터럽트 받을 경우
            // awaitTermination()으로 대기중인 현재 스레드가 인터럽트 될 수 있다.
            // - 메인 스레드가 인터럽트 받으면 즉시 강제 종료 실행
            // - 정상 종료 대기를 더 이상 기다리지 않고 즉시 중단
            es.shutdownNow();

            // 인터럽트 상태 복원은 호출자가 처리할 수 있도록 함
            // Thread.currentThread().interrupt(); // 선택적 구현
        }
    }
}