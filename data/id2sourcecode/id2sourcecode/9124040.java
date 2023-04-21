    public void setLength(final int length) throws HeapException {
        this.length = length;
        if (numberOfByteReadedWrited > this.length) {
            throw new HeapException("too much byte readed/writed=" + numberOfByteReadedWrited + " length=" + length);
        }
    }
