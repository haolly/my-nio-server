package event.domain;

import event.Event;

import java.nio.ByteBuffer;

/**
 * Created by liuhao on 15-5-17.
 */
public class DataOutEvent implements Event{
    private ByteBuffer buffer;
    public DataOutEvent(ByteBuffer buffer) {
        this.buffer = buffer;
    }
}
