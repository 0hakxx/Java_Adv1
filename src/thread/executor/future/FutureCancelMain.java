package thread.executor.future;

import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class FutureCancelMain {


    //private static boolean mayInterruptIfRunning = true; // 실행 중에도 인터럽트로 취소
    private static boolean mayInterruptIfRunning = false; // 실행 중이면 인터럽트 없이 취소 시도

    public static void main(String[] args) {

        ExecutorService es = Executors.newFixedThreadPool(1);


        Future<String> future = es.submit(new MyTask());
        log("Future.state: " + future.state());

        // 3초간 대기 (작업이 3초 정도 진행된 후 취소 시도)
        sleep(3000);

        // 작업 취소 시도
        // mayInterruptIfRunning 값에 따라 실행 중 인터럽트 여부 결정
        log("future.cancel(" + mayInterruptIfRunning + ") 호출");
        boolean cancelResult = future.cancel(mayInterruptIfRunning);
        log("cancel(" + mayInterruptIfRunning + ") result: " + cancelResult);

        // 취소 후 future.get() 호출 시도
        // 작업이 이미 취소되었다면 CancellationException 발생
        try {
            log("Future result: " + future.get());
        } catch (CancellationException e) {
            log("Future는 이미 취소 되었습니다."); // 취소된 경우 이 메시지 출력
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        es.close();
    }

    /**
     * - 중간에 인터럽트가 발생하면 "인터럽트 발생" 로그를 남기고 "Interrupted"를 반환합니다.
     * - 정상적으로 끝나면 "Completed"를 반환합니다.
     */
    static class MyTask implements Callable<String> {
        @Override
        public String call() {
            try {
                for (int i = 0; i < 10; i++) {
                    log("작업 중: " + i);
                    Thread.sleep(1000); // 1초 대기
                }
            } catch (InterruptedException e) {
                log("인터럽트 발생"); // 인터럽트가 발생하면 여기로 진입
                return "Interrupted";
            }
            return "Completed"; // 정상 완료 시
        }
    }
}