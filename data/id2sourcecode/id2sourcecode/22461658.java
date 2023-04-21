    public void messageReceived(RTMPConnection conn, ProtocolState state, Object in) throws Exception {
        IRTMPEvent message = null;
        try {
            final Packet packet = (Packet) in;
            message = packet.getMessage();
            final Header header = packet.getHeader();
            final Channel channel = conn.getChannel(header.getChannelId());
            final IClientStream stream = conn.getStreamById(header.getStreamId());
            if (log.isDebugEnabled()) {
                log.debug("Message recieved");
                log.debug("Stream Id: " + header);
                log.debug("Channel: " + channel);
            }
            Red5.setConnectionLocal(conn);
            BaseRTMPHandler.setStreamId(header.getStreamId());
            conn.messageReceived();
            message.setSource(conn);
            switch(header.getDataType()) {
                case TYPE_CHUNK_SIZE:
                    onChunkSize(conn, channel, header, (ChunkSize) message);
                    break;
                case TYPE_INVOKE:
                case TYPE_FLEX_MESSAGE:
                    onInvoke(conn, channel, header, (Invoke) message, (RTMP) state);
                    if (message.getHeader().getStreamId() != 0 && ((Invoke) message).getCall().getServiceName() == null && ACTION_PUBLISH.equals(((Invoke) message).getCall().getServiceMethodName())) {
                        if (stream != null) {
                            ((IEventDispatcher) stream).dispatchEvent(message);
                        }
                    }
                    break;
                case TYPE_NOTIFY:
                    if (((Notify) message).getData() != null && stream != null) {
                        ((IEventDispatcher) stream).dispatchEvent(message);
                    } else {
                        onInvoke(conn, channel, header, (Notify) message, (RTMP) state);
                    }
                    break;
                case TYPE_FLEX_STREAM_SEND:
                    if (stream != null) {
                        ((IEventDispatcher) stream).dispatchEvent(message);
                    }
                    break;
                case TYPE_PING:
                    onPing(conn, channel, header, (Ping) message);
                    break;
                case TYPE_BYTES_READ:
                    onStreamBytesRead(conn, channel, header, (BytesRead) message);
                    break;
                case TYPE_AUDIO_DATA:
                case TYPE_VIDEO_DATA:
                    if (stream != null) ((IEventDispatcher) stream).dispatchEvent(message);
                    break;
                case TYPE_FLEX_SHARED_OBJECT:
                case TYPE_SHARED_OBJECT:
                    onSharedObject(conn, channel, header, (SharedObjectMessage) message);
                    break;
                default:
                    log.debug("Unknown type: {}", header.getDataType());
            }
            if (message instanceof Unknown) {
                log.info("{}", message);
            }
        } catch (RuntimeException e) {
            log.error("Exception", e);
        }
        if (message != null) {
            message.release();
        }
    }
