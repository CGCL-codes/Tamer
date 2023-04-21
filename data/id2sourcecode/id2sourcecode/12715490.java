    @Override
    public void connectionOpened(RTMPConnection conn, RTMP state) {
        log.debug("connectionOpened");
        Channel channel = conn.getChannel((byte) 3);
        PendingCall pendingCall = new PendingCall("connect");
        pendingCall.setArguments(connectArguments);
        Invoke invoke = new Invoke(pendingCall);
        invoke.setConnectionParams(connectionParams);
        invoke.setInvokeId(1);
        if (connectCallback != null) {
            pendingCall.registerCallback(connectCallback);
        }
        conn.registerPendingCall(invoke.getInvokeId(), pendingCall);
        log.debug("Writing 'connect' invoke: {}, invokeId: {}", invoke, invoke.getInvokeId());
        channel.write(invoke);
    }
