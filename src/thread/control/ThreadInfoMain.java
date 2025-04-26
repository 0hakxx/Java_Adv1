package thread.control;

import thread.start.HelloRunnable;

import static util.MyLogger.log;

public class ThreadInfoMain {

    public static void main(String[] args) {
        // 현재 실행 중인 스레드(메인 스레드) 정보를 가져온다.
        Thread mainThread = Thread.currentThread();

        // 메인 스레드의 정보 출력
        log("mainThread = " + mainThread); // Thread 객체의 toString() 결과 (이름, 우선순위 등)
        log("mainThread.threadId()=" + mainThread.threadId()); // 스레드의 고유 ID
        log("mainThread.getName()=" + mainThread.getName()); // 스레드 이름 (보통 "main")
        log("mainThread.getPriority()=" + mainThread.getPriority()); // 스레드 우선순위 (기본: 5)
        log("mainThread.getThreadGroup()=" + mainThread.getThreadGroup()); // 스레드가 속한 그룹 정보
        log("mainThread.getState()=" + mainThread.getState()); // 스레드의 현재 상태 (RUNNABLE 등)

        // 새로운 스레드(myThread) 생성: HelloRunnable을 실행하는 스레드, 이름은 "myThread"
        Thread myThread = new Thread(new HelloRunnable(), "myThread");

        // myThread의 정보 출력
        log("myThread = " + myThread); // Thread 객체의 toString() 결과
        log("myThread.threadId()=" + myThread.threadId()); // myThread의 고유 ID
        log("myThread.getName()=" + myThread.getName()); // myThread의 이름 ("myThread")
        log("myThread.getPriority()=" + myThread.getPriority()); // myThread의 우선순위 (기본: 5)
        log("myThread.getThreadGroup()=" + myThread.getThreadGroup()); // myThread가 속한 그룹 정보
        log("myThread.getState()=" + myThread.getState()); // (아직 start() 호출 전이므로 상태는 NEW) myThread의 현재 상태 (NEW)

        // ※ 참고: myThread.start()를 호출하지 않았으므로 실제로 실행되지는 않는다.
        // 만약 myThread.start()를 호출하면 HelloRunnable의 run()이 실행되고, 상태가 RUNNABLE 등으로 바뀐다.
    }
}
