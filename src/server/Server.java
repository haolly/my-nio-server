package server;

/**
 * Created by liuhao on 15-5-31.
 */
public class Server {
    private static Acceptor acceptor;
    private static Reader reader;
    private static final int THREAD_NUM = 4;
    public static void main(String[] args) {
        acceptor = new Acceptor();
        reader = new Reader(4);
        acceptor.start();
    }
}
