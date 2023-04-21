    protected void onChannelData(SshMsgChannelData msg) throws java.io.IOException {
        buffer.getOutputStream().write(msg.getChannelData());
        int read;
        byte[] tmp = new byte[4];
        byte[] msgdata;
        while (buffer.getInputStream().available() > 4) {
            if (nextMessageLength == -1) {
                read = 0;
                while ((read += buffer.getInputStream().read(tmp)) < 4) {
                    ;
                }
                nextMessageLength = (int) ByteArrayReader.readInt(tmp, 0);
            }
            if (buffer.getInputStream().available() >= nextMessageLength) {
                msgdata = new byte[nextMessageLength];
                buffer.getInputStream().read(msgdata);
                messageStore.addMessage(msgdata);
                nextMessageLength = -1;
            } else {
                break;
            }
        }
    }
