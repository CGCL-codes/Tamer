    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        MRTMPPacket packet = (MRTMPPacket) message;
        if (packet.getHeader().getType() != MRTMPPacket.RTMP) {
            return;
        }
        MRTMPPacket.Header header = packet.getHeader();
        MRTMPPacket.Body body = packet.getBody();
        int clientId = header.getClientId();
        int sessionId = getSessionId(session);
        RTMPOriginConnection conn = null;
        lock.readLock().lock();
        try {
            if (header.isDynamic()) {
                conn = dynConnMap.get(clientId);
            } else {
                StaticConnId connId = new StaticConnId();
                connId.clientId = header.getClientId();
                connId.sessionId = sessionId;
                conn = statConnMap.get(connId);
            }
        } finally {
            lock.readLock().unlock();
        }
        if (conn != null) {
            MRTMPPacket.RTMPBody rtmpBody = (MRTMPPacket.RTMPBody) body;
            final int channelId = rtmpBody.getRtmpPacket().getHeader().getChannelId();
            final IClientStream stream = conn.getStreamByChannelId(channelId);
            if (stream != null && (stream instanceof PlaylistSubscriberStream)) {
                ((PlaylistSubscriberStream) stream).written(rtmpBody.getRtmpPacket().getMessage());
            }
        } else {
            log.warn("Handle on a non-existent origin connection!");
        }
    }
