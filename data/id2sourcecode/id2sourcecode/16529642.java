    @Override
    public void messageReceived(Object in, IoSession session) throws Exception {
        RTMPConnection conn = (RTMPConnection) session.getAttribute(RTMPConnection.RTMP_CONNECTION_KEY);
        RTMP state = (RTMP) session.getAttribute(ProtocolState.SESSION_KEY);
        IRTMPEvent message = null;
        final Packet packet = (Packet) in;
        message = packet.getMessage();
        final Header header = packet.getHeader();
        final Channel channel = conn.getChannel(header.getChannelId());
        conn.messageReceived();
        if (header.getDataType() == TYPE_BYTES_READ) {
            onStreamBytesRead(conn, channel, header, (BytesRead) message);
        }
        if (header.getDataType() == TYPE_INVOKE) {
            final IServiceCall call = ((Invoke) message).getCall();
            final String action = call.getServiceMethodName();
            if (call.getServiceName() == null && !conn.isConnected() && StreamAction.valueOf(action).equals(StreamAction.CONNECT)) {
                handleConnect(conn, channel, header, (Invoke) message, (RTMP) state);
                return;
            }
        }
        switch(header.getDataType()) {
            case TYPE_CHUNK_SIZE:
            case TYPE_INVOKE:
            case TYPE_FLEX_MESSAGE:
            case TYPE_NOTIFY:
            case TYPE_AUDIO_DATA:
            case TYPE_VIDEO_DATA:
            case TYPE_FLEX_SHARED_OBJECT:
            case TYPE_FLEX_STREAM_SEND:
            case TYPE_SHARED_OBJECT:
            case TYPE_BYTES_READ:
                forwardPacket(conn, packet);
                break;
            case TYPE_PING:
                onPing(conn, channel, header, (Ping) message);
                break;
            default:
                if (log.isDebugEnabled()) {
                    log.debug("Unknown type: {}", header.getDataType());
                }
        }
        if (message instanceof Unknown) {
            log.info(message.toString());
        }
        if (message != null) {
            message.release();
        }
    }
