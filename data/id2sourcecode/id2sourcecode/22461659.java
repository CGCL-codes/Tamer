    public void messageSent(RTMPConnection conn, Object message) {
        if (log.isDebugEnabled()) {
            log.debug("Message sent");
        }
        if (message instanceof ByteBuffer) {
            return;
        }
        conn.messageSent((Packet) message);
        Packet sent = (Packet) message;
        final int channelId = sent.getHeader().getChannelId();
        final IClientStream stream = conn.getStreamByChannelId(channelId);
        if (stream != null && (stream instanceof PlaylistSubscriberStream)) {
            ((PlaylistSubscriberStream) stream).written(sent.getMessage());
        }
    }
