    public int readBytes(byte[] bytes) throws JMSException {
        if (input) {
            throw new MessageNotReadableException("Message in write-only mode. Reset to read from message.");
        }
        int remaining = buffer.remaining();
        return remaining - buffer.get(bytes, 0, remaining < bytes.length ? remaining : bytes.length).remaining();
    }
