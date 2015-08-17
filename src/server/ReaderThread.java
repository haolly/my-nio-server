package server;

import event.Event;
import event.LoginEvent;
import event.DataInEvent;
import event.RegisterEvent;
import handler.EventHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liuhao on 15-5-31.
 */
public class ReaderThread extends Thread{
    private BlockingQueue<Event> events = new LinkedBlockingQueue<Event>();
    private volatile boolean isRunning = true;

    @Override
    public void run() {
        if(!isRunning) {
            return;
        }
        Event event = null;
        try {
            event = events.take();
        } catch (InterruptedException e) {
            //TODO, server is shutdown?
            e.printStackTrace();
        }
        EventHandler handler = null;
        if (event instanceof DataInEvent) {
            handler = Dispatcher.getInstance().getEventHandler(DataInEvent.class);
            handler.handleEvent(event);
        } else if (event instanceof LoginEvent){
            handler = Dispatcher.getInstance().getEventHandler(LoginEvent.class);
            handler.handleEvent(event);
        } else if (event instanceof RegisterEvent) {
            handler = Dispatcher.getInstance().getEventHandler(RegisterEvent.class);
            handler.handleEvent(event);
        }
    }

    public void addEvent(Event event) {
        try {
            this.events.put(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutDown() {
        this.isRunning = false;
    }
}
