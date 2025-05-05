package thread.bounded;

import java.util.ArrayDeque;
import java.util.Queue;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;


/*
이 코드는 생산자가 먼저 실행, 소비자 먼저 실행해도 큐에 데이터 추출/추가 하는 동작에는 이상이 없으나
소비자 먼저 실행할 경우 비효율적인 문제가 발생함.
* */
public class BoundedQueueV3 implements BoundedQueue {

    private final Queue<String> queue = new ArrayDeque<>();
    private final int max; // 큐의 최대 크기

    public BoundedQueueV3(int max) {
        this.max = max;
    }

    /**
     * 큐에 데이터를 추가 (생산자)
     * - 큐가 가득 차면 wait()으로 대기 (락 해제)
     * - 데이터 추가 후 notify()로 대기 중인 스레드 깨움
     */
    @Override
    public synchronized void put(String data) {

        while (queue.size() == max) {
            log("[put] 큐가 가득 참, 생산자 대기");
            try {
                // [V2와의 차이점] V2는 sleep() 사용, V3는 wait() 사용
                // [V2와의 중요 차이점] : wait()은 락을 해제하고, 스레드(생산자)를 WAITING 상태로 만든다.
                // 다른 스레드(소비자)가 take()를 통해 큐에서 데이터를 빼고 notify()를 호출하면 다시 깨어남
                wait();
                log("[put] 생산자 깨어남");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        queue.offer(data); // 큐에 데이터 추가
        log("[put] 생산자 데이터 저장, notify() 호출");
        notify(); // 대기중인 스레드에게 알려주기 위해 notify()를 호출하여 다시 깨어남, WAIT -> BLOCKED 상태로 변경
    }

    @Override
    public synchronized String take() {

        while (queue.isEmpty()) {
            log("[take] 큐에 데이터가 없음, 소비자 대기");
            try {
                // [V2와의 차이점] V2는 sleep() 사용, V3는 wait() 사용
                // [V2와의 중요 차이점] : wait()은 락을 해제하고, 스레드(소비자)를 WAITING 상태로 만든다.
                // 다른 스레드(생산자)가 put()을 통해 데이터를 추가하고 notify()를 호출하면 다시 깨어남
                wait();
                log("[take] 소비자 깨어남");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String data = queue.poll(); // 큐에서 데이터 꺼냄
        log("[take] 소비자 데이터 획득, notify() 호출");
        notify(); // 대기중인 스레드에게 알려주기 위해 notify()를 호출하여 다시 깨어남, WAIT -> BLOCKED 상태로 변경
        return data;
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
