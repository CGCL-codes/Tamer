    protected void processChannelData(SshMsgChannelExtendedData msg) throws IOException {
        synchronized (state) {
            if (msg.getChannelData().length > localWindow.getWindowSpace()) {
                throw new IOException("More data recieved than is allowed by the channel data window [" + name + "]");
            }
            long windowSpace = localWindow.consumeWindowSpace(msg.getChannelData().length);
            if (windowSpace < getMinimumWindowSpace()) {
                if (log.isDebugEnabled()) {
                    log.debug("Channel " + String.valueOf(localChannelId) + " requires more window space [" + name + "]");
                }
                windowSpace = getMaximumWindowSpace() - windowSpace;
                connection.sendChannelWindowAdjust(this, windowSpace);
                localWindow.increaseWindowSpace(windowSpace);
            }
            onChannelExtData(msg);
            Iterator it = eventListeners.iterator();
            ChannelEventListener eventListener;
            while (it.hasNext()) {
                eventListener = (ChannelEventListener) it.next();
                if (eventListener != null) {
                    eventListener.onDataReceived(this, msg.getChannelData());
                }
            }
        }
    }
