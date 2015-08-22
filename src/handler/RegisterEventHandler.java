package handler;

import domin.User;
import domin.UserManager;
import event.Event;
import event.RegisterEvent;
import event.ResponseMessage;
import protocol.RegisterMsg;
import server.MsgSender;

import java.util.ArrayList;
import java.util.List;

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
                //todo, log info
                return ;
            }
            String regName = event.name;
            String passwd = event.passwd;
            RegisterMsg.RegisterResult.Builder builder = RegisterMsg.RegisterResult.newBuilder();
            if(UserManager.validateNewUserName(regName)) {
                u = User.createUser(regName, u.getPasswd());
                session.setUser(u);
                builder.setRes(RegisterMsg.RegisterResult.ResultCode.OK);
                UserManager.getInstance().addUser(u);
            }
            else {
                builder.setRes(RegisterMsg.RegisterResult.ResultCode.ALREADY_IN_USE);
            }
            List<UserSession> receivers = new ArrayList<>();
            receivers.add(session);
            ResponseMessage responseMsg = new ResponseMessage(builder.build().toByteArray(), receivers);
            MsgSender.sendResponse(responseMsg);
        }
    }
}
