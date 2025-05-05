package thread.bounded;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BoundedQueueV6_1 implements BoundedQueue {

    // [V5와의 차이점] V5는 Lock/Condition/Queue 직접 구현, V6는 BlockingQueue로 동기화 및 버퍼 관리 일임
    private BlockingQueue<String> queue;

    public BoundedQueueV6_1(int max) {
        // [V5와의 차이점] V5는 max 직접 관리, V6는 ArrayBlockingQueue 생성자로 max 전달
        this.queue = new ArrayBlockingQueue<>(max);
    }

    @Override
    public void put(String data) {
        // [V5와의 차이점] V5는 lock, while, await, signal 등 직접 구현
        // V6는 queue.put() 한 줄로 동기화 및 대기/신호 자동 처리
        try {
            queue.put(data); // 내부적으로 락/조건/대기/신호 모두 처리됨
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String take() {
        // [V5와의 차이점] V5는 lock, while, await, signal 등 직접 구현
        // V6는 queue.take() 한 줄로 동기화 및 대기/신호 자동 처리
        try {
            return queue.take(); // 내부적으로 락/조건/대기/신호 모두 처리됨
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
