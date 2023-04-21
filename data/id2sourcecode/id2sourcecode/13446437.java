    public void msgChannelRequest(byte[] msg, int msglen) throws IOException {
        TypesReader tr = new TypesReader(msg, 0, msglen);
        tr.readByte();
        int id = tr.readUINT32();
        Channel c = getChannel(id);
        if (c == null) throw new IOException("Unexpected SSH_MSG_CHANNEL_REQUEST message for non-existent channel " + id);
        String type = tr.readString("US-ASCII");
        boolean wantReply = tr.readBoolean();
        if (log.isEnabled()) log.log(80, "Got SSH_MSG_CHANNEL_REQUEST (channel " + id + ", '" + type + "')");
        if (type.equals("exit-status")) {
            if (wantReply != false) throw new IOException("Badly formatted SSH_MSG_CHANNEL_REQUEST message, 'want reply' is true");
            int exit_status = tr.readUINT32();
            if (tr.remain() != 0) throw new IOException("Badly formatted SSH_MSG_CHANNEL_REQUEST message");
            synchronized (c) {
                c.exit_status = new Integer(exit_status);
                c.notifyAll();
            }
            if (log.isEnabled()) log.log(50, "Got EXIT STATUS (channel " + id + ", status " + exit_status + ")");
            return;
        }
        if (type.equals("exit-signal")) {
            if (wantReply != false) throw new IOException("Badly formatted SSH_MSG_CHANNEL_REQUEST message, 'want reply' is true");
            String signame = tr.readString("US-ASCII");
            tr.readBoolean();
            tr.readString();
            tr.readString();
            if (tr.remain() != 0) throw new IOException("Badly formatted SSH_MSG_CHANNEL_REQUEST message");
            synchronized (c) {
                c.exit_signal = signame;
                c.notifyAll();
            }
            if (log.isEnabled()) log.log(50, "Got EXIT SIGNAL (channel " + id + ", signal " + signame + ")");
            return;
        }
        if (wantReply) {
            byte[] reply = new byte[5];
            reply[0] = Packets.SSH_MSG_CHANNEL_FAILURE;
            reply[1] = (byte) (c.remoteID >> 24);
            reply[2] = (byte) (c.remoteID >> 16);
            reply[3] = (byte) (c.remoteID >> 8);
            reply[4] = (byte) (c.remoteID);
            tm.sendAsynchronousMessage(reply);
        }
        if (log.isEnabled()) log.log(50, "Channel request '" + type + "' is not known, ignoring it");
    }
