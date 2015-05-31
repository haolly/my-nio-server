package event.sys;

import event.Event;

import java.nio.channels.SocketChannel;

/**
 * Created by liuhao on 15-5-16.
 */
public class AcceptEvent implements Event {
    private SocketChannel socket;

    public AcceptEvent(SocketChannel socket) {
        this.socket = socket;
    }
    public SocketChannel getSocket(){
        return socket;
    }
}
