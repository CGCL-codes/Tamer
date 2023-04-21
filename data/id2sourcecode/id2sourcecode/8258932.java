    private static void processBytes(Object id, SelectionKey key, ByteBuffer b, int bytes) throws IOException {
        WrapperAndListener struct = (WrapperAndListener) key.attachment();
        DataListener in = struct.getDataHandler();
        BasChannelImpl channel = (BasChannelImpl) struct.getChannel();
        b.flip();
        if (bytes < 0) {
            if (apiLog.isLoggable(Level.FINE)) apiLog.fine(channel + "far end closed, cancel key, close socket");
            channel.close(NullWriteCallback.singleton(), -1);
            in.farEndClosed(channel);
        } else if (bytes > 0) {
            if (apiLog.isLoggable(Level.FINER)) apiLog.finer(channel + "READ bytes=" + bytes);
            in.incomingData(channel, b);
            if (b.hasRemaining()) {
                log.warning(id + "Discarding unread data(" + b.remaining() + ") from class=" + in.getClass());
            }
        } else {
            if (log.isLoggable(Level.WARNING)) log.warning(channel + "READ 0 bytes(this is strange)...buffer=" + b);
            logBufferNextRead = true;
            assert false : "Should not occur";
        }
        b.clear();
    }
