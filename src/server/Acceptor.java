package server;

import event.DataInEvent;
import handler.UserSession;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
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
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", ServerConfig.SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            serverSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
            isRunning = true;

        } catch (IOException e) {
            e.printStackTrace();
        }

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
                            socketChannel.register(readSelector, SelectionKey.OP_READ, new UserSession(socketChannel));
                            ConnectionManager.instance.addConnection(socketChannel);
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
                        UserSession session = (UserSession) key.attachment();
                        // handle event will be blocked? no
                        if(key.isWritable()) {
                            // reset to read only
                            Dispatcher.getInstance().addWriteEvent(session);
                            key.interestOps(SelectionKey.OP_READ);
                        } else if(key.isReadable()) {
                            handleRead(client, session);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void handleRead(SocketChannel socketChannel, UserSession session) {
        int count = 0;
        try {
            count = socketChannel.read(session.inMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(count == -1) {
            //end of stream
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // not enough for a packet
        if (count <4) {
            return ;
        }
        session.inMsg.flip();

        int dataLen = session.inMsg.getInt();
        if(session.inMsg.remaining() < dataLen) {
            session.inMsg.position(session.inMsg.limit());
            session.inMsg.limit(session.inMsg.capacity());
            return ;
        }

        while(session.inMsg.remaining() >= dataLen) {
            byte[] data = new byte[dataLen];
            session.inMsg.get(data);
            //when wrap, the content is ready for read
            Dispatcher.getInstance().addReadEvent(new DataInEvent(ByteBuffer.wrap(data), session), session);

            // not enough for a packet
            if(session.inMsg.remaining() < UserSession.HEAD_SIZE_BYTE) {
                session.inMsg.compact();
                return ;
            }
            dataLen = session.inMsg.getInt();
        }
        //not received full content, rollback to the head_size_byte
        session.inMsg.position(session.inMsg.position()-4);
        session.inMsg.compact();
    }

    public void shutDown() {
        isRunning = false;
        try {
            serverSocketChannel.close();
        } catch (IOException e) {

        }
    }
}
