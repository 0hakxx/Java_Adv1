package thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import static util.MyLogger.log;

/**
 * ExecutorUtils
 * - ExecutorService(특히 ThreadPoolExecutor)의 상태를 모니터링하고 출력하는 유틸리티 클래스
 * - 스레드 풀의 현재 풀 크기, 활성 스레드 수, 대기 중인 작업 수, 완료된 작업 수 등을 출력
 */
public abstract class ExecutorUtils {

    /**
     * ExecutorService의 상태를 출력하는 메서드
     * - ThreadPoolExecutor 타입인 경우, 상세 상태 정보를 출력
     * - 그 외의 ExecutorService 구현체인 경우, 객체 정보를 출력
     *
     * @param executorService 상태를 출력할 ExecutorService 인스턴스
     */
    public static void printState(ExecutorService executorService) {
        // 자바 16+ 패턴 매칭을 활용한 instanceof
        if (executorService instanceof ThreadPoolExecutor poolExecutor) {
            // 현재 풀에 있는 스레드 수
            int pool = poolExecutor.getPoolSize();
            // 현재 작업 중인(활성화된) 스레드 수
            int active = poolExecutor.getActiveCount();
            // 작업 큐에 대기 중인 작업의 수
            int queuedTasks = poolExecutor.getQueue().size();
            // 완료된 작업의 총 개수
            long completedTask = poolExecutor.getCompletedTaskCount();
            // 상태 정보 출력
            log("[pool=" + pool + ", active=" + active + ", queuedTasks=" + queuedTasks + ", completedTask=" + completedTask + "]");
        } else {
            // ThreadPoolExecutor가 아닌 경우, 객체 정보만 출력
            log(executorService);
        }
    }

    /**
     * ExecutorService의 상태를 출력하는 오버로딩 메서드 (태스크 이름 포함)
     * - 태스크 이름과 함께 스레드 풀의 상태 정보를 출력
     *
     * @param executorService 상태를 출력할 ExecutorService 인스턴스
     * @param taskName        상태 정보 앞에 붙일 태스크명 또는 식별자
     */
    public static void printState(ExecutorService executorService, String taskName) {
        if (executorService instanceof ThreadPoolExecutor poolExecutor) {
            int pool = poolExecutor.getPoolSize();
            int active = poolExecutor.getActiveCount();
            int queuedTasks = poolExecutor.getQueue().size();
            long completedTask = poolExecutor.getCompletedTaskCount();
            // 태스크명과 함께 상태 정보 출력
            log(taskName + " -> [pool=" + pool + ", active=" + active + ", queuedTasks=" + queuedTasks + ", completedTask=" + completedTask + "]");
        } else {
            log(executorService);
        }
    }
}
