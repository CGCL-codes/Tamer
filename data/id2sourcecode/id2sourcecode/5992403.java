    public boolean writeMessage(FileMessage msg) throws IllegalArgumentException, IOException {
        if (!openForWrite) throw new IllegalArgumentException("Can't write message, this factory is reading.");
        if (log.isDebugEnabled()) log.debug("Message " + msg + " data " + msg.getData() + " data length " + msg.getDataLength() + " out " + out);
        if (out != null) {
            out.write(msg.getData(), 0, msg.getDataLength());
            nrOfMessagesProcessed++;
            out.flush();
            if (msg.getMessageNumber() == msg.getTotalNrOfMsgs()) {
                out.close();
                cleanup();
                return true;
            }
        } else {
            if (log.isWarnEnabled()) log.warn("Receive Message again -- Sender ActTimeout to short [ path: " + msg.getContextPath() + " war: " + msg.getFileName() + " data: " + msg.getData() + " data length: " + msg.getDataLength() + " ]");
        }
        return false;
    }
