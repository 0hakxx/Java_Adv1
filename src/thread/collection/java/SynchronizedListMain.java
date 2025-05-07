package thread.collection.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * - ArrayList는 기본적으로 스레드 안전하지 않으나,
 *   Collections.synchronizedList로 감싸면 내부적으로 동기화(synchronized) 처리됨
 */
public class SynchronizedListMain {

    public static void main(String[] args) {
        // Collections.synchronizedList는 모든 메서드에 대해 synchronized 처리를 해줌
        List<String> list = Collections.synchronizedList(new ArrayList<>());

        // 리스트에 데이터 추가
        list.add("data1");
        list.add("data2");
        list.add("data3");


        System.out.println(list.getClass());

        System.out.println("list = " + list);
    }
}
