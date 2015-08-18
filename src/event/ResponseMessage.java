package event;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuhao on 15/8/17.
 */
public class ResponseMessage {
    private SocketChannel sender;
    private List<SocketChannel> receivers;
    private Map<Object, Object> responseData = new HashMap<>();
    private ByteBuffer msg;

    public ResponseMessage(Map<Object, Object> data) {
        this.responseData = data;
        // todo, convert response data to msg use the specific protocol

    }

    public ByteBuffer getMsg() {
        return msg;
    }

}
