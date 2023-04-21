    public Channel openChannel(String kind, List parameters, int timeout_ms) throws IOException {
        if (!mActive) {
            throw new SSHException("No existing session.");
        }
        Event e = null;
        int chanid = 0;
        synchronized (mLock) {
            chanid = getNextChannel();
            Message m = new Message();
            m.putByte(MessageType.CHANNEL_OPEN);
            m.putString(kind);
            m.putInt(chanid);
            m.putInt(mWindowSize);
            m.putInt(mMaxPacketSize);
            if (parameters != null) {
                m.putAll(parameters);
            }
            Channel c = getChannelForKind(chanid, kind, parameters);
            if (c == null) {
                throw new ChannelException(ChannelError.ADMINISTRATIVELY_PROHIBITED);
            }
            mChannels[chanid] = c;
            e = new Event();
            mChannelEvents[chanid] = e;
            c.setTransport(this, mLog);
            c.setWindow(mWindowSize, mMaxPacketSize);
            sendUserMessage(m, timeout_ms);
        }
        if (!waitForEvent(e, timeout_ms)) {
            throw new SSHException("Timeout.");
        }
        synchronized (mLock) {
            Channel c = mChannels[chanid];
            if (c == null) {
                IOException x = getException();
                if (x == null) {
                    x = new SSHException("Unable to open channel.");
                }
                throw x;
            }
            return c;
        }
    }
