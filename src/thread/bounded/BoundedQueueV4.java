package thread.bounded;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;


/*
결과는 V3와 완전히 같다. 단순히 wait(), notify()를 Condition으로만 구현, 비효율 문제의 해결은 V5 코드에 구현한다.
* */
public class BoundedQueueV4 implements BoundedQueue {

    // [V3와의 차이점] V3는 synchronized/wait/notify, V4는 Lock/Condition 사용
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition(); // [V3는 없음], 대기 스레드 공간 생성

    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;

    public BoundedQueueV4(int max) {
        this.max = max;
    }


    @Override
    public void put(String data) {
        lock.lock(); // [V3는 synchronized, V4는 명시적 lock.lock()]
        try {
            while (queue.size() == max) {
                log("[put] 큐가 가득 참, 생산자 대기");
                try {
                    // [V3와의 차이점] V3는 wait(), V4는 condition.await()
                    // 둘 다 락을 해제하고 대기하지만, V4는 더 세밀한 조건 객체 사용 가능
                    condition.await();
                    log("[put] 생산자 깨어남");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            queue.offer(data);
            log("[put] 생산자 데이터 저장, signal() 호출");
            // [V3와의 차이점] V3는 notify(), V4는 condition.signal()
            // V4는 여러 Condition을 만들 수 있어 더 유연한 동기화 가능
            condition.signal();
        } finally {
            lock.unlock(); // [V3는 synchronized 블록 자동 해제, V4는 명시적으로 unlock()]
        }
    }

    @Override
    public String take() {
        lock.lock(); // [V3는 synchronized, V4는 명시적 lock.lock()]
        try {
            while (queue.isEmpty()) {
                log("[take] 큐에 데이터가 없음, 소비자 대기");
                try {
                    // [V3와의 차이점] V3는 wait(), V4는 condition.await()
                    condition.await();
                    log("[take] 소비자 깨어남");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            String data = queue.poll();
            log("[take] 소비자 데이터 획득, signal() 호출");
            // [V3와의 차이점] V3는 notify(), V4는 condition.signal()
            condition.signal();
            return data;
        } finally {
            lock.unlock(); // [V3는 synchronized 블록 자동 해제, V4는 명시적으로 unlock()]
        }

    }
    @Override
    public String toString() {
        return queue.toString();
    }
}
