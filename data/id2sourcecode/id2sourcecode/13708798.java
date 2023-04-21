    protected void processChannelData(SshMsgChannelData msg) throws IOException {
        synchronized (state) {
            if (!isClosed()) {
                if (msg.getChannelDataLength() > localWindow.getWindowSpace()) {
                    throw new IOException("More data recieved than is allowed by the channel data window [" + name + "]");
                }
                long windowSpace = localWindow.consumeWindowSpace(msg.getChannelData().length);
                if (windowSpace < getMinimumWindowSpace()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Channel " + String.valueOf(localChannelId) + " requires more window space [" + name + "]");
                    }
                    windowSpace = getMaximumWindowSpace() - windowSpace;
                    log.debug("Requesting connection protocol increase window");
                    connection.sendChannelWindowAdjust(this, windowSpace);
                    localWindow.increaseWindowSpace(windowSpace);
                }
                onChannelData(msg);
                Iterator it = eventListeners.iterator();
                ChannelEventListener eventListener;
                while (it.hasNext()) {
                    eventListener = (ChannelEventListener) it.next();
                    if (eventListener != null) {
                        eventListener.onDataReceived(this, msg.getChannelData());
                    }
                }
            } else {
                throw new IOException("Channel data received but channel is closed [" + name + "]");
            }
        }
    }
