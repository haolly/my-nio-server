package db;

/**
 * Created by liuhao on 2015/9/17.
 */
public class DBException extends RuntimeException {
    public DBException(String msg) {
        super(msg);
    }

    public DBException(Throwable throwable, String msg) {
        super(msg, throwable);
    }
}
