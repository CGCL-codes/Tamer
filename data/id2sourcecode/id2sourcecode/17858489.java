    private ByteBufferOutputStream beforeWrite(final int type) throws JMSException {
        if (this.in != null) throw new MessageNotWriteableException("can not write while reading, call reset() first");
        if (this.out == null) {
            this.out = new ByteBufferOutputStream(ByteBuffer.wrap(this.data), 0.5f);
            this.out.getBuffer().position(this.out.getBuffer().capacity());
            this.data = null;
        }
        try {
            this.out.writeByte(type);
        } catch (final IOException ex) {
            throw exceptionOnWrite(ex);
        }
        return this.out;
    }
