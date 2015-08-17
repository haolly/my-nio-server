package server;

/**
 * Created by liuhao on 15-5-31.
 * 必须保证同一个用户的读始终在同一个线程里面，写也在同一个线程里面
 */
public class Server {
    private static Server INSTANCE;
    private  Acceptor acceptor;

    public Server() {
        acceptor = new Acceptor();
    }
    public static void main(String[] args) {
        INSTANCE = new Server();
        INSTANCE.startServer();
    }

    public void startServer() {
        this.acceptor.start();
    }

    /**
     * todo
     * 关闭服务器步骤：
     * 不去接受新连接请求
     * 不去读取已经连接的客户端的请求
     * 将所有的写出事件处理完
     * 关闭所有连接
     */
    public void shutDown() {
        acceptor.shutDown();
        Dispatcher.getInstance().shutDownReader();
    }
}
