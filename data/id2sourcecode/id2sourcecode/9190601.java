    public synchronized void processOutput() {
        LogUtil.entering(log);
        while ((hasOutput()) && getChannel().isOpen()) {
            if (!hasOutBuffer()) {
                String nextLine = (String) outLines.removeFirst();
                byte[] outBytes = nextLine.getBytes();
                outBuffer = ByteBuffer.wrap(outBytes);
                setHasOutBuffer(true);
            }
            ByteBuffer buf = outBuffer;
            try {
                int nbytes = getChannel().write(buf);
                addSentBytesNum(nbytes);
                if (buf.remaining() == 0) {
                    setHasOutBuffer(false);
                    outBuffer = null;
                } else {
                    break;
                }
            } catch (IOException e) {
                System.out.println("Error sending...: " + e.getMessage());
                try {
                    getChannel().close();
                } catch (IOException e2) {
                }
                doClose = true;
            }
        }
        if (hasOutput()) {
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        } else {
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }
