package thread.start;

import static util.MyLogger.log;


//InnerRunnableMainV2에서 수정하여 변수 생성없이 바로 넘겨줄 수도 있음
public class InnerRunnableMainV3 {

    public static void main(String[] args) {
        log("main() start");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                log("run()");
            }
        });
        thread.start();

        log("main() end");
    }

}
