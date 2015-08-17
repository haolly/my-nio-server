package server;

import handler.UserSession;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by liuhao on 15/8/17.
 */
public class WriterThread extends Thread{
    private volatile boolean isRunning = true;
    private BlockingQueue<UserSession> queue = new LinkedBlockingDeque<>();

    @Override
    public void run() {
        if(!isRunning) {
            return ;
        }
        try {
            UserSession session = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void shutDown() {
        isRunning = false;
    }

    public void addResponse(UserSession session) {
        try {
            queue.put(session);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
