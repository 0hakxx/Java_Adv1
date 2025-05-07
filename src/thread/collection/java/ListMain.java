package thread.collection.java;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList를 사용한 스레드 안전한 리스트 예제
 * - CopyOnWriteArrayList는 java.util.concurrent 패키지에서 제공하는
 *   스레드 안전한 리스트 구현체
 * - 쓰기(추가/수정/삭제) 연산 시마다 내부 배열을 복사하여 새로운 배열로 만듦
 * - 읽기(조회) 연산이 많고, 쓰기 연산이 적은 환경에서 매우 효율적임
 */
public class ListMain {

    public static void main(String[] args) {
        // CopyOnWriteArrayList 생성 (스레드 안전)
        List<Integer> list = new CopyOnWriteArrayList<>();

        // 리스트에 데이터 추가
        list.add(1);
        list.add(2);
        list.add(3);

        // 리스트의 현재 내용 출력
        System.out.println("list = " + list);
    }
}
