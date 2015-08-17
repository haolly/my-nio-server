package handler;

import domin.User;
import event.*;
import event.DataInEvent;
import server.Dispatcher;

import java.nio.ByteBuffer;

/**
 * Created by liuhao on 15/8/15.
 */
public class DataInEventHandler implements EventHandler{
    @Override
    public void handleEvent(Event e) {
        if(e instanceof DataInEvent) {
            DataInEvent event = (DataInEvent)e;
            UserSession session = event.session;
            ByteBuffer content = event.content;
            int eventType = content.getInt();
            User user = session.getUser();
            switch (eventType) {
                case UserEventType.login:
                    Dispatcher.getInstance().addReadEvent(new LoginEvent(content), session);
                    break;
                case UserEventType.register:
                    if (user == null) {
                        Dispatcher.getInstance().addReadEvent(new RegisterEvent(content), session);
                    }
                    break;
                case UserEventType.logout:
                    break;
                case UserEventType.shutDownServer:
                    break;
            }
        }
    }

    private boolean checkCouldRegister(String name) {
        return true;
    }
}
