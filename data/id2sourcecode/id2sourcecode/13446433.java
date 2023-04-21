    public int getChannelData(Channel c, boolean extended, byte[] target, int off, int len) throws IOException {
        int copylen = 0;
        int increment = 0;
        int remoteID = 0;
        int localID = 0;
        synchronized (c) {
            int stdoutAvail = 0;
            int stderrAvail = 0;
            while (true) {
                stdoutAvail = c.stdoutWritepos - c.stdoutReadpos;
                stderrAvail = c.stderrWritepos - c.stderrReadpos;
                if ((!extended) && (stdoutAvail != 0)) break;
                if ((extended) && (stderrAvail != 0)) break;
                if ((c.EOF) || (c.state != Channel.STATE_OPEN)) return -1;
                try {
                    c.wait();
                } catch (InterruptedException ignore) {
                    throw new InterruptedIOException();
                }
            }
            if (!extended) {
                copylen = (stdoutAvail > len) ? len : stdoutAvail;
                System.arraycopy(c.stdoutBuffer, c.stdoutReadpos, target, off, copylen);
                c.stdoutReadpos += copylen;
                if (c.stdoutReadpos != c.stdoutWritepos) System.arraycopy(c.stdoutBuffer, c.stdoutReadpos, c.stdoutBuffer, 0, c.stdoutWritepos - c.stdoutReadpos);
                c.stdoutWritepos -= c.stdoutReadpos;
                c.stdoutReadpos = 0;
            } else {
                copylen = (stderrAvail > len) ? len : stderrAvail;
                System.arraycopy(c.stderrBuffer, c.stderrReadpos, target, off, copylen);
                c.stderrReadpos += copylen;
                if (c.stderrReadpos != c.stderrWritepos) System.arraycopy(c.stderrBuffer, c.stderrReadpos, c.stderrBuffer, 0, c.stderrWritepos - c.stderrReadpos);
                c.stderrWritepos -= c.stderrReadpos;
                c.stderrReadpos = 0;
            }
            if (c.state != Channel.STATE_OPEN) return copylen;
            if (c.localWindow < ((Channel.CHANNEL_BUFFER_SIZE + 1) / 2)) {
                int minFreeSpace = Math.min(Channel.CHANNEL_BUFFER_SIZE - c.stdoutWritepos, Channel.CHANNEL_BUFFER_SIZE - c.stderrWritepos);
                increment = minFreeSpace - c.localWindow;
                c.localWindow = minFreeSpace;
            }
            remoteID = c.remoteID;
            localID = c.localID;
        }
        if (increment > 0) {
            if (log.isEnabled()) log.log(80, "Sending SSH_MSG_CHANNEL_WINDOW_ADJUST (channel " + localID + ", " + increment + ")");
            synchronized (c.channelSendLock) {
                byte[] msg = c.msgWindowAdjust;
                msg[0] = Packets.SSH_MSG_CHANNEL_WINDOW_ADJUST;
                msg[1] = (byte) (remoteID >> 24);
                msg[2] = (byte) (remoteID >> 16);
                msg[3] = (byte) (remoteID >> 8);
                msg[4] = (byte) (remoteID);
                msg[5] = (byte) (increment >> 24);
                msg[6] = (byte) (increment >> 16);
                msg[7] = (byte) (increment >> 8);
                msg[8] = (byte) (increment);
                if (c.closeMessageSent == false) tm.sendMessage(msg);
            }
        }
        return copylen;
    }
