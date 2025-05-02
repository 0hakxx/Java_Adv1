package thread.control;

public class CheckedExceptionMain {

    public static void main(String[] args) throws Exception {
        // main() 메서드에서 Exception을 직접 발생시킴
        // main() 메서드 선언부에 throws Exception이 있으므로 컴파일 에러 없이 예외를 던질 수 있음
        throw new Exception();
    }

    // Runnable을 구현한 내부 클래스
    static class CheckedRunnable implements Runnable {

        @Override
        public void run() /*throws Exception*/ { // (1) throws Exception 주석을 해제하면 컴파일 에러 발생
            //throw new Exception(); // (2) 이 줄의 주석을 해제하면 컴파일 에러 발생

            // 이유:
            // - Runnable 인터페이스의 run() 메서드는 throws 절이 없음(throws Exception 불가)
            // - 즉, run() 메서드에서 체크 예외(Checked Exception)를 직접 던질 수 없음
            // - 만약 주석을 해제하면 "Unhandled exception: java.lang.Exception" 컴파일 에러 발생

            // 해결 방법:
            // - run() 내부에서 체크 예외가 필요하면 try-catch로 직접 처리해야 함
            //   예시:
            //   try {
            //       throw new Exception();
            //   } catch (Exception e) {
            //       e.printStackTrace();
            //   }
        }
    }
}
