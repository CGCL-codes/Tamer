    public void run() {
        lastFlushTime = System.currentTimeMillis();
        long currentTime;
        long waittime;
        int sendcounter = 0;
        Message msg;
        while (alive) {
            msg = null;
            try {
                msg = (Message) sendQueue.pop();
                switch(msg.getPayloadDescriptor()) {
                    case DescriptorHeader.QUERY_HIT:
                        Debug.log("invio msg QUERY_HIT");
                        break;
                }
                msg.write(out);
                sendcounter++;
                messagesSent++;
                sendcounter = 0;
                lastFlushTime = System.currentTimeMillis();
                out.flush();
            } catch (IOException e) {
                servent.stop();
                return;
            } catch (InterruptedException e) {
                Debug.log("Interrotto writer thread");
            }
        }
    }
