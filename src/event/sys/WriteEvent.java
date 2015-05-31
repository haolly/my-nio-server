package event.sys;

import event.Event;

import java.nio.channels.SelectionKey;

/**
 * Created by liuhao on 15-5-16.
 */
public class WriteEvent implements Event{
    public SelectionKey key;
    public WriteEvent(SelectionKey key) {
        this.key = key;
    }
}
