package thread.collection.simple.list;

import java.util.Vector;

import static util.MyLogger.log;

/**
 * 멀티스레드 환경에서 다양한 리스트 구현체의 스레드 안전성을 테스트하는 예제
 * - 동기화되지 않은 리스트와 동기화된 리스트 간의 차이점을 보여줌.
 */
public class SimpleListMainV2 {

    public static void main(String[] args) throws InterruptedException {

        // test(new BasicList()); // BasicList(동기화되지 않은 기본 리스트)로 테스트 실행

        // test(new SyncList()); // 동기화된 리스트로 테스트할 경우
        test(new SyncProxyList(new BasicList())); // 동기화 프록시를 사용한 리스트로 테스트할 경우

    }

    private static void test(SimpleList list) throws InterruptedException {
        log(list.getClass().getSimpleName());

        // A를 리스트에 저장하는 Runnable 객체 정의
        Runnable addA = new Runnable() {
            @Override
            public void run() {
                list.add("A"); // 리스트에 "A" 추가
                log("Thread-1: list.add(A)"); // 작업 완료 로그
            }
        };

        // B를 리스트에 저장하는 Runnable 객체 정의
        Runnable addB = new Runnable() {
            @Override
            public void run() {
                list.add("B"); // 리스트에 "B" 추가
                log("Thread-2: list.add(B)"); // 작업 완료 로그
            }
        };


        Thread thread1 = new Thread(addA, "Thread-1");
        Thread thread2 = new Thread(addB, "Thread-2");


        thread1.start();
        thread2.start();

        // 두 스레드가 모두 종료될 때까지 메인 스레드 대기
        thread1.join();
        thread2.join();

        // 모든 작업 완료 후 최종 리스트 상태 출력
        // BasicList를 사용할 경우 동기화 문제로 데이터 손실 가능성 있음
        log(list);
    }
}
