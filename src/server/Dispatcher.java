package server;

import event.Event;
import handler.EventHandler;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuhao on 15-5-16.
 * singleton pattern
 */
public class Dispatcher {
    public Map<Class<? extends Event>,EventHandler> handlerMap;
    private Reader reader;
    private static Dispatcher INSTANCE;
    private Dispatcher() {
        this.handlerMap = new ConcurrentHashMap<Class<? extends Event>, EventHandler>();
        this.reader = new Reader(100);
        INSTANCE = this;
    }

    public static Dispatcher getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Dispatcher();
        }
        return INSTANCE;
    }

    public boolean addEventHandler(Class<? extends Event> name, EventHandler handler) {
        if(!handlerMap.containsKey(name)) {
            handlerMap.put(name, handler);
            return true;
        }
        return false;
    }

    public boolean removeEventHandler(String name, EventHandler handler) {
        if(handlerMap.containsKey(name)) {
            handlerMap.remove(name);
            return true;
        }
        return false;
    }

    public void addEvent(Event event, SocketChannel client) {
        this.reader.addEvent(event, client);
    }

}
