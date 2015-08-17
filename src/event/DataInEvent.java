package event;

import handler.UserSession;

import java.nio.ByteBuffer;

/**
 * Created by liuhao on 15-5-17.
 */
public class DataInEvent implements Event{
    public UserSession session;
    public ByteBuffer content;
    private int type;

    public DataInEvent(ByteBuffer buffer, UserSession session){
        this.content = buffer;
        this.session = session;
        this.type = buffer.getInt();
    }

    public int getType() {
        return type;
    }
}
