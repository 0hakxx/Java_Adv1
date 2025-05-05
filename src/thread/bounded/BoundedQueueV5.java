package thread.bounded;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;

/**
 * BoundedQueueV5: Lock과 Condition을 이용한 유한 큐 (생산자/소비자 조건 분리)
 * - V3와 달리, 생산자와 소비자 각각의 대기 조건을 별도의 Condition 객체로 분리하여
 *   더 효율적이고 명확한 동기화 구현이 가능함
 */
public class BoundedQueueV5 implements BoundedQueue {

    // [V3와의 차이점] V3는 synchronized/wait/notify, V5는 명시적 Lock/Condition 사용
    private final Lock lock = new ReentrantLock();

    // [V3와의 차이점] V3는 단일 wait/notify, V5는 생산자/소비자 각각의 Condition 사용
    private final Condition producerCond = lock.newCondition(); // 생산자 대기용 Condition
    private final Condition consumerCond = lock.newCondition(); // 소비자 대기용 Condition

    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;

    public BoundedQueueV5(int max) {
        this.max = max;
    }

    /**
     * 큐에 데이터를 추가 (생산자)
     * - 큐가 가득 차면 producerCond.await()로 대기 (락 해제)
     * - 데이터 추가 후 consumerCond.signal()로 소비자만 깨움
     */
    @Override
    public void put(String data) {
        lock.lock(); // [V3는 synchronized, V5는 명시적 lock.lock()]
        try {
            while (queue.size() == max) {
                log("[put] 큐가 가득 참, 생산자 대기");
                try {
                    producerCond.await();// 생산자만 producerCond에서 대기, 락 해제
                    log("[put] 생산자 깨어남");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            queue.offer(data);
            log("[put] 생산자 데이터 저장, consumerCond.signal() 호출");
            consumerCond.signal(); // V5는 consumerCond.signal()로 소비자만 깨움, 생산자 불필요한 깨어남 방지
        } finally {
            lock.unlock(); // [V3는 synchronized 블록 자동 해제, V5는 명시적으로 unlock()]
        }
    }

    /**
     * 큐에서 데이터를 꺼냄 (소비자)
     * - 큐가 비어있으면 consumerCond.await()로 대기 (락 해제)
     * - 데이터 꺼낸 후 producerCond.signal()로 생산자만 깨움
     */
    @Override
    public String take() {
        lock.lock(); // [V3는 synchronized, V5는 명시적 lock.lock()]
        try {
            while (queue.isEmpty()) {
                log("[take] 큐에 데이터가 없음, 소비자 대기");
                try {
                    consumerCond.await(); // 소비자만 consumerCond에서 대기, 락 해제
                    log("[take] 소비자 깨어남");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            String data = queue.poll();
            log("[take] 소비자 데이터 획득, producerCond.signal() 호출");
            producerCond.signal(); //V5는 producerCond.signal()로 생산자만 깨움, 소비자 불필요한 깨어남 방지
            return data;
        } finally {
            lock.unlock(); // [V3는 synchronized 블록 자동 해제, V5는 명시적으로 unlock()]
        }
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
