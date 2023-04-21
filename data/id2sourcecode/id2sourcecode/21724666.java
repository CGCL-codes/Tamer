    public void msgChannelOpenFailure(byte[] msg, int msglen) throws IOException {
        if (msglen < 5) throw new IOException("SSH_MSG_CHANNEL_OPEN_FAILURE message has wrong size (" + msglen + ")");
        TypesReader tr = new TypesReader(msg, 0, msglen);
        tr.readByte();
        int id = tr.readUINT32();
        Channel c = getChannel(id);
        if (c == null) throw new IOException("Unexpected SSH_MSG_CHANNEL_OPEN_FAILURE message for non-existent channel " + id);
        int reasonCode = tr.readUINT32();
        String description = tr.readString("UTF-8");
        String reasonCodeSymbolicName = null;
        switch(reasonCode) {
            case 1:
                reasonCodeSymbolicName = "SSH_OPEN_ADMINISTRATIVELY_PROHIBITED";
                break;
            case 2:
                reasonCodeSymbolicName = "SSH_OPEN_CONNECT_FAILED";
                break;
            case 3:
                reasonCodeSymbolicName = "SSH_OPEN_UNKNOWN_CHANNEL_TYPE";
                break;
            case 4:
                reasonCodeSymbolicName = "SSH_OPEN_RESOURCE_SHORTAGE";
                break;
            default:
                reasonCodeSymbolicName = "UNKNOWN REASON CODE (" + reasonCode + ")";
        }
        StringBuffer descriptionBuffer = new StringBuffer();
        descriptionBuffer.append(description);
        for (int i = 0; i < descriptionBuffer.length(); i++) {
            char cc = descriptionBuffer.charAt(i);
            if ((cc >= 32) && (cc <= 126)) continue;
            descriptionBuffer.setCharAt(i, '�');
        }
        synchronized (c) {
            c.EOF = true;
            c.state = Channel.STATE_CLOSED;
            c.setReasonClosed("The server refused to open the channel (" + reasonCodeSymbolicName + ", '" + descriptionBuffer.toString() + "')");
            c.notifyAll();
        }
        if (log.isEnabled()) log.log(50, "Got SSH_MSG_CHANNEL_OPEN_FAILURE (channel " + id + ")");
    }
