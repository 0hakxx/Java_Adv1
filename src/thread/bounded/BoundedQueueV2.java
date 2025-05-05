package thread.bounded;

import java.util.ArrayDeque;
import java.util.Queue;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;
/*
이 코드의 문제점
큐가 가득 찼을 때, sleep 을 사용하여 큐에 데이터가 빠져나갈 것을 기대하였으나,
생산자가 락을 가지고 있는 상태에서, 소비자가 큐에 데이터를 뺄 때까지 기다림.
따라서 큐는 계속 가득 차 있는 상태이므로 sleep()이 계속 지속되는 무한 대기 상태 문제에 직면하게 됨.
*/


public class BoundedQueueV2 implements BoundedQueue {

    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;

    public BoundedQueueV2(int max) {
        this.max = max;
    }

    @Override
    public synchronized void put(String data) {
        while (queue.size() == max) {
            log("[put] 큐가 가득 참, 생산자 대기");
            sleep(1000);  // 1초마다 깨어나서 다시 확인
        }
        queue.offer(data);
    }

    @Override
    public synchronized String take() {
        while (queue.isEmpty()) {
            log("[take] 큐에 데이터가 없음, 소비자 대기");
            sleep(1000); // 1초마다 깨어나서 다시 확인
        }
        return queue.poll();
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
