    public synchronized SSH2SessionChannel newSession(SSH2StreamFilterFactory filterFactory) {
        SSH2SessionChannel channel = new SSH2SessionChannel(this);
        if (filterFactory != null) {
            channel.applyFilter(filterFactory.createFilter(this, channel));
        }
        SSH2TransportPDU pdu = getChannelOpenPDU(channel);
        transmit(pdu);
        return channel;
    }
