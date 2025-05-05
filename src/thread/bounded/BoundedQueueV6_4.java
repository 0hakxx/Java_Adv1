package thread.bounded;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static util.MyLogger.log;


public class BoundedQueueV6_4 implements BoundedQueue {

    private BlockingQueue<String> queue;

    public BoundedQueueV6_4(int max) {
        // ArrayBlockingQueue는 생성 시 용량을 지정해야 하며, 고정 크기 큐입니다.
        this.queue = new ArrayBlockingQueue<>(max);
    }

    @Override
    public void put(String data) {
        // add()는 큐가 가득 찬 경우 예외(IllegalStateException)를 던집니다.
        // 예: 큐가 이미 최대 용량에 도달한 경우
        queue.add(data); // java.lang.IllegalStateException: Queue full
    }

    @Override
    public String take() {
        // remove()는 큐가 비어 있을 때 예외(NoSuchElementException)를 던집니다.
        // 예: 큐에 아무 데이터도 없는 경우
        return queue.remove(); // java.util.NoSuchElementException
    }

    @Override
    public String toString() {
        // ArrayBlockingQueue의 toString()을 그대로 사용
        return queue.toString();
    }
}
