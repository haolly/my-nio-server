package domin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuhao on 15/8/17.
 */
public class UserManager {
    private Map<String, User> allUser = new ConcurrentHashMap<>();
    private UserManager() {}
    private static UserManager instance = new UserManager();

    public static UserManager getInstance() {
        return instance;
    }

    public User getUserByName(String name) {
        return allUser.get(name);
    }

    // TODO, 数据库连接池是什么时候建立的呢？
    public static boolean validateNewUserName(String regName) {
        return true;
    }

    // todo, add user info to database;
    public boolean addUser(User newUser) {
        if (!allUser.containsKey(newUser.getUserName())) {
            allUser.put(newUser.getUserName(), newUser);
        }
        return false;
    }
}
