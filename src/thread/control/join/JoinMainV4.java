package thread.control.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV4 {

    public static void main(String[] args) throws InterruptedException {
        log("Start");
        SumTask task1 = new SumTask(1, 50);
        Thread thread1 = new Thread(task1, "thread-1");

        thread1.start();

        // [V4의 핵심: 제한시간(timeout)을 두고 join()]
        // main 스레드는 thread1이 종료될 때까지 최대 1초 동안만 대기함
        // - thread1이 1초 내에 끝나면 바로 다음 코드로 진행
        // - thread1이 1초 내에 끝나지 않으면 1초가 지난 후 바로 다음 코드로 진행(아직 작업 중일 수 있음)
        // - 즉, "최대 대기 시간"만 보장하며, 반드시 스레드가 끝난 후 결과를 읽는 것은 아님
        log("join(1000) - main 스레드가 thread1 종료까지 1초 대기");
        thread1.join(1000); // 최대 1초 대기
        log("main 스레드 대기 완료");

        // join(1000) 이후 thread1이 아직 작업 중이라면 result는 0(초기값)일 수 있음
        // 작업이 끝났다면 result에 계산 결과가 들어감
        log("task1.result = " + task1.result);
        log("End");
    }

    static class SumTask implements Runnable {

        int startValue;
        int endValue;
        int result = 0;

        public SumTask(int startValue, int endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public void run() {
            log("작업 시작");
            sleep(2000); // 2초 대기 후 합계 계산
            int sum = 0;
            for (int i = startValue; i <= endValue; i++) {
                sum += i;
            }
            result = sum;
            log("작업 완료 result = " + result);
        }
    }
}

/*
==================================================================================
V3 (JoinMainV3)
-------------------
- main()에서 스레드 시작 후 join() 호출
- main 스레드는 스레드가 종료될 때까지 정확하게 대기
- join() 이후 result를 읽으면 반드시 작업이 끝난 후의 값이 보장됨
- "정확한 동기화" → 올바른 멀티스레드 프로그래밍 방식

V4 (JoinMainV4, 현재 코드)
-----------------------------
- main()에서 스레드 시작 후 join(1000) 호출(최대 1초만 대기)
- thread1이 1초 내에 끝나면 result는 계산된 값, 끝나지 않으면 result는 0(초기값)일 수 있음
- "부분 동기화" 또는 "타임아웃 동기화"
    - 스레드가 오래 걸릴 경우, main 스레드는 더 이상 기다리지 않고 넘어감
    - 반드시 작업이 끝난 후의 결과를 보장하지 않음
- 실무에서는 "최대 대기 시간"이 필요한 경우에 사용(예: UI 응답성, 타임아웃 정책 등)

==================================================================================
- V3: join()으로 정확히 스레드 종료 대기 → 항상 정확한 결과 보장 (권장 방식)
- V4: join(timeout)으로 최대 대기 시간만 보장 → 작업이 끝나지 않아도 넘어갈 수 있음, 결과가 0일 수도 있음

*/
