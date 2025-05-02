package thread.control.printer;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

// 사용자 입력을 받아 프린터 작업 큐에 넣고, 별도의 스레드가 큐의 작업을 순차적으로 출력(처리)한다.
public class MyPrinterV1 {
    public static void main(String[] args) {
        // [메인 스레드] Printer 객체 생성 (프린터 역할, 작업 큐와 제어 플래그 포함)
        Printer printer = new Printer();
        // [메인 스레드] 프린터 스레드 생성 (Printer 객체를 Runnable로 전달)
        Thread printerThread = new Thread(printer, "printer");
        // [메인 스레드] 프린터 스레드 시작 (run() 메서드 실행)
        printerThread.start();
        // [메인 스레드] 사용자 입력을 받기 위한 Scanner 생성
        Scanner userInput = new Scanner(System.in);

        // [메인 스레드] 무한루프: 사용자로부터 계속 입력을 받음
        while (true) {
            log("프린터할 문서를 입력하세요. 종료 (q): ");
            String input = userInput.nextLine();

            // [메인 스레드] 입력이 'q'면 종료 신호를 프린터 스레드에 전달하고 루프 탈출
            if (input.equals("q")) {
                printer.work = false; // [공유 변수] 프린터 스레드 종료 플래그
                break;
            }
            // [메인 스레드] 입력한 문서를 프린터 작업 큐에 추가 (스레드 안전)
            printer.addJob(input);
        }
    }

    // 프린터 역할을 하는 Runnable 구현 클래스 (별도 스레드에서 실행)
    static class Printer implements Runnable {
        volatile boolean work = true; // [공유 변수] 프린터 동작 제어 플래그 (스레드 안전)
        Queue<String> jobQueue = new ConcurrentLinkedQueue<>(); // [공유 자원] 스레드 안전한 작업 큐

        @Override
        public void run() {
            while (work) {
                // [프린터 스레드] 작업 큐가 비어있으면 대기(루프 반복, busy-wait)
                // continue로 인해 아래 코드는 실행하지 않고 반복문의 처음으로 돌아간다.
                if (jobQueue.isEmpty()) {
                    continue;
                }
                // [프린터 스레드] 큐에서 작업 하나 꺼내오기 (poll: 큐가 비면 null 반환)
                String job = jobQueue.poll();

                // [프린터 스레드] 작업 시작 로그 출력 (현재 대기중인 문서도 함께 출력)
                log("출력 시작: " + job + ", 대기 문서: " + jobQueue);

                // [프린터 스레드] 실제 프린터가 출력하는 것처럼 3초간 대기 (작업 처리 시뮬레이션)
                sleep(3000);

                // [프린터 스레드] 작업 완료 로그 출력
                log("출력 완료");
            }
            log("프린터 종료");
        }

        // [메인 스레드] 작업 큐에 새 작업을 추가하는 메서드 (스레드 안전)
        public void addJob(String input) {
            jobQueue.offer(input); // 큐에 입력값 추가
        }
    }
}
