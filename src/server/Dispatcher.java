package server;

import event.Event;
import event.DataInEvent;
import event.ResponseMessage;
import handler.DataInEventHandler;
import handler.EventHandler;
import handler.UserSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuhao on 15-5-16.
 * singleton pattern
 */
public class Dispatcher {
    public Map<Class<? extends Event>,EventHandler> handlerMap;
    private Reader reader;
    private Writer writer;

    private static Dispatcher INSTANCE;

    private Dispatcher() {
        this.handlerMap = new ConcurrentHashMap<Class<? extends Event>, EventHandler>();
        this.reader = new Reader(ServerConfig.IN_MESSAGE_HANDLE_NUM);
        this.writer = new Writer(ServerConfig.OUT_MESSAGE_HANDLE_NUM);
        INSTANCE = this;
        initEventHandler();
    }

    private void initEventHandler() {
        addEventHandler(DataInEvent.class, new DataInEventHandler());
    }

    public static Dispatcher getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Dispatcher();
        }
        return INSTANCE;
    }

    private boolean addEventHandler(Class<? extends Event> name, EventHandler handler) {
        if(!handlerMap.containsKey(name)) {
            handlerMap.put(name, handler);
            return true;
        }
        return false;
    }

    public EventHandler getEventHandler(Class<? extends Event> name) {
        return handlerMap.get(name);
    }


    public void addWriteEvent(ResponseMessage response, UserSession session) {
        this.writer.addResponse(response, session);
    }

    public void addReadEvent(Event event, UserSession session) {
        this.reader.addEvent(event, session);
    }


    public void shutDownReader() {
        this.reader.shutDown();
    }


}
