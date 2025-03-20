package thread.start;

import static thread.util.MyLogger.*;

public class ThreadInfoMain {
    public static void main(String[] args){
        Thread mainThread = Thread.currentThread();
        log("mainThread = " + mainThread);

    }



}
