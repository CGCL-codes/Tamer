    public Channel lazilyGetAndConnect(String chanHandle, Channel channel) throws ConnectionException, NoSuchChannelException {
        Channel tmpChan;
        if (channel == null) {
            tmpChan = getChannel(chanHandle);
            if (tmpChan == null) {
                throw new NoSuchChannelException(this, chanHandle);
            }
        } else {
            tmpChan = channel;
        }
        tmpChan.connectAndWait();
        return tmpChan;
    }
