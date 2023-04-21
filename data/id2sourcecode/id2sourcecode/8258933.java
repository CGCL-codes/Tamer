    private static void write(Object id, SelectionKey key) throws IOException, InterruptedException {
        if (log.isLoggable(Level.FINEST)) log.finest(key.attachment() + "writing data");
        WrapperAndListener struct = (WrapperAndListener) key.attachment();
        BasChannelImpl channel = (BasChannelImpl) struct.getChannel();
        if (log.isLoggable(Level.FINER)) log.finer(channel + "notifying channel of write");
        channel.writeAll();
    }
