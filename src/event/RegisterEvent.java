package event;

import handler.UserSession;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by liuhao on 15/8/17.
 */
public class RegisterEvent implements Event{
    public String name;
    public String passwd;
    public UserSession session;
    public RegisterEvent(ByteBuffer data, UserSession session) {
        this.session =session;
        int nameLen = data.getInt();
        byte[] byteName = new byte[nameLen];
        data.get(byteName);
        try {
            name = new String(byteName,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int passwdLen = data.getInt();
        byte[] bytePasswd = new byte[passwdLen];
        data.get(bytePasswd);
        try {
            passwd = new String(bytePasswd, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
