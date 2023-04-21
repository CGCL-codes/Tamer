    private static void connect(Object id, SelectionKey key) throws IOException {
        if (log.isLoggable(Level.FINEST)) log.finest(id + "" + key.attachment() + "finishing connect process");
        WrapperAndListener struct = (WrapperAndListener) key.attachment();
        ConnectionListener callback = struct.getConnectCallback();
        BasTCPChannel channel = (BasTCPChannel) struct.getChannel();
        int interests = key.interestOps();
        key.interestOps(interests & (~SelectionKey.OP_CONNECT));
        try {
            channel.finishConnect();
            callback.connected(channel);
        } catch (Exception e) {
            log.log(Level.WARNING, id + "" + key.attachment() + "Could not open connection", e);
            callback.connectFailed(channel, e);
        }
    }
