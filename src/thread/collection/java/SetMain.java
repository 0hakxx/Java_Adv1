package thread.collection.java;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * CopyOnWriteArraySet과 ConcurrentSkipListSet을 사용하는 스레드 안전한 Set 예제
 * - 둘 다 java.util.concurrent 패키지에서 제공하는 멀티스레드 환경에서 안전하게 사용할 수 있는 Set 구현체
 * - CopyOnWriteArraySet: 내부적으로 CopyOnWriteArrayList를 사용, 읽기 위주 환경에 적합
 * - ConcurrentSkipListSet: 내부적으로 Skip List 구조를 사용, 항상 정렬된 상태 유지
 */
public class SetMain {

    public static void main(String[] args) {
        // CopyOnWriteArraySet 생성 (스레드 안전, 읽기 위주 환경에 적합)
        Set<Integer> copySet = new CopyOnWriteArraySet<>();
        copySet.add(1);
        copySet.add(2);
        copySet.add(3);
        // 입력 순서와 상관없이 Set이므로 중복 없이 저장됨
        System.out.println("copySet = " + copySet);

        // ConcurrentSkipListSet 생성 (스레드 안전, 정렬된 상태 유지)
        Set<Integer> skipSet = new ConcurrentSkipListSet<>();
        skipSet.add(3);
        skipSet.add(2);
        skipSet.add(1);
        // 항상 오름차순(정렬) 상태로 저장됨
        System.out.println("skipSet = " + skipSet);
    }
}
