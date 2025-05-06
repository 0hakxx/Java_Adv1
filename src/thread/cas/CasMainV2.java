package thread.cas;

import java.util.concurrent.atomic.AtomicInteger;

import static util.MyLogger.log;

/*
자바에서 제공하는 incrementAndGet 메서드의 기능을 직접 코드로 구현한 예제
AtomicInteger의 compareAndSet을 이용해 동시성 환경에서 안전하게 값을 1씩 증가시키는 과정
*/

public class CasMainV2 {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        System.out.println("start value = " + atomicInteger.get());

        // incrementAndGet 메서드를 호출하여 값을 1 증가시킴
        int resultValue1 = incrementAndGet(atomicInteger);
        System.out.println("resultValue1 = " + resultValue1);

        // 다시 한 번 값을 1 증가시킴
        int resultValue2 = incrementAndGet(atomicInteger);
        System.out.println("resultValue2 = " + resultValue2);
    }

    /**
     * AtomicInteger의 값을 1 증가시키고, 증가된 값을 반환하는 메서드
     * compareAndSet을 이용해 직접 CAS(Compare-And-Swap) 방식으로 구현
     * 여러 스레드가 동시에 접근해도 안전하게 동작
     */
    private static int incrementAndGet(AtomicInteger atomicInteger) {
        int getValue;   // 현재 값을 저장할 변수
        boolean result; // CAS 연산 성공 여부

        do {
            getValue = atomicInteger.get(); // 현재 값 읽기
            log("getValue: " + getValue);   // 현재 값 로그 출력

            // compareAndSet(예상값, 새값):
            // 현재 값이 예상값(getValue)이면 새값(getValue+1)으로 변경
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
