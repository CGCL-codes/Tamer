    public BufferedTextSocket(final int readBufSizeVal, final int writeBufSizeVal) {
        _readBufSize = Math.max(DEFAULT_READBUF_SIZE, readBufSizeVal);
        _writeBufSize = Math.max(DEFAULT_WRITEBUF_SIZE, writeBufSizeVal);
    }
