package server;

import event.Event;
import event.domain.DataInEvent;
import event.domain.DataOutEvent;
import event.sys.ReadEvent;
import event.sys.WriteEvent;
import handler.EventHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liuhao on 15-5-31.
 */
public class ReaderThread extends Thread{
    private BlockingQueue<Event> events = new LinkedBlockingQueue<Event>();
    @Override
    public void run() {
        Event event = null;
        try {
            event = events.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        EventHandler handler = null;
        if(event instanceof ReadEvent) {
            handler = Dispatcher.getInstance().handlerMap.get(ReadEvent.class);
            handler.handleEvent(event);
        }else if(event instanceof WriteEvent) {

        }else if (event instanceof DataInEvent) {

        }else if (event instanceof DataOutEvent) {

        }else {

        }
    }

    public void addEvent(Event event) {
        try {
            this.events.put(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
