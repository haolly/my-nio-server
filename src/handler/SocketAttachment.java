package handler;

import java.nio.ByteBuffer;

/**
 * Created by liuhao on 15-5-17.
 */
public class SocketAttachment {
    public static final int MAX_POCKET_SIZE_BYTE = 1024;
    public static final int HEAD_SIZE_BYTE= 4;
    public ByteBuffer inMsg;
    public ByteBuffer outMsg;

    public SocketAttachment(){
        this.inMsg = ByteBuffer.allocateDirect(MAX_POCKET_SIZE_BYTE);
        this.outMsg = ByteBuffer.allocateDirect(MAX_POCKET_SIZE_BYTE);
    }

}
