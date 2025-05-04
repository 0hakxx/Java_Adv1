package thread.control.printer;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.MyLogger.log;

public class MyPrinterV3 {

    public static void main(String[] args) {
        Printer printer = new Printer();
        Thread printerThread = new Thread(printer, "printer");
        printerThread.start();

        Scanner userInput = new Scanner(System.in);
        while (true) {
            log("프린터할 문서를 입력하세요. 종료 (q): ");
            String input = userInput.nextLine();
            if (input.equals("q")) {
                printerThread.interrupt(); // [중요] 프린터 스레드에 인터럽트 신호만 보냄 (V2와의 차이: work 플래그 없음)
                // [V2와의 차이] V2는 printer.work = false 도 함께 사용하지만, V3는 플래그 없이 interrupt만 사용
                break;
            }
            printer.addJob(input); // [중요] 입력된 문서를 작업 큐에 추가 (스레드 안전)
        }
    }

    static class Printer implements Runnable {
        Queue<String> jobQueue = new ConcurrentLinkedQueue<>(); // [중요] 스레드 안전한 작업 큐

        @Override
        public void run() {
            // [중요] 반복 조건에 Thread.interrupted() 사용
            // [V2와의 차이] V2는 work 플래그로 반복을 제어했지만, V3는 인터럽트 상태로만 반복 제어
            while (!Thread.interrupted()) {
                if (jobQueue.isEmpty()) {
                    continue; // [중요] 작업이 없으면 대기 (busy-wait)
                }

                try {
                    String job = jobQueue.poll(); // [중요] 큐에서 작업 하나 꺼냄
                    log("출력 시작: " + job + ", 대기 문서: " + jobQueue);
                    Thread.sleep(3000); // [중요] 출력 작업 시뮬레이션 (3초 대기)
                    log("출력 완료");
                } catch (InterruptedException e) {
                    log("인터럽트!"); // [중요] sleep 중 인터럽트 발생 시 즉시 종료 루트로 진입
                    break;
                }
            }
            log("프린터 종료"); // [중요] 스레드 종료 시점
        }

        public void addJob(String input) {
            jobQueue.offer(input); // [중요] 작업 큐에 문서 추가
        }
    }
}
