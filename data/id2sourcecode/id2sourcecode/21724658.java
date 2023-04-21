    public void msgChannelWindowAdjust(byte[] msg, int msglen) throws IOException {
        if (msglen != 9) throw new IOException("SSH_MSG_CHANNEL_WINDOW_ADJUST message has wrong size (" + msglen + ")");
        int id = ((msg[1] & 0xff) << 24) | ((msg[2] & 0xff) << 16) | ((msg[3] & 0xff) << 8) | (msg[4] & 0xff);
        int windowChange = ((msg[5] & 0xff) << 24) | ((msg[6] & 0xff) << 16) | ((msg[7] & 0xff) << 8) | (msg[8] & 0xff);
        Channel c = getChannel(id);
        if (c == null) throw new IOException("Unexpected SSH_MSG_CHANNEL_WINDOW_ADJUST message for non-existent channel " + id);
        synchronized (c) {
            final long huge = 0xFFFFffffL;
            c.remoteWindow += (windowChange & huge);
            if ((c.remoteWindow > huge)) c.remoteWindow = huge;
            c.notifyAll();
        }
        if (log.isEnabled()) log.log(80, "Got SSH_MSG_CHANNEL_WINDOW_ADJUST (channel " + id + ", " + windowChange + ")");
    }
