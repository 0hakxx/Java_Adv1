package thread.control.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV2 {

    public static void main(String[] args) {
        log("Start");
        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);
        Thread thread1 = new Thread(task1, "thread-1");
        Thread thread2 = new Thread(task2, "thread-2");

        thread1.start();
        thread2.start();

        // [V2의 핵심 차이점]
        // main 스레드가 sleep(3000)으로 임의의 시간(3초) 동안 대기함
        // - V1에서는 sleep 없이 즉시 결과를 출력해서 항상 0이 출력될 가능성이 높았음
        // - V2에서는 3초 대기(스레드는 2초 sleep 후 계산)로, 대부분의 경우 두 스레드의 작업이 끝난 후 결과를 읽게 됨
        // - 하지만, 작업 시간이 3초보다 길어지거나, 시스템 상황에 따라 여전히 결과가 0이 출력될 수도 있음
        // - 즉, 정확한 동기화가 아닌 "타이밍을 맞추는 임시방편"이며 타이밍을 맞추기 어렵다.
        log("main 스레드 sleep()");
        sleep(3000);
        log("main 스레드 깨어남");

        // 대부분의 경우, 아래의 result는 계산이 끝난 값이 출력됨
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
            sleep(2000); // 2초 대기 후 계산
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
==================================================================================
- V2는 스레드 작업 완료를 "정확하게" 기다리는 것이 아니라,
  "충분히 기다렸으니 아마 끝났겠지"라는 식의 임시방편(타이밍 맞추기)임
- 실제로 스레드 동기화가 필요한 경우에는 thread.join()을 사용해야 함
- sleep을 통한 대기는 신뢰성이 떨어지며, 작업 시간이 변동될 경우 동기화 문제가 다시 발생할 수 있음
*/
