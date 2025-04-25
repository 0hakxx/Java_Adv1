package util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/*
로깅 기능을 제공하는 추상 클래스
- 추상 클래스이므로 객체를 생성하여 사용할 수 없다.(New MyLogger() 불가)
- 다음과 같이 log() 메서드를 호출하면,
  MyLogger.log("Hello, world!");
  다음과 같은 메시지가 콘솔에 출력된다.
  14:30:00.000 [main     ] Hello, world!
*/
public abstract class MyLogger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    // log() 메서드: 로깅 메시지를 출력하는 메서드
    // - obj: 출력할 객체 (모든 타입의 객체를 받을 수 있도록 Object 타입으로 선언)
    public static void log(Object obj) {
        // 현재 시간을 지정된 형식으로 가져오기
        String time = LocalTime.now().format(formatter);

        // 콘솔에 로깅 메시지 출력
        // - %s: 문자열 (time, 스레드 이름, obj)
        // - %9s: 9자리 문자열 (스레드 이름을 9자리로 맞춤)
        // - %n: 줄바꿈
        System.out.printf("%s [%9s] %s\n", time, Thread.currentThread().getName(), obj);
    }
}


