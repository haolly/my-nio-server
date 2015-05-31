package server;

import event.sys.ReadEvent;
import event.sys.WriteEvent;
import handler.ReadEventHandler;
import handler.SocketAttachment;
import handler.WriteEventHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by liuhao on 15-5-15.
 * singleton pattern
 */
public class Acceptor extends Thread{
    private ServerSocketChannel serverSocketChannel;
    private Selector acceptSelector;
    private Selector readSelector;
    private volatile boolean isRunning;

    public Acceptor() {
        try {
            acceptSelector = Selector.open();
            readSelector = Selector.open();

            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 6677));
            serverSocketChannel.configureBlocking(false);

            serverSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
            isRunning = true;

            addEventListener();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addEventListener() {
        Dispatcher.getInstance().addEventHandler(ReadEvent.class, new ReadEventHandler());
        Dispatcher.getInstance().addEventHandler(WriteEvent.class, new WriteEventHandler());
    }

    @Override
    public void run() {
        while(isRunning) {
            try {
                int newCount = acceptSelector.selectNow();
                if(newCount > 0) {
                    Set<SelectionKey> keys = acceptSelector.selectedKeys();
                    for(Iterator<SelectionKey> iterator = keys.iterator(); iterator.hasNext(); ) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if(key.isAcceptable()) {
                            ServerSocketChannel serverSocket = (ServerSocketChannel) key.channel();
                            SocketChannel socketChannel = serverSocket.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
                            socketChannel.register(readSelector, SelectionKey.OP_READ, new SocketAttachment());
                        }
                    }
                }
                int readCount = readSelector.selectNow();
                if(readCount > 0) {
                    Set<SelectionKey> keys = readSelector.selectedKeys();
                    for(Iterator<SelectionKey> iterator = keys.iterator(); iterator.hasNext(); ) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        SocketChannel client = (SocketChannel) key.channel();
                        // todo, handle event will be blocked?
                        if(key.isWritable()) {
                            Dispatcher.getInstance().addEvent(new WriteEvent(key), client);
                            // reset to read only
                            key.interestOps(SelectionKey.OP_READ);
                        } else if(key.isReadable()) {
                            Dispatcher.getInstance().addEvent(new ReadEvent(key), client);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
