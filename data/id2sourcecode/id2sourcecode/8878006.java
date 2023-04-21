    public void send(Message msg) throws ChannelNotConnectedException, ChannelClosedException {
        msg.putHeader(ID, hdr);
        mux.getChannel().send(msg);
        if (stats) {
            sent_msgs++;
            sent_bytes += msg.getLength();
        }
    }
