    public void msgChannelSuccess(byte[] msg, int msglen) throws IOException {
        if (msglen != 5) throw new IOException("SSH_MSG_CHANNEL_SUCCESS message has wrong size (" + msglen + ")");
        int id = ((msg[1] & 0xff) << 24) | ((msg[2] & 0xff) << 16) | ((msg[3] & 0xff) << 8) | (msg[4] & 0xff);
        Channel c = getChannel(id);
        if (c == null) {
            c = getChannelByRemoteID(id);
            if (c != null) log.log(40, "SSH_MSG_CHANNEL_SUCCESS has sender channel number; adjusting");
        }
        if (c == null) throw new IOException("Unexpected SSH_MSG_CHANNEL_SUCCESS message for non-existent channel " + id);
        synchronized (c) {
            c.successCounter++;
            c.notifyAll();
        }
        if (log.isEnabled()) log.log(80, "Got SSH_MSG_CHANNEL_SUCCESS (channel " + id + ")");
    }
