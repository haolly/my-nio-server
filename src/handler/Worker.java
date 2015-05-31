package handler;

import java.nio.channels.SelectionKey;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liuhao on 15-5-17.
 */
public class Worker extends Thread {
    private BlockingQueue<SelectionKey> queue = new LinkedBlockingQueue<SelectionKey>();
    public Worker(String name) {
        super(name);
    }

    @Override
    public void run() {
        while(queue.size() > 0) {

        }
    }
}
