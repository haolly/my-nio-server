package event;

import handler.UserSession;

import java.util.List;

/**
 * Created by liuhao on 15/8/17.
 */
public class ResponseMessage {
    private List<UserSession> receivers;
    private byte[] bytes;

    public ResponseMessage(byte[] bytes, List<UserSession> receivers) {
        this.bytes = bytes;
        this.receivers = receivers;
    }

    public byte[] getMsg() {
        return bytes;
    }

    public List<UserSession> getReceivers() {
        return this.receivers;
    }

}
