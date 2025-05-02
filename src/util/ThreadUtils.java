package util;

import static util.MyLogger.log;

// 유틸리티 메서드를 제공하는 추상 클래스, 추상 클래스이므로 직접 new ThreadUtils() 불가.
public abstract class ThreadUtils {
    // - 호출 시 지정한 시간(millis)만큼 현재 스레드를 일시 정지시킴
    // - Checked Exception(InterruptedException)을 RuntimeException으로 변환하여
    //   호출자가 예외를 직접 처리하지 않아도 되도록 함
    public static void sleep(long millis) {
        try {
            // 지정한 시간(ms) 동안 현재 스레드를 일시 정지
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // 스레드가 sleep 중 인터럽트 될 경우 예외 발생, 로그로 인터럽트 상황을 기록
            log("인터럽트 발생, " + e.getMessage());
            // Checked Exception(InterruptedException)을 RuntimeException으로 감싸서 던짐
            // -> 호출자는 try-catch 없이 간편하게 사용할 수 있음
            throw new RuntimeException(e);
        }
    }
}
