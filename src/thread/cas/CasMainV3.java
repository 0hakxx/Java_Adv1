package thread.cas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

/**
 * 여러 스레드가 동시에 AtomicInteger 값을 CAS(Compare-And-Set)로 증가시키는 멀티스레드 예제
 * - AtomicInteger의 compareAndSet을 이용해 동시성 환경에서도 안전하게 값을 증가시킴
 */
public class CasMainV3 {

    private static final int THREAD_COUNT = 2; // 실행할 스레드 수

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0); // CAS 연산 대상 변수
        System.out.println("start value = " + atomicInteger.get());

        // 각 스레드가 실행할 작업 정의: atomicInteger 값을 1 증가
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                incrementAndGet(atomicInteger);
            }
        };

        // 스레드 리스트 생성 및 실행
        List<Thread> threads = new ArrayList<>();
        for (int i = 1; i <= THREAD_COUNT; i++) {
            Thread thread = new Thread(runnable);
            threads.add(thread);
            thread.start();
        }

        // 모든 스레드가 종료될 때까지 대기
        for (Thread thread : threads) {
            thread.join();
        }

        // 최종 결과 출력
        int result = atomicInteger.get();
        System.out.println(atomicInteger.getClass().getSimpleName() + " resultValue: " + result);
    }

    /**
     * AtomicInteger의 값을 1 증가시키고, 증가된 값을 반환하는 메서드
     * - compareAndSet을 이용해 직접 CAS(Compare-And-Swap) 방식으로 구현
     * - 여러 스레드가 동시에 접근해도 안전하게 동작
     */
    private static int incrementAndGet(AtomicInteger atomicInteger) {
        int getValue;     // 현재 값을 저장할 변수
        boolean result;   // CAS 연산 성공 여부

        do {
            getValue = atomicInteger.get(); // 현재 값 읽기
            sleep(100); // 스레드 동시 실행(충돌)을 유도하기 위해 잠시 대기
            log("getValue: " + getValue);

            // compareAndSet(예상값, 새값): 현재 값이 예상값(getValue)이면 새값(getValue+1)으로 변경
            // 여러 스레드가 동시에 접근하여 값을 바꾸는 과정에서 변경됨을 방지하기 위해 사용
            // 성공하면 true, 실패하면 false 반환
            result = atomicInteger.compareAndSet(getValue, getValue + 1);
            log("result: " + result);

            // CAS가 실패했다면(다른 스레드가 값을 바꿨다면) 다시 시도 (반복)
        } while (!result);

        // CAS에 성공하면 반복문을 빠져나와 증가된 값을 반환
        return getValue + 1;
    }
}
