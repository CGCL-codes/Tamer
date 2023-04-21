    public void msgChannelOpenConfirmation(byte[] msg, int msglen) throws IOException {
        PacketChannelOpenConfirmation sm = new PacketChannelOpenConfirmation(msg, 0, msglen);
        Channel c = getChannel(sm.recipientChannelID);
        if (c == null) throw new IOException("Unexpected SSH_MSG_CHANNEL_OPEN_CONFIRMATION message for non-existent channel " + sm.recipientChannelID);
        synchronized (c) {
            if (c.state != Channel.STATE_OPENING) throw new IOException("Unexpected SSH_MSG_CHANNEL_OPEN_CONFIRMATION message for channel " + sm.recipientChannelID);
            c.remoteID = sm.senderChannelID;
            c.remoteWindow = sm.initialWindowSize & 0xFFFFffffL;
            c.remoteMaxPacketSize = sm.maxPacketSize;
            c.state = Channel.STATE_OPEN;
            c.notifyAll();
        }
        if (log.isEnabled()) log.log(50, "Got SSH_MSG_CHANNEL_OPEN_CONFIRMATION (channel " + sm.recipientChannelID + " / remote: " + sm.senderChannelID + ")");
    }
