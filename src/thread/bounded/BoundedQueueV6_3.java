package thread.bounded;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static util.MyLogger.log;

public class BoundedQueueV6_3 implements BoundedQueue {


    private BlockingQueue<String> queue;

    public BoundedQueueV6_3(int max) {
        this.queue = new ArrayBlockingQueue<>(max);
    }

    @Override
    public void put(String data) {
        try {
            // 대기 시간(1ns) 지정, 즉시 공간이 없으면 바로 false 반환
            boolean result = queue.offer(data, 1, TimeUnit.NANOSECONDS);
            log("저장 시도 결과 = " + result); // 성공(true) 또는 실패(false) 로그 출력
        } catch (InterruptedException e) {
            throw new RuntimeException(e); // 대기 중 인터럽트 발생 시 런타임 예외로 변환
        }
    }

    @Override
    public String take() {
        try {
            // 대기 시간(2초) 지정, 그 안에 데이터가 오면 반환, 아니면 null 반환
            return queue.poll(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
