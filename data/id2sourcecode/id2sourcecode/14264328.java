    private boolean writePacket(Packet p, SocketChannel ch) {
        if (LOG.isDebugEnabled()) LOG.debug("Writing packet #" + p.getSerial() + " to channel " + p.getChannelID());
        try {
            byte[] data = p.toBytes();
            ByteBuffer data_buf = ByteBuffer.allocate(data.length + 4);
            data_buf.clear();
            data_buf.putInt(data.length);
            data_buf.put(data);
            data_buf.flip();
            while (data_buf.hasRemaining()) {
                LOG.debug("Writing data to channel");
                if (0 == ch.write(data_buf)) {
                    throw new IOException("TCP send buffer OVERFLOW on channel " + ch.hashCode());
                }
            }
        } catch (IOException e) {
            LOG.error("Error writing packet " + p.getSerial() + " to channel " + ch.hashCode());
            dropChannel(ch);
            return false;
        }
        return true;
    }
