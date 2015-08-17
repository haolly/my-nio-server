package handler;

import domin.User;
import server.ServerConfig;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by liuhao on 15-5-17.
 */
public class UserSession {
    public static final int MAX_POCKET_SIZE_BYTE = 1024;
    public static final int HEAD_SIZE_BYTE= 4;
    public ByteBuffer inMsg;
    public ByteBuffer[] outMsg;
    private SocketChannel channel;
    public int inMsgHandleDistributeKey;
    public int outMsgHandleDistributeKey;
    private User user = null;

    public UserSession(SocketChannel channel){
        this.inMsg = ByteBuffer.allocateDirect(MAX_POCKET_SIZE_BYTE);
        this.outMsg = new ByteBuffer[10];
        for (int i=0;i<10; i++) {
            this.outMsg[i] = ByteBuffer.allocate(MAX_POCKET_SIZE_BYTE);
        }
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
}
