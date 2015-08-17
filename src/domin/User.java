package domin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liuhao on 15/8/15.
 */
public class User {
    private static AtomicInteger idGen = new AtomicInteger(0);
    private int userId;
    private String userName;
    private String passwd;
    private boolean isAdmin;
    private List<String> friends;
    private Map<String, Object> userVariable = new ConcurrentHashMap<>();

    public User(int id, String name, String passwd) {
        this.userId = id;
        this.passwd = passwd;
        this.userName = name;
        this.isAdmin = false;
    }

    public static User createUser(String name, String passwd) {
        return new User(generateNewId(), name, passwd);
    }

    private static int generateNewId() {
        return idGen.getAndDecrement();
    }

    public boolean isAdmin() {
        return  this.isAdmin;
    }

    public String getUserName() {
        return userName;
    }

    public String getPasswd() {
        return  passwd;
    }
}
