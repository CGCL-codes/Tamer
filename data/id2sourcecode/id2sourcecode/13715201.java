    void parseChannelOpen(Message m) throws IOException {
        String kind = m.getString();
        int reason = ChannelError.SUCCESS;
        int chanID = m.getInt();
        int initialWindowSize = m.getInt();
        int maxPacketSize = m.getInt();
        boolean reject = false;
        int myChanID = 0;
        Channel c = null;
        synchronized (mLock) {
            myChanID = getNextChannel();
            c = getChannelForKind(myChanID, kind, m);
            mChannels[myChanID] = c;
        }
        reason = mServer.checkChannelRequest(kind, myChanID);
        if (reason != ChannelError.SUCCESS) {
            mLog.debug("Rejecting '" + kind + "' channel request from client.");
            reject = true;
        }
        if (reject) {
            if (c != null) {
                synchronized (mLock) {
                    mChannels[myChanID] = null;
                }
            }
            Message mx = new Message();
            mx.putByte(MessageType.CHANNEL_OPEN_FAILURE);
            mx.putInt(chanID);
            mx.putInt(reason);
            mx.putString("");
            mx.putString("en");
            sendMessage(mx);
            return;
        }
        synchronized (mLock) {
            c.setTransport(this, mLog);
            c.setWindow(mWindowSize, mMaxPacketSize);
            c.setRemoteChannel(chanID, initialWindowSize, maxPacketSize);
            c.setServer(mServer);
        }
        Message mx = new Message();
        mx.putByte(MessageType.CHANNEL_OPEN_SUCCESS);
        mx.putInt(chanID);
        mx.putInt(myChanID);
        mx.putInt(mWindowSize);
        mx.putInt(mMaxPacketSize);
        sendMessage(mx);
        mLog.notice("Secsh channel " + myChanID + " opened.");
        synchronized (mServerAcceptLock) {
            mServerAccepts.add(c);
            mServerAcceptLock.notify();
        }
    }
