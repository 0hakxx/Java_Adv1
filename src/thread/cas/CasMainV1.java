package thread.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS(Compare-And-Swap) 연산을 이용한 원자적 값 변경 예제
 * - 멀티스레드 환경에서 동기화 없이 안전하게 값을 변경하는 방법
 */
public class CasMainV1 {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        System.out.println("start value = " + atomicInteger.get());

        // compareAndSet(예상값, 새값): CAS 연산의 핵심 메서드
        // - 현재 값과 예상값을 비교해서 같으면 새 값으로 변경하고 true 반환
        // - 다르면 값을 변경하지 않고 false 반환
        // 첫 번째 시도: 현재 값 0 == 예상값 0 → 성공되어 1로 변경
        boolean result1 = atomicInteger.compareAndSet(0, 1);
        System.out.println("result1 = " + result1 + ", value = " + atomicInteger.get()); // true, 1

        // 두 번째 시도: 현재 값 1 != 예상값 0 → 실패
        // 첫 번째 연산으로 값이 이미 1이 되었기 때문에, 
        // 예상값 0과 일치하지 않아 변경 실패하고 값을 변경하지 않는다.
        boolean result2 = atomicInteger.compareAndSet(0, 1);
        System.out.println("result2 = " + result2 + ", value = " + atomicInteger.get()); // false, 1 (변경 안됨)
    }
}
