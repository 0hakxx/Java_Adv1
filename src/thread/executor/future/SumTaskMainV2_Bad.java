package thread.executor.future;

import java.util.concurrent.*;

import static util.MyLogger.log;

/*
이 코드의 문제점은 병렬 처리 효과 없음
스레드풀을 2개로 만들었지만, 첫 번째 작업이 끝나야 두 번째 작업을 제출함.
두 작업을 동시에 실행할 수 있는데, 순차적으로 실행되어 총 4초가 걸림. 아래처럼 변경해야 병렬처리의 효과를 얻음.

<기존>
Future<Integer> future1 = es.submit(task1);
Integer sum1 = future1.get();
Future<Integer> future2 = es.submit(task2);
Integer sum2 = future2.get();
<변경>
Future<Integer> future1 = es.submit(task1);
Future<Integer> future2 = es.submit(task2);
Integer sum1 = future1.get();
Integer sum2 = future2.get();



* */


public class SumTaskMainV2_Bad {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        SumTask task1 = new SumTask(1, 50);

        SumTask task2 = new SumTask(51, 100);


        ExecutorService es = Executors.newFixedThreadPool(2);


        Future<Integer> future1 = es.submit(task1);
        // future1.get() 호출로 결과를 기다림 (블로킹, 약 2초)
        Integer sum1 = future1.get(); // 2초


        Future<Integer> future2 = es.submit(task2);
        // future2.get() 호출로 결과를 기다림 (블로킹, 약 2초)
        Integer sum2 = future2.get(); // 2초

        // 각 작업의 결과 출력
        log("task1.result=" + sum1);
        log("task2.result=" + sum2);

        // 두 작업의 결과를 합산하여 출력
        int sumAll = sum1 + sum2;
        log("task1 + task2 = " + sumAll);
        log("End");

        // ExecutorService 종료 (Java 19+에서 close() 지원)
        es.close();
    }

    /**
     * SumTask 클래스는 startValue부터 endValue까지의 합을 계산하는 Callable 구현체
     */
    static class SumTask implements Callable<Integer> {
        int startValue;
        int endValue;

        // 생성자: 시작값과 끝값을 받아 필드에 저장
        public SumTask(int startValue, int endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public Integer call() throws InterruptedException {
            log("작업 시작");
            Thread.sleep(2000); // 2초간 대기(작업 지연 시뮬레이션)
            int sum = 0;
            // startValue부터 endValue까지 합 계산
            for (int i = startValue; i <= endValue; i++) {
                sum += i;
            }
            log("작업 완료 result=" + sum);
            return sum; // 합계 반환
        }
    }
}
