package thread.control.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV1 {

    public static void main(String[] args) {
        log("Start");
        SumTask task1 = new SumTask(1, 50); // 1~50까지 합을 구하는 작업(task1)
        SumTask task2 = new SumTask(51, 100); // 51~100까지 합을 구하는 작업(task2)
        // 각각의 작업을 실행할 스레드 생성
        Thread thread1 = new Thread(task1, "thread-1");
        Thread thread2 = new Thread(task2, "thread-2");

        // 두 스레드 실행 시작 (비동기적으로 동작)
        thread1.start();
        thread2.start();

        // (중요) 아래 코드는 스레드의 작업이 끝나기 전에 실행될 수 있음
        // 즉, 아직 result 값이 계산되기 전일 수 있음 (동기화 X)
        log("task1.result = " + task1.result); // 0이 출력될 가능성 매우 높음
        log("task2.result = " + task2.result); // 0이 출력될 가능성 매우 높음
        // 두 작업의 합계도 마찬가지로 0 + 0 = 0이 될 수 있음
        int sumAll = task1.result + task2.result;
        log("task1 + task2 = "+ sumAll);
        log("End");
    }

    static class SumTask implements Runnable {

        int startValue;
        int endValue;
        int result = 0; // 합계 결과값
        public SumTask(int startValue, int endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }
        @Override
        public void run() {
            log("작업 시작");
            sleep(2000); // 2초간 대기, 일부러 메인 스레드가 먼저 끝나는 것을 유도하기 위해 설정
            int sum = 0;
            for (int i = startValue; i <= endValue; i++) {
                sum += i;
            }
            result = sum; // 계산 결과 저장
            log("작업 완료 result = " + result);
        }
    }
}

/*
==================================================================================
문제점
---------
- 메인 스레드는 다른 스레드의 실행만 시키고 기다려주지 않는다.
- 따라서 result 값이 0(초기값)으로 출력될 가능성이 매우 높음
- 두 작업의 합(sumAll)도 0 + 0 = 0이 될 수 있음

스레드 동기화 필요성
----------------------
- 만약 스레드의 작업이 끝난 후 result 값을 정확히 읽고 싶다면,
  main()에서 thread1.join(), thread2.join()을 호출하여
  두 스레드의 작업이 끝날 때까지 대기해야 함

*/
