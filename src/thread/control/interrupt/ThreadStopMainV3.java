package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV3 {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(100); // 0.1초 후에 작업 중단 지시
        log("작업 중단 지시 thread.interrupt()");
        thread.interrupt();
        log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted());
    }

    static class MyTask implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) { //[핵심] while 루프에서 인터럽트 상태를 직접 확인하여 종료
                log("작업 중");
            }
            // 인터럽트 상태가 true가 되면 반복문 탈출
            log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted());

            try {
                log("자원 정리");
                Thread.sleep(1000);
                log("자원 종료");
            } catch (InterruptedException e) {
                // 자원 정리 중 인터럽트가 다시 발생하면 예외 처리
                log("자원 정리 실패 - 자원 정리 중 인터럽트 발생");
                log("work 스레드 인터럽트 상태3 = " + Thread.currentThread().isInterrupted());
            }
            log("작업 종료");
        }
    }
}

/*
==================================================================================
[V2와 V3의 차이점 상세 설명]
==================================================================================
종료 타이밍 및 반응성
- V2: sleep() 등 블로킹 메서드 사용 → 인터럽트 시 즉시 예외 발생, 즉각적인 종료 가능
- V3: 블로킹 메서드 없이 반복문만 사용 → 인터럽트 신호가 올 때까지 반복문 계속 실행
      - 인터럽트 신호가 오면 다음 반복에서 바로 종료(거의 즉각적)
      - 단, 반복문이 복잡하거나 오래 걸리면 반응이 느릴 수 있음
[핵심 요약]
- V2: 블로킹 메서드(sleep 등)에서 인터럽트 시 예외로 즉시 종료
- V3: 블로킹 없이 반복문에서 인터럽트 상태를 직접 체크하여 종료(예외 없음)

[해당 코드, V3 코드의 심각한 문제점]
==================================================================================

1. 인터럽트 상태가 true인 채로 자원 정리 진입
---------------------------------------------   
- while 루프는 인터럽트 상태가 true가 되면 탈출함.
- 하지만, 반복문을 빠져나온 시점에서 스레드의 인터럽트 상태는 여전히 true임.
- 이 상태에서 자원 정리 단계로 진입하여 Thread.sleep(1000)을 호출하면,
  이미 인터럽트 상태가 true이므로 sleep()은 즉시 InterruptedException을 던짐.
- 그 결과, 실제 자원 정리 코드가 실행되지 못하고 catch 블록으로 바로 이동하여
  "자원 정리 실패 - 자원 정리 중 인터럽트 발생" 로그가 남게 됨.

2. 자원 정리가 반드시 필요한 경우 치명적
----------------------------------------
- 자원 정리(파일 닫기, DB 연결 해제 등)가 반드시 필요한 경우,
  인터럽트 플래그가 true인 채로 진입하면 정리가 제대로 되지 않을 수 있음.
- 이로 인해 리소스 누수, 데이터 손상 등 심각한 문제가 발생할 수 있음.

*/


