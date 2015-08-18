package server;

import handler.UserSession;

/**
 * Created by liuhao on 15/8/17.
 */
public class Writer {
    private WriterThread[] worker;

    public Writer(int workerNum) {
        worker = new WriterThread[workerNum];
        for (int i=0; i<workerNum; i++) {
            worker[i]  = new WriterThread();
            worker[i].start();
        }
    }

    public void addResponse(UserSession session) {
        int distribute = session.inMsgHandleDistributeKey;
        worker[distribute].addResponse(session);
    }

    public void shutDown() {
        for(WriterThread writer: worker) {
            writer.shutDown();
        }
    }
}
