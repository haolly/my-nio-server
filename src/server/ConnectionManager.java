package server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuhao on 15-6-6.
 * manage client socket connection, which consume system resources
 */
public class ConnectionManager {
    public static ConnectionManager instance = new ConnectionManager();
    private ConnectionManager() {}
    private Map<SocketChannel, Long> connections = new ConcurrentHashMap<>();
    public void addConnection(SocketChannel socketChannel) {
        connections.put(socketChannel,System.currentTimeMillis());
    }

    public void closeConnection(SocketChannel socketChannel) {
        if(connections.containsKey(socketChannel)) {
            try {
                socketChannel.close();
                connections.remove(socketChannel);
            } catch (IOException e) {

            }
        }
    }

    public void closeAllConnection() {
        for(SocketChannel sc: connections.keySet()) {
            try {
                sc.close();
            } catch (IOException e) {

            }
            connections.clear();
        }
    }

    public void checkIdleConnection() {
        for(Map.Entry<SocketChannel, Long> entry : connections.entrySet()) {
            SocketChannel sc = entry.getKey();
            if(System.currentTimeMillis() - entry.getValue() > 60 * 1000) {
                closeConnection(sc);
            }
        }
    }

    public void updateLastOpTime(SocketChannel sc) {

    }
}
