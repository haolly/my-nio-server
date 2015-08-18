package handler;

import domin.User;
import server.ServerConfig;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by liuhao on 15-5-17.
 */
public class UserSession {
    public static final int MAX_POCKET_SIZE_BYTE = 1024;
    public static final int HEAD_SIZE_BYTE= 4;
    public ByteBuffer inMsg;
    private BlockingQueue<ByteBuffer> outMsgQueue;
    private SocketChannel channel;
    public int inMsgHandleDistributeKey;
    public int outMsgHandleDistributeKey;
    private User user = null;

    public UserSession(SocketChannel channel){
        this.inMsg = ByteBuffer.allocateDirect(MAX_POCKET_SIZE_BYTE);
        this.outMsgQueue = new ArrayBlockingQueue<ByteBuffer>(ServerConfig.OUT_MESSAGE_QUEUE_SIZE);
        this.channel = channel;
        this.inMsgHandleDistributeKey = channel.hashCode() % ServerConfig.IN_MESSAGE_HANDLE_NUM;
        this.outMsgHandleDistributeKey = channel.hashCode() % ServerConfig.OUT_MESSAGE_HANDLE_NUM;
    }

    public void setUser(User u) {
        this.user = u;
    }

    public User getUser() {
        return this.user;
    }

    public SocketChannel getChannel() {
        return channel;
    }


    public boolean isHavingDataToWrite() {
        return outMsgQueue.size() != 0;
    }

    /**
     * Could not use the blocking method, cause this thread blocking
     * @return
     */
    public ByteBuffer getOutMsge() {
        return outMsgQueue.poll();
    }

    /**
     * Could not use the blocking method, cause this thread blocking
     * @param data
     */
    public void putOutMsg(ByteBuffer data) {
        boolean suc = outMsgQueue.offer(data);
        if (!suc) {
            // todo, log nedd more queue size
        }
    }
}
