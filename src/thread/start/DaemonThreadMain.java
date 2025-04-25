package thread.start;

public class DaemonThreadMain {

    public static void main(String[] args) {

        System.out.println(Thread.currentThread().getName() + ": main() start");
     
        DaemonThread daemonThread = new DaemonThread();
        daemonThread.setDaemon(true); // 데몬 스레드로 설정, 이 설정은 start() 하기 전에 설정해야함.

        // 데몬 스레드를 시작.
        // start() 메서드를 호출하면 새로운 스레드가 생성되고, 해당 스레드의 run() 메서드가 실행
        daemonThread.start();

        // 메인 스레드의 종료를 알리는 메시지를 콘솔에 출력
		// 메인 스레드가 종료되면 데몬 스레드는 강제적으로 종료
        System.out.println(Thread.currentThread().getName() + ": main() end");
   
    }

    static class DaemonThread extends Thread {

        @Override
        public void run() {
            // 데몬 스레드가 시작되었음을 알리는 메시지를 콘솔에 출력합니다.
            System.out.println(Thread.currentThread().getName() + ": run()");
            try {
                Thread.sleep(10000); // 10초간 실행
            } catch (InterruptedException e) {
                // 예외처리
                throw new RuntimeException(e);
            }

            // 데몬 스레드 이므로, 메인 스레드가 먼저 종료되어 이 메시지는 출력되지 않는다.
            System.out.println(Thread.currentThread().getName() + ": run() end");
   
        }
    }
}