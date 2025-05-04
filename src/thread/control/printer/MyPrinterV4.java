package thread.control.printer;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.MyLogger.log;

public class MyPrinterV4 {

    public static void main(String[] args) {
        Printer printer = new Printer();
        Thread printerThread = new Thread(printer, "printer");
        printerThread.start();

        Scanner userInput = new Scanner(System.in);
        while (true) {
            log("프린터할 문서를 입력하세요. 종료 (q): ");
            String input = userInput.nextLine();
            if (input.equals("q")) {
                printerThread.interrupt();
                break;
            }
            printer.addJob(input);
        }
    }

    // 프린터 역할을 하는 Runnable 구현 클래스
    static class Printer implements Runnable {
        Queue<String> jobQueue = new ConcurrentLinkedQueue<>();

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                // [중요] 작업 큐가 비어있으면
                if (jobQueue.isEmpty()) {
                    Thread.yield(); // [V3와의 차이] V4에서는 yield() 추가!
                    // [설명] V3는 단순히 continue만 했으나, V4는 yield()를 호출하여 CPU 양보
                    // [장점] 불필요하게 CPU를 점유하지 않고, 다른 스레드(예: 입력 대기 중인 메인 스레드)에게 실행 기회를 줌
                    continue;
                }

                try {
                    String job = jobQueue.poll();
                    log("출력 시작: " + job + ", 대기 문서: " + jobQueue);
                    Thread.sleep(3000);
                    log("출력 완료");
                } catch (InterruptedException e) {
                    log("인터럽트!");
                    break;
                }
            }
            log("프린터 종료");
        }

        public void addJob(String input) {
            jobQueue.offer(input);
        }
    }
}
