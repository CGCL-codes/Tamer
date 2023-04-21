    private static void acceptSocket(Object id, SelectionKey key) throws IOException {
        if (log.isLoggable(Level.FINER)) log.finer(id + "" + key.attachment() + "Incoming Connection=" + key);
        WrapperAndListener struct = (WrapperAndListener) key.attachment();
        ConnectionListener cb = struct.getAcceptCallback();
        BasTCPServerChannel channel = (BasTCPServerChannel) struct.getChannel();
        channel.accept("session " + channel.getSession(), cb);
    }
