package handler;

import event.Event;
import event.domain.DataInEvent;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by liuhao on 15-5-17.
 * 这里应该是具体应用的逻辑处理，可以使用多个线程来处理数据,需要保证每个用户的消息处理是FIFO
 * todo
 */
public class DataInEventHandler implements EventHandler {
    public static final int WORKER_THREAD_NUM = 5;


    @Override
    public void handleEvent(Event e) {
        if(e instanceof DataInEvent) {
            DataInEvent event = (DataInEvent)e;
            ByteBuffer data = event.buffer;
            SelectionKey key = event.key;
            //todo, default behavior
            CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
            try {
                CharBuffer charBuffer = decoder.decode(data);
                charBuffer.flip();
                Writer writer = new OutputStreamWriter(System.out);
                if(charBuffer.hasRemaining()) {
                    writer.write(charBuffer.toString());
                }
                // prepare for write to client
                key.interestOps(SelectionKey.OP_WRITE);
            } catch (CharacterCodingException e1) {
                //decode error
            } catch (IOException e1) {
                // write error
            }
        }
    }
}
