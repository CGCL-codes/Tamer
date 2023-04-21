    public void msgChannelData(byte[] msg, int msglen) throws IOException {
        if (msglen <= 9) throw new IOException("SSH_MSG_CHANNEL_DATA message has wrong size (" + msglen + ")");
        int id = ((msg[1] & 0xff) << 24) | ((msg[2] & 0xff) << 16) | ((msg[3] & 0xff) << 8) | (msg[4] & 0xff);
        int len = ((msg[5] & 0xff) << 24) | ((msg[6] & 0xff) << 16) | ((msg[7] & 0xff) << 8) | (msg[8] & 0xff);
        Channel c = getChannel(id);
        if (c == null) throw new IOException("Unexpected SSH_MSG_CHANNEL_DATA message for non-existent channel " + id);
        if (len != (msglen - 9)) throw new IOException("SSH_MSG_CHANNEL_DATA message has wrong len (calculated " + (msglen - 9) + ", got " + len + ")");
        if (log.isEnabled()) log.log(80, "Got SSH_MSG_CHANNEL_DATA (channel " + id + ", " + len + ")");
        synchronized (c) {
            if (c.state == Channel.STATE_CLOSED) return;
            if (c.state != Channel.STATE_OPEN) throw new IOException("Got SSH_MSG_CHANNEL_DATA, but channel is not in correct state (" + c.state + ")");
            if (c.localWindow < len) throw new IOException("Remote sent too much data, does not fit into window.");
            c.localWindow -= len;
            System.arraycopy(msg, 9, c.stdoutBuffer, c.stdoutWritepos, len);
            c.stdoutWritepos += len;
            c.notifyAll();
        }
    }
