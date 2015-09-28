package db;

/**
 * Created by liuhao on 2015/9/18.
 */
public class DBConfig {
    private static final String MySqlDriver = "com.mysql.jdbc.Driver";
    private static final String DefaultHost = "localhost";

    public String name; //used by program

    public String host;
    public String db;   // used by ConnectionPool

    // must!
    public String driver;
    public String userName;
    public String passWord;
    public String jdbcUrl =
            String.format("jdbc:mysql://%s/%s?user=%s&password=%s&autoReconnect=true",
                    host, db,userName, passWord);

    // basic
    public int poolMin;
    public int poolMax;
    public int initPoolSize;
    public int acquireIncrement;

    // connectionValidTest
    /**
     * If you don't know what to do, try this:

     If you know your driver supports the JDBC 4 Connection.isValid(...) method and you are using c3p0-0.9.5 or above, don't set a preferredTestQuery. If your driver does not support this method (or if you are not sure), try SELECT 1 for your preferredTestQuery, if you are running MySQL or Postgres. For other databases, look for suggestions here. Leave automatedTestTable undefined.

     Begin by setting testConnectionOnCheckout to true and get your application to run correctly and stably. If you are happy with your application's performance, you can stop here! This is the simplest, most reliable form of Connection-testing, but it does have a client-visible performance cost.
     */
    public String preferredTestQuery;

    //
    public boolean batchOptimze = true;
    public int checkoutTimeout;
    public int idleConnectionTestPeriod;
    public boolean testConnectionOnCheckIn = false;

    public DBConfig(String name, String db, String userName, String passWord) {
        this.name = name;
        this.db = db;
        this.userName = userName;
        this.passWord = passWord;
    }
}
