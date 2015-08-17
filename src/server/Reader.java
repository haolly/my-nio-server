package server;

import event.Event;
import handler.UserSession;

/**
 * Created by liuhao on 15-5-31.
 * 需要保证同一用户的消息按顺序执行
 */
public class Reader {
    private ReaderThread[] worker;
    private int workerNum;
    public Reader(int workerNum) {
        this.workerNum = workerNum;
        worker = new ReaderThread[workerNum];
        for (int i=0; i<workerNum; i++) {
            worker[i] = new ReaderThread();
            worker[i].start();
        }
    }

    public void addEvent(Event event, UserSession session) {
        int distribute = session.inMsgHandleDistributeKey;
        worker[distribute].addEvent(event);
    }

    public void shutDown() {
        for(ReaderThread reader: worker) {
            reader.shutDown();
        }
    }
}
