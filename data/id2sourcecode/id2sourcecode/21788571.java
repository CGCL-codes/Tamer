    private ByteBufferOutputStream beforeWrite() throws JMSException {
        if (this.in != null) throw new MessageNotWriteableException("can not write while reading, call reset() first");
        if (this.out == null) {
            this.out = new ByteBufferOutputStream(ByteBuffer.wrap(this.body.getData()), 0.5f);
            this.out.getBuffer().position(this.out.getBuffer().capacity());
            this.body.setData(Bytes.EMPTY_ARRAY);
        }
        return this.out;
    }
