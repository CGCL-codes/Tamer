    public void writeBytes(byte[] bytes, int offset, int length) throws JMSException {
        if (input) {
            buffer.put(bytes, offset, length);
        } else {
            throw new MessageNotWriteableException("Body is in read-only mode. Clear body to write.");
        }
    }
