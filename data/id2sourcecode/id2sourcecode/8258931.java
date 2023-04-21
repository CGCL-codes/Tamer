    private static void read(Object id, SelectionKey key) throws IOException {
        if (log.isLoggable(Level.FINEST)) log.finest(id + "" + key.attachment() + "reading data");
        WrapperAndListener struct = (WrapperAndListener) key.attachment();
        DataListener in = struct.getDataHandler();
        BasChannelImpl channel = (BasChannelImpl) struct.getChannel();
        ByteBuffer b = channel.getIncomingDataBuf();
        try {
            int bytes = -1;
            try {
                if (logBufferNextRead) log.info(channel + "buffer=" + b);
                bytes = channel.readImpl(b);
                if (logBufferNextRead) {
                    logBufferNextRead = false;
                    log.info(channel + "buffer2=" + b);
                }
            } catch (IOException e) {
                log.fine("message='" + e.getMessage() + "'");
                String msg = e.getMessage();
                if (msg == null) throw e;
                if (!msg.startsWith("An existing connection was forcibly closed")) throw e;
                bytes = -1;
            }
            processBytes(id, key, b, bytes);
        } catch (PortUnreachableException e) {
            log.log(Level.FINEST, id + "Client sent data to a host or port that is not listening " + "to udp, or udp can't get through to that machine", e);
            in.failure(channel, null, e);
        } catch (NotYetConnectedException e) {
            log.log(Level.WARNING, id + "Can't read until UDPChannel is connected", e);
            in.failure(channel, null, e);
        } catch (IOException e) {
            log.log(Level.FINE, id + "Exception", e);
            channel.close(NullWriteCallback.singleton(), -1);
            in.farEndClosed(channel);
        }
    }
