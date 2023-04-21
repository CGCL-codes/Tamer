    @Override
    public void connectionOpened(RTMPConnection conn, RTMP state) {
        Channel channel = conn.getChannel((byte) 3);
        PendingCall pendingCall = new PendingCall("connect");
        Invoke invoke = new Invoke(pendingCall);
        invoke.setConnectionParams(connectionParams);
        invoke.setInvokeId(conn.getInvokeId());
        if (connectCallback != null) pendingCall.registerCallback(connectCallback);
        conn.registerPendingCall(invoke.getInvokeId(), pendingCall);
        channel.write(invoke);
    }
