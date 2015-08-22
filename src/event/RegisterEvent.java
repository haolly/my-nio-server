package event;

import com.google.protobuf.InvalidProtocolBufferException;
import handler.UserSession;
import protocol.RegisterMsg;

import java.nio.ByteBuffer;

/**
 * Created by liuhao on 15/8/17.
 */
public class RegisterEvent implements Event{
    public String name;
    public String passwd;
    public String email;
    public UserSession session;
    public RegisterEvent(ByteBuffer data, UserSession session) {
        this.session =session;
        byte[] bytes = new byte[data.remaining()];
        data.get(bytes);
        try {
            RegisterMsg.Register register = RegisterMsg.Register.newBuilder().mergeFrom(bytes).build();
            this.name = register.getName();
            this.passwd = register.getPassword();
            if(register.hasEmail()) {
                this.email = register.getEmail();
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

    }
}
