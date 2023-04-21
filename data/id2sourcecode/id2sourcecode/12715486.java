    public void publish(int streamId, String name, String mode, INetStreamEventHandler handler) {
        log.debug("publish - stream id: {}, name: {}, mode: {}", new Object[] { streamId, name, mode });
        Object[] params = new Object[2];
        params[0] = name;
        params[1] = mode;
        PendingCall pendingCall = new PendingCall("publish", params);
        conn.invoke(pendingCall, getChannelForStreamId(streamId));
        if (handler != null) {
            NetStreamPrivateData streamData = streamDataMap.get(streamId);
            if (streamData != null) {
                log.debug("Setting handler on stream data - handler: {}", handler);
                streamData.handler = handler;
            } else {
                log.debug("Stream data not found for stream id: {}", streamId);
            }
        }
    }
