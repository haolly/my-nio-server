package handler;

import event.Event;
import event.domain.DataInEvent;
import event.sys.ReadEvent;
import server.Dispatcher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by liuhao on 15-5-16.
 * protocol:
 * dataSize[4]data[dataSize]
 */
public class ReadEventHandler implements EventHandler {

    @Override
    public void handleEvent(Event e) {
        if(e instanceof ReadEvent) {
            ReadEvent readEvent = (ReadEvent) e;
            SelectionKey key = readEvent.key;
            SocketChannel socketChannel = (SocketChannel) key.channel();
            SocketAttachment attachment = (SocketAttachment) key.attachment();
            try {

                int count = socketChannel.read(attachment.inMsg);
                if(count == -1) {
                    //end of stream
                    socketChannel.close();
                    key.cancel();
                }
                attachment.inMsg.flip();
                // not enough for a packet
                if(attachment.inMsg.remaining() < SocketAttachment.HEAD_SIZE_BYTE) {
                    attachment.inMsg.position(attachment.inMsg.remaining());
                    attachment.inMsg.limit(attachment.inMsg.capacity());
                }else {
                    int dataLen = attachment.inMsg.getInt();
                    while(attachment.inMsg.remaining() >= dataLen) {
                        byte[] data = new byte[dataLen];
                        attachment.inMsg.get(data);
                        Dispatcher.getInstance().addEvent(new DataInEvent(ByteBuffer.wrap(data), key), (SocketChannel) key.channel());

                        // not enough for a packet
                        if(attachment.inMsg.remaining() < SocketAttachment.HEAD_SIZE_BYTE) {
                            attachment.inMsg.compact();
                            return ;
                        }
                        dataLen = attachment.inMsg.getInt();
                    }
                    //not received full content, rollback to the head_size_byte
                    attachment.inMsg.position(attachment.inMsg.position()-4);
                    attachment.inMsg.compact();
                    attachment.inMsg.limit(attachment.inMsg.capacity());
                }
            } catch (IOException e1) {
                // todo,maybe the client closed socket
                e1.printStackTrace();
            }
        }
    }
}
