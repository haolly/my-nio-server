package handler;

import domin.User;
import domin.UserManager;
import event.Event;
import event.RegisterEvent;
import event.ResponseMessage;
import server.MsgSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuhao on 15/8/17.
 */
public class RegisterEventHandler implements EventHandler{
    @Override
    public void handleEvent(Event e) {
        if(e instanceof RegisterEvent) {
            RegisterEvent event = (RegisterEvent) e;
            UserSession session = event.session;
            User u = session.getUser();
            if(u != null) {
                return ;
            }
            String regName = event.name;
            Map<Object, Object> response = new HashMap<>();
            if(UserManager.validateNewUserName(regName)) {
                response.put("res", 1);
                u = User.createUser(regName, u.getPasswd());
                session.setUser(u);
            }
            else {
                response.put("res", -1);
            }
            ResponseMessage responseMsg = new ResponseMessage(response);
            MsgSender.sendResponse(responseMsg);
        }
    }
}
