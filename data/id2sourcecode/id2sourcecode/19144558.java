    private void processProxy() throws IOException {
        byte[] read_buffer = new byte[1000];
        int len = 0;
        do {
            try {
                len = inputStream.read(read_buffer, 0, 1000);
                if (len > 0) {
                    proxyStream.write(read_buffer, 0, len);
                    proxyStream.flush();
                }
            } catch (InterruptedIOException ex) {
                System.out.println(getName() + " proxy server thread is interrupted");
                if (isInterrupted()) {
                    return;
                }
            }
        } while (len >= 0);
        if (len == -1) {
            if (isInterrupted() == true) {
                return;
            }
            if (parentThread.sendMessage(new AceInputSocketStreamMessage(this, read_buffer, 0, AceInputSocketStreamMessage.EOF_REACHED, userParm, null)) == false) {
                System.err.println(getName() + ": AceInputSocketStream.processRead() -- Could not send EOF message to the requesting thread: " + getErrorMessage());
            }
            return;
        }
    }
