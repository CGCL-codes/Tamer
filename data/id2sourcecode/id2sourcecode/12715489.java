    public void play(int streamId, String name, int start, int length) {
        log.debug("play stream {}, name: {}, start {}, length {}", new Object[] { streamId, name, start, length });
        if (conn != null) {
            Object[] params = new Object[3];
            params[0] = name;
            params[1] = start;
            params[2] = length;
            PendingCall pendingCall = new PendingCall("play", params);
            conn.invoke(pendingCall, getChannelForStreamId(streamId));
        } else {
            log.info("Connection was null ?");
        }
    }
