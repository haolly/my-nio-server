package event.domain;

import event.Event;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * Created by liuhao on 15-5-17.
 */
public class DataInEvent implements Event{
    public SelectionKey key;
    public ByteBuffer buffer;

    public DataInEvent(ByteBuffer buffer, SelectionKey key){
        this.buffer = buffer;
        this.key = key;
    }
}
