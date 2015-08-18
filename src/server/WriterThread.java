package server;

import domin.User;
import handler.UserSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
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
            doWrite(session);
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

    private void doWrite(UserSession session) {
        SocketChannel clientChannel = session.getChannel();
        User user = session.getUser();
        if(user == null) {
            return ;
        }
        if (!clientChannel.isConnected()) {
            return;
        }
        ByteBuffer out = session.getOutMsge();
        if(out == null) {
            return ;
        }
        try {
            int needToWrite = out.remaining();
            while (needToWrite > 0) {
                int num = clientChannel.write(out);
                needToWrite -= num;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(session.isHavingDataToWrite()) {
            addResponse(session);
        }
    }
}
