    public void unpublish(int streamId) {
        log.debug("unpublish stream {}", streamId);
        PendingCall pendingCall = new PendingCall("publish", new Object[] { false });
        conn.invoke(pendingCall, getChannelForStreamId(streamId));
    }
