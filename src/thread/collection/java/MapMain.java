package thread.collection.java;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * ConcurrentHashMap과 ConcurrentSkipListMap을 사용하는 스레드 안전한 Map 예제
 * - 둘 다 java.util.concurrent 패키지에서 제공하는 멀티스레드 환경에서 안전하게 사용할 수 있는 Map 구현체
 * - ConcurrentHashMap: 키의 순서를 보장하지 않는 해시 기반 Map
 * - ConcurrentSkipListMap: 내부적으로 Skip List 구조를 사용하여 키의 정렬(오름차순)을 보장하는 Map
 */
public class MapMain {

    public static void main(String[] args) {
        // ConcurrentHashMap 생성 (스레드 안전, 키 순서 없음)
        Map<Integer, String> map1 = new ConcurrentHashMap<>();
        map1.put(3, "data3");
        map1.put(2, "data2");
        map1.put(1, "data1");
        // 출력: 키의 순서는 보장되지 않음 (해시 기반)
        System.out.println("map1 = " + map1);

        // ConcurrentSkipListMap 생성 (스레드 안전, 키 정렬 보장)
        Map<Integer, String> map2 = new ConcurrentSkipListMap<>();
        map2.put(2, "data2");
        map2.put(3, "data3");
        map2.put(1, "data1");
        // 출력: 항상 키의 오름차순으로 정렬됨
        System.out.println("map2 = " + map2);
    }
}
