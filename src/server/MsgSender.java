package server;

import event.ResponseMessage;
import handler.UserSession;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by liuhao on 15/8/17.
 */
public class MsgSender {
    public static void sendResponse(ResponseMessage response) {
        List<UserSession> receivers = response.getReceivers();
        for(int i=0; i<receivers.size(); i++) {
            UserSession receive = receivers.get(i);
            receive.putOutMsg(ByteBuffer.wrap(response.getMsg()));
        }
    }
}
