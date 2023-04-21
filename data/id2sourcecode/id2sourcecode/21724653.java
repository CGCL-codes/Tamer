    public void msgChannelExtendedData(byte[] msg, int msglen) throws IOException {
        if (msglen <= 13) throw new IOException("SSH_MSG_CHANNEL_EXTENDED_DATA message has wrong size (" + msglen + ")");
        int id = ((msg[1] & 0xff) << 24) | ((msg[2] & 0xff) << 16) | ((msg[3] & 0xff) << 8) | (msg[4] & 0xff);
        int dataType = ((msg[5] & 0xff) << 24) | ((msg[6] & 0xff) << 16) | ((msg[7] & 0xff) << 8) | (msg[8] & 0xff);
        int len = ((msg[9] & 0xff) << 24) | ((msg[10] & 0xff) << 16) | ((msg[11] & 0xff) << 8) | (msg[12] & 0xff);
        Channel c = getChannel(id);
        if (c == null) throw new IOException("Unexpected SSH_MSG_CHANNEL_EXTENDED_DATA message for non-existent channel " + id);
        if (dataType != Packets.SSH_EXTENDED_DATA_STDERR) throw new IOException("SSH_MSG_CHANNEL_EXTENDED_DATA message has unknown type (" + dataType + ")");
        if (len != (msglen - 13)) throw new IOException("SSH_MSG_CHANNEL_EXTENDED_DATA message has wrong len (calculated " + (msglen - 13) + ", got " + len + ")");
        if (log.isEnabled()) log.log(80, "Got SSH_MSG_CHANNEL_EXTENDED_DATA (channel " + id + ", " + len + ")");
        synchronized (c) {
            if (c.state == Channel.STATE_CLOSED) return;
            if (c.state != Channel.STATE_OPEN) throw new IOException("Got SSH_MSG_CHANNEL_EXTENDED_DATA, but channel is not in correct state (" + c.state + ")");
            if (c.localWindow < len) throw new IOException("Remote sent too much data, does not fit into window.");
            c.localWindow -= len;
            System.arraycopy(msg, 13, c.stderrBuffer, c.stderrWritepos, len);
            c.stderrWritepos += len;
            c.notifyAll();
        }
    }
