    public void channelQueUp(int i, CMMsg msg) {
        CMLib.map().sendGlobalMessage(msg.source(), CMMsg.TYP_CHANNEL, msg);
        Vector q = getChannelQue(i);
        synchronized (q) {
            if (q.size() >= QUEUE_SIZE) q.removeElementAt(0);
            q.addElement(msg);
        }
    }
