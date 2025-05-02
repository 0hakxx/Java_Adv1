package thread.control.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV3 {

    public static void main(String[] args) throws InterruptedException {
        log("Start");
        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);
        Thread thread1 = new Thread(task1, "thread-1");
        Thread thread2 = new Thread(task2, "thread-2");

        thread1.start();
        thread2.start();

        // [V3의 핵심: 정확한 스레드 동기화]
        // main 스레드는 아래 join() 메서드를 통해 thread1, thread2가 종료될 때까지 대기함
        // - join()은 해당 스레드가 종료될 때까지 호출한 스레드(main)를 블로킹함
        // - 따라서 아래의 result 값은 반드시 두 작업이 끝난 후의 값(정확한 결과)이 보장됨
        log("join() - main 스레드가 thread1, thread2 종료까지 대기");
        thread1.join(); // thread1 종료까지 대기
        thread2.join(); // thread2 종료까지 대기
        log("main 스레드 대기 완료");

        // join() 이후이므로, 반드시 result 값이 계산 완료되어 있음
        log("task1.result = " + task1.result);
        log("task2.result = " + task2.result);

        int sumAll = task1.result + task2.result;
        log("task1 + task2 = "+ sumAll);
        log("End");
    }

    static class SumTask implements Runnable {

        int startValue;
        int endValue;
        int result = 0;

        public SumTask(int startValue, int endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public void run() {
            log("작업 시작");
            sleep(2000); // 2초 대기 후 합계 계산
            int sum = 0;
            for (int i = startValue; i <= endValue; i++) {
                sum += i;
            }
            result = sum;
            log("작업 완료 result = " + result);
        }
    }
}

/*
==================================================================================
[핵심 요약]
- V1: 동기화 없음(즉시 결과 출력) → 잘못된 결과(0) 가능성 높음
- V2: sleep()으로 임시 대기 → 대부분 맞지만, 100% 신뢰 불가
- V3: join()으로 정확히 스레드 종료 대기 → 항상 정확한 결과 보장 (권장 방식)

그러나, join()의 단점은 스레드가 종료될 때까지 무기한으로 기다려야한다는 점이다. 중간에 기다리는 것을 캔슬할 수 없다.
*/
