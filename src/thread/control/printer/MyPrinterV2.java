package thread.control.printer;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class MyPrinterV2 {

    public static void main(String[] args) {
        // Printer 객체 생성 (프린터 역할을 하는 Runnable)
        Printer printer = new Printer();
        // Printer를 실행할 별도의 스레드 생성
        Thread printerThread = new Thread(printer, "printer");
        // 프린터 스레드 시작
        printerThread.start();

        // 사용자 입력을 받기 위한 Scanner 생성
        Scanner userInput = new Scanner(System.in);
        while (true) {
            log("프린터할 문서를 입력하세요. 종료 (q): ");
            // 사용자로부터 한 줄 입력 받음
            String input = userInput.nextLine();
            // 입력이 'q'이면 종료
            if (input.equals("q")) {
                // 프린터 작업 종료 신호
                printer.work = false;
                // 프린터 스레드에 인터럽트 발생시켜 즉시 종료 유도
                printerThread.interrupt();
                break;
            }
            // 입력받은 문서를 프린터 작업 큐에 추가
            printer.addJob(input);
        }
    }

    // 프린터 역할을 하는 내부 클래스 (Runnable 구현)
    static class Printer implements Runnable {
        // 프린터 동작 여부를 제어하는 플래그 (volatile로 스레드 간 동기화)
        volatile boolean work = true;
        // 출력할 작업(문서)들을 저장하는 큐 (스레드 안전한 ConcurrentLinkedQueue 사용)
        Queue<String> jobQueue = new ConcurrentLinkedQueue<>();

        @Override
        public void run() {
            // work가 true인 동안 반복 (프린터 동작)
            while (work) {
                // 작업 큐가 비어있으면 아무것도 하지 않고 대기(continue)
                if (jobQueue.isEmpty()) {
                    continue;
                }

                try {
                    // 큐에서 하나의 작업(문서) 꺼내옴
                    String job = jobQueue.poll();
                    // 출력 시작 로그 및 현재 대기 중인 문서 목록 출력
                    log("출력 시작: " + job + ", 대기 문서: " + jobQueue);
                    // 출력하는 데 3초 소요 (Thread.sleep)
                    Thread.sleep(3000);
                    // 출력 완료 로그
                    log("출력 완료");
                } catch (InterruptedException e) {
                    // 스레드가 인터럽트되면 예외 발생, 로그 출력 후 반복 종료
                    log("인터럽트!");
                    break;
                }
            }
            // 프린터 종료 로그
            log("프린터 종료");
        }

        // 작업(문서)을 큐에 추가하는 메서드
        public void addJob(String input) {
            jobQueue.offer(input);
        }
    }
}


//정확한 차이점 요약 (주석 위치 기준)
//main()에서 interrupt() 호출
//
//printerThread.interrupt(); // [V1과의 차이] ...
//
//Printer.run()에서 InterruptedException 처리
//
//try { ... } catch (InterruptedException e) { ... } // [V1과의 차이] ...